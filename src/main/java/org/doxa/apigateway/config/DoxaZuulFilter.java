package org.doxa.apigateway.config;

import com.google.common.collect.Lists;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.doxa.apigateway.config.openidc.OauthConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;

@Slf4j
@Component
public class DoxaZuulFilter extends ZuulFilter {

    @Autowired
    private OauthConfig config;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        boolean shouldFilter = false;
        RequestContext ctx = RequestContext.getCurrentContext();
        String URI = ctx.getRequest().getRequestURI();

        if ( URI.contains("auth/token")) {
            shouldFilter = true;
        }
        return shouldFilter;
    }

    @Override
    public Object run() {
        try {
            RequestContext context = getCurrentContext();
            InputStream in = (InputStream) context.get("requestEntity");
            if (in == null) {
                in = context.getRequest().getInputStream();
            }
            StringBuilder body = new StringBuilder(StreamUtils.copyToString(in, StandardCharsets.UTF_8));
            String addOn = String.format("client_id=%s&client_secret=%s&redirect_uri=%s&grant_type=authorization_code", config.getClientId(), config.getClientSecret(), config.getRedirectUri());
            body.append("&").append(addOn);
            log.info("Body {} " + body.toString());
            byte[] bytes = body.toString().getBytes(StandardCharsets.UTF_8);
            context.setRequest(new CustomHttpServletRequest(context.getRequest(), bytes));
            context.addZuulRequestHeader("content-type", "application/x-www-form-urlencoded");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

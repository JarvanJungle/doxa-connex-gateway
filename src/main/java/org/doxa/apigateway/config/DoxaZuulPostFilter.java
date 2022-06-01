package org.doxa.apigateway.config;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.doxa.apigateway.config.openidc.OauthConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;

@Slf4j
@Component
public class DoxaZuulPostFilter extends ZuulFilter {

    @Autowired
    private OauthConfig config;

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        try {
            RequestContext context = getCurrentContext();
            log.info("running here");
            log.info("Response body {}" + context.getResponseBody());
//            context.addZuulRequestHeader("test", "test123");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

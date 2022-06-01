package org.doxa.apigateway.config;

import com.netflix.zuul.http.ServletInputStreamWrapper;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

public class CustomHttpServletRequest extends HttpServletRequestWrapper {

    private final byte[] bytes;

    public CustomHttpServletRequest(HttpServletRequest request, byte[] bytes) {
        super(request);
        request.setAttribute("Content-Type", "application/x-www-form-urlencoded");
        this.bytes = bytes;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStreamWrapper(bytes);
    }

    @Override
    public int getContentLength() {
        return bytes.length;
    }

    @Override
    public long getContentLengthLong() {
        return bytes.length;
    }
}
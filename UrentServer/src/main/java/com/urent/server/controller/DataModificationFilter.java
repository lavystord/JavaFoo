package com.urent.server.controller;

import com.urent.server.domain.DataModificationLog;
import com.urent.server.service.DataModificationLogService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * Created by Administrator on 2015/8/22.
 */

@Component
public class DataModificationFilter implements Filter {
    private Logger logger = Logger.getLogger(DataModificationFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
      /*
      * 由于后面可能要重复读取request的body，所以这里需要进行一层封装
       */

        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        if(httpServletRequest.getMethod() != "GET" && (httpServletRequest.getContentType() == null || (httpServletRequest.getContentType().contains("application/json"))
                && httpServletRequest.getContentLength() < 2000)) {
            CustomHttpServletRequestWrapper wrapper = new CustomHttpServletRequestWrapper((HttpServletRequest) servletRequest);
            filterChain.doFilter(wrapper, servletResponse);
        }
        else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }



    public class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {

        private final String body;

        public CustomHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
            super(request);

            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = null;

            try {
                InputStream inputStream = request.getInputStream();

                if (inputStream != null) {
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

                    char[] charBuffer = new char[128];
                    int bytesRead = -1;

                    while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                        stringBuilder.append(charBuffer, 0, bytesRead);
                    }
                } else {
                    stringBuilder.append("");
                }
            } catch (IOException ex) {
                throw ex;
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException ex) {
                        throw ex;
                    }
                }
            }

            body = stringBuilder.toString();
        }

        @Override
        public ServletInputStream getInputStream () throws IOException {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes("UTF-8"));

            ServletInputStream inputStream = new ServletInputStream() {
                public int read () throws IOException {
                    return byteArrayInputStream.read();
                }
            };

            return inputStream;
        }


        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(this.getInputStream()));
        }

        public String getBody() {
            return body;
        }

    }
}

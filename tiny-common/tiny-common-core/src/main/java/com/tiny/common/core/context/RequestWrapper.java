package com.tiny.common.core.context;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * @description: 对HttpServletRequest进行包装，处理header、reader、body
 * @author: wzh
 * @date: 2023/09/20 11:16
 */
public class RequestWrapper extends HttpServletRequestWrapper {

    private final String body;

    public RequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        StringBuilder sb = new StringBuilder();
        InputStream ins = request.getInputStream();
        BufferedReader isr = null;
        try {
            if (ins != null) {
                isr = new BufferedReader(new InputStreamReader(ins));
                char[] charBuffer = new char[128];
                int readCount;
                while ((readCount = isr.read(charBuffer)) != -1) {
                    sb.append(charBuffer, 0, readCount);
                }
            }
        } finally {
            if (isr != null) {
                isr.close();
            }
        }
        body = sb.toString();
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayIns = new ByteArrayInputStream(body.getBytes());
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayIns.read();
            }
        };
    }

}
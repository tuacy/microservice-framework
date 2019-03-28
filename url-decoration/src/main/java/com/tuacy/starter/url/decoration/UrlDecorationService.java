package com.tuacy.starter.url.decoration;

/**
 * 自定义业务实现
 */
public class UrlDecorationService {

    /**
     * http or https
     */
    private String protocol;
    /**
     * 地址 127.0.0.1
     */
    private String host;
    /**
     * 端口 8000
     */
    private int port;

    public UrlDecorationService(UrlDecorationServiceProperties properties) {
        this.protocol = properties.getProtocol();
        this.host = properties.getHost();
        this.port = properties.getPort();
    }

    public String url(String paramter) {
        return protocol + "//" + host + ":" + port + "/" + paramter;
    }
}

package com.tuacy.starter.url.decoration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置文件实体映射体
 */
@ConfigurationProperties("url")
public class UrlDecorationServiceProperties {

    /**
     * http or https
     */
    private String protocol = "http";
    /**
     * 地址 127.0.0.1
     */
    private String host = "127.0.0.1";
    /**
     * 端口 8000
     */
    private int port = 8080;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

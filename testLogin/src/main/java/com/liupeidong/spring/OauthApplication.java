package com.liupeidong.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

/**
 * @creator liupeidong
 * @createTime 2019-11-27 20:10
 * @description
 **/
@EnableDiscoveryClient
@EnableOAuth2Client
@SpringBootApplication
public class OauthApplication {
    public static void main(String[] args) {
        SpringApplication.run(OauthApplication.class, args);
        System.out.println("==============Oauth启动成功=======================");
    }
}

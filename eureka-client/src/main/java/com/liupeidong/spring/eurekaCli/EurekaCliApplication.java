package com.liupeidong.spring.eurekaCli;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class EurekaCliApplication {
    @Value("${server.port}")
    String port;
    @Value("${eureka.client.serviceUrl.defaultZone}")
    String eurekaUrl;
    public static void main(String[] args) {
        // new SpringApplicationBuilder(EurekaCliApplication.class).web(true).run(args);
        SpringApplication.run(EurekaCliApplication.class, args);
    }
    // just for test

    @RequestMapping("/hello")
    public String home(@RequestParam(value = "name", defaultValue = "marus") String name) {
        return "hello " + name + " ,i am from port:" + port + "\n" + eurekaUrl;
    }
}

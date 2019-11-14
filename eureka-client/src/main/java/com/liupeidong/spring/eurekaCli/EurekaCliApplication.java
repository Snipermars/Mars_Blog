package com.liupeidong.spring.eurekaCli;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableEurekaClient
public class EurekaCliApplication {
    @Value("${server.port}")
    String port;
    @Value("${eureka.client.serviceUrl.defaultZone}")
    String eurekaUrl;
    public static void main(String[] args) {
        // new SpringApplicationBuilder(EurekaCliApplication.class).web(true).run(args);
        SpringApplication.run(EurekaCliApplication.class, args);
    }

    @RequestMapping("/hello")
    public String home(@RequestParam(value = "name", defaultValue = "adou") String name) {
        return "hello " + name + " ,i am from port:" + port + "\n" + eurekaUrl;
    }
}

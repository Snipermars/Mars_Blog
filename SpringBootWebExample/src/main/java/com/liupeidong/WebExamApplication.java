package com.liupeidong;

import com.liupeidong.filters.DruidStatFilter;
import com.liupeidong.filters.DruidStatViewServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import javax.servlet.annotation.WebFilter;

/**
 * @Author: Marus
 * @Description:
 * @Date: 2021/1/23 16:43
 * @Version:
 * @Modified:
 **/
@SpringBootApplication
@ServletComponentScan
public class WebExamApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebExamApplication.class, args);
    }
}

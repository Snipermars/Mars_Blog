package com.liupeidong.filters;

import com.alibaba.druid.support.http.WebStatFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * @Author: Marus
 * @Description: 配置监控拦截器, druid监控拦截器
 * @Date: 2021/1/25 13:17
 * @Version:
 * @Modified:
 **/
@WebFilter(filterName="druidWebStatFilter",
        urlPatterns="/*",
        initParams={
                @WebInitParam(name="exclusions",value="*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*"), // 忽略资源
        })
public class DruidStatFilter extends WebStatFilter {
}

package com.liupeidong.spring.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author fantasy
 * @date 2019-09-17
 * @time 17:13
 */

@Component
@ConfigurationProperties(prefix = "limit")
public class LoginLimit {
    // 锁定时间，单位min
    private Integer timeout;
    // 登录错误次数
    private Integer times;

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }
}

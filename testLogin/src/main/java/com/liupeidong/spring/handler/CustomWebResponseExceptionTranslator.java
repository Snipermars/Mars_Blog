package com.liupeidong.spring.handler;

import com.liupeidong.spring.conf.LoginLimit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @creator liupeidong
 * @createTime 2019-11-27 17:33
 * @description
 **/
@Component
public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private LoginLimit loginLimit;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomWebResponseExceptionTranslator.class);

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        ResponseEntity<OAuth2Exception> responseEntity = new DefaultWebResponseExceptionTranslator().translate(e);
        OAuth2Exception body = responseEntity.getBody();
        LOGGER.error("OAuth2Exception：{}", body.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setAll(responseEntity.getHeaders().toSingleValueMap());

        if (responseEntity.getStatusCodeValue() == 401 && body.getMessage().startsWith("错误次数已达上限")) {
            return new ResponseEntity<>(body, headers, HttpStatus.valueOf(405));
        }

        if ("Bad credentials".equals(body.getMessage())) {
            // 获取request
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String username = request.getParameter("username");
            if (!StringUtils.isBlank(username)) {
                ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
                String key = "limit:" + username;
                String time = valueOperations.get(key);
                if (time == null) {
                    valueOperations.set(key, "1", loginLimit.getTimeout(), TimeUnit.MINUTES);
                } else {
                    valueOperations.set(key, String.valueOf(Integer.parseInt(time) + 1), loginLimit.getTimeout(), TimeUnit.MINUTES);
                }
            }
        }

        // do something with header or response
        return new ResponseEntity<>(body, headers, responseEntity.getStatusCode());
    }

}

package com.liupeidong.spring.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.liupeidong.spring.conf.LoginLimit;
import com.liupeidong.spring.entity.CustomUserDetails;
import com.liupeidong.spring.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.liupeidong.spring.utils.JsonMapper;

import java.util.*;

/**
 * @creator liupeidong
 * @createTime 2019-11-27 17:14
 * @description
 **/
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private LoginLimit loginLimit;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${psp.getUserInfo}")
    private String osp_getUserInfo;//osp获取用户信息接口


    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        validLoginLimit(userName);

        User user;
        UserDetails userDetails = null;

        Boolean getDetail = false;

        try {
            if( ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPassword() != null){
                getDetail = true;
            }
        } catch(Exception e) {
            getDetail = false;
        }

        user = getUserByRestTemplate(userName, getDetail);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        userDetails = new CustomUserDetails(user.getPwd(), userName, user,
                true, true, true, true, authorities);

        return userDetails;
    }

    private void validLoginLimit(String userName) {
        // 错误次数校验
        String time = redisTemplate.opsForValue().get("limit:" + userName);
        if (time != null && Integer.parseInt(time) >= loginLimit.getTimes()) {
            throw new UnauthorizedUserException("错误次数已达上限，请稍后登录！");
        }
    }

    private User getUserByRestTemplate(String userName, Boolean getDetail) {
        User user;
        String response = restTemplate.getForEntity(osp_getUserInfo + userName, String.class).getBody();
        JsonMapper jsonMapper = new JsonMapper();
        Map<String, Object> dataJson = jsonMapper.convertToMap(response);

        String resCode = dataJson.getOrDefault("resCode", "").toString();
        if ("00100000".equals(resCode)) {
            JSONObject objJson = JSONObject.parseObject(dataJson.get("obj").toString());
            user = getUserInfo(objJson, getDetail);
        } else {
            throw new UsernameNotFoundException("User not found:" + userName);
        }
        return user;
    }

    private User getUserInfo(JSONObject objJson, Boolean getDetail) {
        User user = new User();

        user.setUsername(objJson.getString("loginName"));
        user.setRealname(objJson.getString("name"));
        user.setPwd(objJson.getString("password"));

        LOGGER.info("登录名: " + objJson.getString("loginName"));

        if(!getDetail){
            return user;
        }
        LOGGER.info("------getUserInfo-----getDetail-----");
        return user;
    }
}

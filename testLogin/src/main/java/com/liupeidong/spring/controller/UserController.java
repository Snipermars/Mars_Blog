package com.liupeidong.spring.controller;

import com.liupeidong.spring.entity.CustomUserDetails;
import com.liupeidong.spring.entity.Message;
import com.liupeidong.spring.entity.ResponseCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @creator liupeidong
 * @createTime 2019-11-27 17:36
 * @description
 **/
@RestController
public class UserController {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    ConsumerTokenServices consumerTokenServices;

    /**
     * 验证用户
     * @return
     */
    @GetMapping("/user")
    public Object user() {
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername
                (((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());

        Map<String, Object> user = new HashMap<>();
        user.put("username", userDetails.getUser().getUsername());
        user.put("data", userDetails.getUser());

        if (user != null) {
            // 清除登录错误限制
            String key = "limit:" + user.get("username");
            redisTemplate.delete(key);
        }

        return user;
    }

    /**
     * 销毁token
     * @param request
     * @return
     */
    @GetMapping("/revokeToken")
    public Message revokeToken(HttpServletRequest request) {
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername
                (((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        String username = userDetails.getUser().getUsername();
        String token = request.getHeader("Authorization").replace("Bearer", "").trim();
        redisTemplate.delete("user:" + username);
        redisTemplate.delete("currentPositionCode:" + username);
        if (consumerTokenServices.revokeToken(token)){
            return new Message(ResponseCodeEnum.RETURN_CODE_100200, null);
        }else{
            return new Message(ResponseCodeEnum.RETURN_CODE_100500, "系统异常");
        }
    }
}

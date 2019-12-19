package com.liupeidong.spring.gateway.filter;


import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;


/**
 * @author fantasy
 * @date 2018/4/23
 * @time 17:51
 */

@Component
public class JwtFilter extends ZuulFilter {

    @Value("${jwt.key}")
    private String key;

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private Logger logger = LogManager.getLogger(JwtFilter.class);
    private String defaultDetails = "{data: {username:'null', realname: 'null'}}";

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();

        OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        String details = null;
        String authorization = "";

        if (auth != null) {
            try {
                details = MAPPER.writeValueAsString(auth.getUserAuthentication().getDetails());
                OAuth2AuthenticationDetails oauthDetails = (OAuth2AuthenticationDetails) auth.getDetails();
                authorization = oauthDetails.getTokenValue();
            } catch (JsonProcessingException e) {
                logger.error(e);
            }
        }
        details = details == null ? defaultDetails : details;

        JSONObject detail = JSONObject.parseObject(JSONObject.parseObject(details).getString("data"));
        JSONObject data = new JSONObject();
        String username = detail.getString("username");
        data.put("username", username);
        data.put("realname", detail.getString("realname"));
        String jwt = JwtHelper.encode(data.toJSONString(), new MacSigner(key)).getEncoded();
        // ctx.addZuulRequestHeader(HystrixHeaderInterceptor.JWT_TOKEN, jwt);
        // HystrixHeaderInterceptor.jwttoken.set(jwt);
        // HystrixHeaderInterceptor.authorization.set(authorization);
        return true;
    }

    @Override
    public Object run() {
        return null;
    }
}

package com.liupeidong.spring.controller;

import com.alibaba.fastjson.JSONObject;
import com.liupeidong.spring.conf.FrontConfig;
import com.liupeidong.spring.entity.Message;
import com.liupeidong.spring.entity.ResponseCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import com.liupeidong.spring.utils.AESUtil;
import com.liupeidong.spring.utils.MD5;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @creator liupeidong
 * @createTime 2019-11-27 17:39
 * @description
 **/
@EnableOAuth2Client
@Controller
@RequestMapping("/oauth")
public class SSOControllerPSP {
    private static final Logger LOGGER = LoggerFactory.getLogger(SSOControllerPSP.class);

    private static String AESSecret = "98C9B19562EDC3AFD51653CB523E3134";

    @Value("${psp.getUserInfo}")
    private String getUserInfo;

    @Value("${oauth.token.url")
    private String oauthTokenUrl;

    @Value("${front.logoutUrlPrefix}")
    private String logoutUrlPrefix;

    @Value("${front.iconUrlPrefix}")
    private String iconUrlPrefix;

    @Autowired
    private FrontConfig frontConfig;

    @RequestMapping("/sso")
    public String getAccessToken(HttpServletRequest request, Model model){

        // 从webgate获取用户的基本信息
        Map<String, String> headers = new HashMap<>();
        Enumeration headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            headers.put(key, value);
        }

        LOGGER.info("webgate响应头 " + headers);
        String username = headers.get("oam_uid");
        String logoutUrl= headers.get("x-forwarded-host");
        String proto = headers.get("x-forwarded-proto");

        // logoutUrl 提取app标识
        String app="";
        if(!logoutUrl.isEmpty()){
            String ruleStr = "[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)";
            Pattern p = Pattern.compile(ruleStr, Pattern.CASE_INSENSITIVE);
            Matcher matcher = p.matcher(logoutUrl);
            matcher.find();
            app = matcher.group().substring(0, matcher.group().indexOf("."));
            if(!app.isEmpty()){
                int index = app.indexOf("-");
                if(index > -1) {
                    app = app.substring(0, index);
                }
            }
        }

        LOGGER.info("appFlag:" + app);
        String frontUrl = frontConfig.getConfig().get(app + ".url");
        String title = frontConfig.getConfig().get(app + ".title");
        String iconUrl = iconUrlPrefix + app + ".ico";
        LOGGER.info("LDAP用户登录： " + username);
        LOGGER.info("frontUrl: " + frontUrl);

        if ("false".equals(frontConfig.getConfig().get(app + ".icon"))) {
            iconUrl = iconUrlPrefix + "favicon.ico";
        }

        if (logoutUrl.contains(",")) {
            logoutUrl = proto + "://" + StringUtils.split(logoutUrl, ",")[0];
        } else {
            logoutUrl = proto + "://" + logoutUrl;
        }

        // 获取accessToken
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForEntity(getUserInfo + username, String.class).getBody();
        JSONObject userObj = JSONObject.parseObject(response);
        String password = userObj.getJSONObject("obj").getString("password");

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("response_type", "token");
        params.add("username", username);
        params.add("password", MD5.md5Password(password));
        HttpHeaders headerParams = new HttpHeaders();
        headerParams.add("Authorization", "Basic Y2xpZW50OnNlY3JldA==");
        HttpEntity requestEntity = new HttpEntity(params, headerParams);

        String result = restTemplate.postForObject(oauthTokenUrl, requestEntity, String.class);
        String accessToken = JSONObject.parseObject(result).getString("access_token");
        model.addAttribute("frontUrl", frontUrl);
        model.addAttribute("logoutUrl", logoutUrlPrefix + logoutUrl);
        model.addAttribute("accessToken", accessToken);
        model.addAttribute("title", title);
        model.addAttribute("iconUrl", iconUrl);
        return "sso";
    }

    @RequestMapping("/ssofmk")
    @ResponseBody
    public Object getTcAccessToken(HttpServletRequest request, Model model) {
        //从集成门户获取username
        String username=null;
        //String logoutUrl=null;
        Map<String,Object> resultToken =new HashMap<>();
        //从url中获取相应参数
        String OamUid=request.getParameter("oam_uid");
        //String app=request.getParameter("app");
        //String positionId=request.getParameter("_posId");
        try {
            username= AESUtil.deCiphering(OamUid,AESSecret);
        }catch (Exception e){
            e.printStackTrace();
            return new Message(ResponseCodeEnum.RETURN_CODE_100007,"");
        }
        if(StringUtils.isEmpty(username)){
            return new Message(ResponseCodeEnum.RETURN_CODE_100011,"");
        }

        // 获取accessToken
        RestTemplate restTemplate = new RestTemplate();
        String password=null;
        try {
            String response = restTemplate.getForEntity(getUserInfo + username, String.class).getBody();
            JSONObject userObj = JSONObject.parseObject(response);
            password = userObj.getJSONObject("obj").getString("password");
        }catch (Exception e){
            e.printStackTrace();
            return new Message(ResponseCodeEnum.RETURN_CODE_100008,"");
        }
        if(StringUtils.isEmpty(password)){
            return new Message(ResponseCodeEnum.RETURN_CODE_100010,"");
        }
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("response_type", "token");
        params.add("username", username);
        params.add("password", MD5.md5Password(password));
        HttpHeaders headerParams = new HttpHeaders();
        headerParams.add("Authorization", "Basic Y2xpZW50OnNlY3JldA==");
        HttpEntity requestEntity = new HttpEntity(params, headerParams);
        String accessToken=null;
        try {
            String result = restTemplate.postForObject(oauthTokenUrl, requestEntity, String.class);
            accessToken = JSONObject.parseObject(result).getString("access_token");
        }catch (Exception e){
            e.printStackTrace();
            return new Message(ResponseCodeEnum.RETURN_CODE_100009,"");
        }
        return new Message(ResponseCodeEnum.RETURN_CODE_100200,accessToken);
    }
}

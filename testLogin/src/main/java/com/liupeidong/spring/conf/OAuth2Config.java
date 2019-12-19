package com.liupeidong.spring.conf;

import com.liupeidong.spring.handler.CustomWebResponseExceptionTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import com.liupeidong.spring.service.impl.CustomUserDetailsService;

@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    RedisConnectionFactory redisConnectionFactory;


    @Value("${oauth.client.name}")
    private String client;

    @Value("${oauth.client.secret}")
    private String secret;

    @Value("${oauth.client.authorities}")
    private String authorities;

    @Value("${oauth.client.validitySeconds}")
    private int validitySeconds;

    @Autowired
    private CustomWebResponseExceptionTranslator customWebResponseExceptionTranslator;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(new RedisTokenStore(redisConnectionFactory))
                .userDetailsService(userDetailsService)
                .exceptionTranslator(customWebResponseExceptionTranslator);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer)
            throws Exception {
        oauthServer.checkTokenAccess("isAuthenticated()");
        //允许表单认证
        oauthServer.allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(client) // client_id
                .secret(secret) // secret
                .authorities(authorities)
                .authorizedGrantTypes("authorization_code", "refresh_token", "password")
                .accessTokenValiditySeconds(validitySeconds)
                .scopes("read", "write", "trust");
    }

}

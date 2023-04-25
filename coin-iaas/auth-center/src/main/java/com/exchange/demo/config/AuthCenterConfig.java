package com.exchange.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @Author: ElevenYang
 * @Description:
 * @Date 2023/4/25 22:06
 */
@Configuration
@EnableAuthorizationServer // 开启授权服务器功能
public class AuthCenterConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Qualifier("userServiceDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

//    @Autowired
//    private RedisConnectionFactory redisConnectionFactory;

    /**
     * 添加第三方的客户端
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("coin-api") // 第三方客户端的名称
                .secret(passwordEncoder.encode("coin-secret")) //  第三方客户端的密钥
                .scopes("all") //第三方客户端的授权范围
                .authorizedGrantTypes("password", "refresh_token")
                .accessTokenValiditySeconds(7 * 24 * 3600) // token的有效期
                .refreshTokenValiditySeconds(30 * 24 * 3600)// refresh_token的有效期
                .and()
                .withClient("inside-app")
                .secret(passwordEncoder.encode("inside-secret"))
                .authorizedGrantTypes("client_credentials")
                .scopes("all")
                .accessTokenValiditySeconds(Integer.MAX_VALUE);
        ;
        super.configure(clients);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .tokenStore(jwtTokenStore())// tokenStore 来存储我们的token jwt 存储token
                .tokenEnhancer(jwtAccessTokenConverter());
        super.configure(endpoints);
    }

//    private TokenStore redisTokenStore() {
//        return new RedisTokenStore(redisConnectionFactory);
//    }

    private TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
        // 加载我们的私钥
        ClassPathResource classPathResource = new ClassPathResource("xxx-exchange.jks");
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(classPathResource, "xxx-exchange".toCharArray());
        tokenConverter.setKeyPair(keyStoreKeyFactory.getKeyPair("xxx-exchange", "xxx-exchange".toCharArray()));
        return tokenConverter;
    }
}

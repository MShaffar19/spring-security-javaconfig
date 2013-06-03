/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.security.oauth2.config.annotation.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpointHandlerMapping;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.endpoint.WhitelabelApprovalEndpoint;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;

/**
 * @author Rob Winch
 *
 */
@Configuration
public abstract class OAuth2ServerConfigurerAdapter extends WebSecurityConfigurerAdapter {
    @Bean
    public AuthorizationEndpoint authorizationEndpoint() throws Exception {
        AuthorizationEndpoint authorizationEndpoint = new AuthorizationEndpoint();
        authorizationEndpoint.setTokenGranter(tokenGranter());
        authorizationEndpoint.setClientDetailsService(clientDetails());
        authorizationEndpoint.setAuthorizationCodeServices(authorizationCodeServices());
        return authorizationEndpoint;
    }

    @Bean
    public ConsumerTokenServices consumerTokenServices() throws Exception {
        return oauthConfigurator().getConsumerTokenServices();
    }

    /**
     * @return
     */
    private AuthorizationCodeServices authorizationCodeServices() throws Exception {
        return oauthConfigurator().getAuthorizationCodeServices();
    }

    /**
     * @return
     */
    private TokenGranter tokenGranter() throws Exception {
        return oauthConfigurator().getTokenGranter();
    }

    private OAuth2ServerConfigurator oauthConfigurator() throws Exception {
        return getHttp().getConfigurator(OAuth2ServerConfigurator.class);
    }

    @Bean
    public TokenEndpoint tokenEndpoint() throws Exception {
        TokenEndpoint tokenEndpoint = new TokenEndpoint();
        tokenEndpoint.setClientDetailsService(clientDetails());
        tokenEndpoint.setTokenGranter(tokenGranter());
        return tokenEndpoint;
    }

    @Bean
    public AuthorizationCodeTokenGranter authorizationTokenGranter() throws Exception {
        return new AuthorizationCodeTokenGranter(tokenServices(), authorizationCodeServices(), clientDetails());
    }

    /**
     * @return
     * @throws Exception
     */
    protected AuthorizationServerTokenServices tokenServices() throws Exception {
        return oauthConfigurator().getTokenServices();
    }

    @Bean
    public WhitelabelApprovalEndpoint approvalEndpoint() {
        return new WhitelabelApprovalEndpoint ();
    }

    @Bean
    public FrameworkEndpointHandlerMapping endpointHandlerMapping() {
        return new FrameworkEndpointHandlerMapping();
    }

    protected abstract ClientDetailsService clientDetails();
}

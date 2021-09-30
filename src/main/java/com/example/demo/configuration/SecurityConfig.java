package com.example.demo.configuration;

import com.example.demo.security.ApiAccessDeniedHandler;
import com.example.demo.security.ApiAuthenticationEntryPoint;
import com.example.demo.security.ApiTokenFilter;
import com.example.demo.service.UserService;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserService userService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Order(1)
    @Configuration
    public static class ApiWebSecurityConfiguration extends WebSecurityConfigurerAdapter {
        @Value("${app.api.token}")
        private String token;

        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            httpSecurity
                    .csrf().disable()
                    //Фильтр только для запросов API
                    .antMatcher("/api/**")
                    //Правила авторизации
//                    .authorizeRequests()
//                        //Все запросы требуют аутентификации
//                        .anyRequest().authenticated()
//                        .and()
                    //Отключение сохранения состояния сеанса
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    //Настройка обработки ошибок
                    .exceptionHandling()
                        .accessDeniedHandler(new ApiAccessDeniedHandler())
                        .authenticationEntryPoint(new ApiAuthenticationEntryPoint())
                    .and()
                        //.authenticationEntryPoint(new Http403ForbiddenEntryPoint());
                    //Фильтр авторизации по токену
                    .addFilterBefore(new ApiTokenFilter(token), BasicAuthenticationFilter.class);
        }
    }

    @Order(2)
    @Configuration
    public static class KeycloakWebSecurityConfiguration extends KeycloakWebSecurityConfigurerAdapter {
        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth)
        {
            var keycloakAuthenticationProvider = keycloakAuthenticationProvider();
            keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
            auth.authenticationProvider(keycloakAuthenticationProvider);
        }

        @Override
        protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
            return new NullAuthenticatedSessionStrategy();
        }

        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception
        {
            super.configure(httpSecurity);
            httpSecurity
                    .csrf().disable()
                    .requestMatchers((requestMatchers) ->
                            requestMatchers
                                    .antMatchers("/keycloak/**", "/sso/login")
                    )
                    .authorizeRequests()
                            .antMatchers("/sso/login").permitAll()
                            .anyRequest().fullyAuthenticated()
            ;
        }

        @Bean
        public KeycloakSpringBootConfigResolver keycloakSpringBootConfigResolver()
        {
            return new KeycloakSpringBootConfigResolver();
        }
    }

    @Order(3)
    @Configuration
    public static class FormLoginWebSecurityConfiguration extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            httpSecurity
                    .csrf().disable()
                    //Правила авторизации
                    .authorizeRequests()
                        //Доступ только для не зарегистрированных пользователей
                        .antMatchers("/user/registration", "/login").not().fullyAuthenticated()
                        //Доступ только для пользователей с ролью Администратор
                        .antMatchers("/admin/**").hasRole("ADMIN")
                        //Доступ разрешен всем пользователей
                        .antMatchers("/", "/resources/**").permitAll()
                        //Все остальные страницы требуют аутентификации
                        //.anyRequest().authenticated()
                    .and()
                    //Настройка для входа в систему
                    .formLogin()
                        .loginPage("/login")
                        //Перенарпавление на главную страницу после успешного входа
                        .defaultSuccessUrl("/")
                        //.permitAll()
                        .and()
                    .logout()
                        .permitAll()
                        .logoutSuccessUrl("/login");
        }
    }
}

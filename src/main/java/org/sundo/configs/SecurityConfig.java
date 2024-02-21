package org.sundo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.userDetailsService() -> UserDetailsService 구현 클래스
        // .passwordEncoder(passworEncoder())
    }


    /**
     * 설정 무력화
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll();
//                .antMatchers("/exam/admin").access("hasRole('ROLE_ADMIN')")
//                .antMatchers("/exam/member").access("hasRole('ROLE_MEMBER')");
//        http.formLogin()
//                .loginPage("/customLogin")
//                .loginProcessingUrl("/login")
//                .successHandler(loginSuccessHandler());
//        http.logout()
//                .logoutUrl("/customLogout")
//                .invalidateHttpSession(true)
//                .deleteCookies("remember-me", "JSESSION_ID");
//        http.rememberMe().key("project").tokenRepository(persistentTokenRepository())
//                .tokenValiditySeconds(604800);
    }

    /**
     * 비밀번호 해시화
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

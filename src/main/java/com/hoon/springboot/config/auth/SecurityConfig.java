package com.hoon.springboot.config.auth;

import com.hoon.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity  //Spring Security 설정 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable().headers().frameOptions().disable() //h2-console 화면을 사용하기 위해서 해당 옵션 disable
                .and()
                    .authorizeRequests()    //url별 권한 관리를 설정하는 옵션의 시작점
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll()
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                    .anyRequest().authenticated()
                .and()
                    .logout()
                        .logoutSuccessUrl("/")
                .and()
                    .oauth2Login()  //oauth2 로그인 기능에 대한 여러 설정의 진입점
                        .userInfoEndpoint() //Oauth2 로그인 성공 후 사용자 정보를 가져올 때의 설정 담당
                            .userService(customOAuth2UserService);  //소셜 로그인 성공 후 후속 조치를 조치할 UserService 인터페이스 구현체 등록
                                                                    //리소스 서버에서 사용자 정보를 가져온 상태에서 추가로 진행하고자하는 기능 명시 가능
    }

}
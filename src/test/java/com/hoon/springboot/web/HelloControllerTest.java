package com.hoon.springboot.web;

import com.hoon.springboot.config.auth.SecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)    //JUnit에 내장된 실행자가 아닌 다른 실행자 실행. 스프링부트 테스트와 JUnit 사이에 연결자 역할
@WebMvcTest(controllers = HelloController.class, //여러 스프링 테스트 어노테이션 중, Spring MVC에 집중할 수 있는 어노테이션. 컨트롤러 테스트할 때 붙임.
                                                 // 컨틀롤러 관련 빈들만 스캔함
        excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)    // SecurityConfig를 생성하기 위한 CustomOauth2UserService가 없어서 에러발생.
                                                                                                    // 스캔 안하도록 변경
        }
)
public class HelloControllerTest {

    @Autowired
    private MockMvc mvc;

    @WithMockUser(roles="USER")
    @Test
    public void hello_리턴된다() throws Exception {
        String hello = "hello";
        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello));
    }

    @WithMockUser(roles="USER")
    @Test
    public void helloDto가_리턴된다() throws Exception {
        String name = "hello";
        int amount = 1000;

        mvc.perform(
                get("/hello/dto")
                    .param("name", name)
                    .param("amount", String.valueOf(amount)))   //String만 가능. 숫자 날짜 등 다 스트링으로 변환해줘야함.
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))     //jsonPath : 응답값을 필드별로 검증. $를 기준으로 필드명 명시
                .andExpect(jsonPath("$.amount", is(amount)));

    }

}
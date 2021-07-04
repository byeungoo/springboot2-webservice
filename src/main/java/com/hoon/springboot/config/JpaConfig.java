package com.hoon.springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // JPA Auditing 활성화. 최소 하나의 Entity 클래스가 필요함. WebMvcTest할 때 에러나서 Application에 안붙이고 따로 config로 구성
public class JpaConfig {}

package com.finalteam3.midam.configuration;

import com.finalteam3.midam.interceptor.LoginCheck;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        addInterceptors(): 사용자가 생성한 인터셉터 클래스 객체를 등록
        registry.addInterceptor(new LoginCheck())
//                addPathPatterns(): 인터셉터를 동작시킬 주소 패턴 등록
                .addPathPatterns("/reservation/*");
//               excludePathPatterns(): 인터셉터에서 제외할 주소 패턴 등록
    }
}
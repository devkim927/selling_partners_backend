package com.sellingPartners.backEnd.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.filter.CorsFilter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod; // comet - GET, POST, PUT, DELETE 별 인증여부 관리.
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	

  private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

  private final CorsFilter corsFilter;
  private final SessionAccessDeniedHandler sessionAccessDeniedHandler;
  private final SessionAuthenticationEntryPoint sessionAuthenticationEntryPoint;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring()
            .requestMatchers("/resources/**", "/css/**", "/vendor/**",
                    "/js/**", "/favicon/**", "/img/**");
  }
  
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
      return authenticationConfiguration.getAuthenticationManager();
  }

  @SuppressWarnings("removal")
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    
    CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
    requestHandler.setCsrfRequestAttributeName("_csrf");
    
    http.cors(cors -> {})
        .csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .csrfTokenRequestHandler(requestHandler)
        )
        .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(exceptionHandling -> exceptionHandling
            .accessDeniedHandler(sessionAccessDeniedHandler)
            .authenticationEntryPoint(sessionAuthenticationEntryPoint)
        )
        .sessionManagement(sessionManagement -> sessionManagement
            .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
            .sessionFixation().changeSessionId()
            .maximumSessions(1)
            .maxSessionsPreventsLogin(false)
        )
        .authorizeRequests()
        .requestMatchers(
        		"/api/v1/signup", "/api/v1/login","api/v1/test", 
        		"/error","/error/**" 						// comet - /error 경로 추가
        		).permitAll()
        // 프로젝트 단일 조회 (GET) - 인증 없이 허용
        .requestMatchers(HttpMethod.GET, 
        		"/api/v1/projects",
        		"/api/v1/projects/*"
        		).permitAll()
// 여기는 필요 시 주석 해제 후 사용.
//        //(DELETE) - 인증 필요
//        .requestMatchers(HttpMethod.DELETE, 
//        		
//        		).authenticated()
//        //(PUT) - 인증 필요
//        .requestMatchers(HttpMethod.PUT, 
//        		
//        		).authenticated()
//        //(DELETE) - 인증 필요
//        .requestMatchers(HttpMethod.POST, 
//
//        		).authenticated()
        
        .anyRequest().authenticated()
        .and()
        .logout(logout -> logout
                .logoutUrl("/api/v1/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(200);
                    response.getWriter().write("로그아웃 성공");
                })
                .invalidateHttpSession(true)
                .clearAuthentication(true)  
                .deleteCookies("JSESSIONID") 
         )
        .formLogin().disable()
        .httpBasic().disable();

    return http.build();
  }
  
}
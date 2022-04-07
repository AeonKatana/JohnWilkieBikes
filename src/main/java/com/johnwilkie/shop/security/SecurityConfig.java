package com.johnwilkie.shop.security;

import java.io.IOException;

import javax.naming.AuthenticationException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  private UserDetailsService service;
  
  @Bean
  public PasswordEncoder passwordEncoder() {
    return (PasswordEncoder)new BCryptPasswordEncoder();
  }
  
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder());
    provider.setUserDetailsService(this.service);
    auth.authenticationProvider((AuthenticationProvider)provider);
    auth.inMemoryAuthentication().withUser("ADMIN").password(passwordEncoder().encode("adminroot21")).authorities("ADMIN");
  }
  
  protected void configure(HttpSecurity http) throws Exception {
       http.csrf().disable()
      .authorizeRequests()
      .antMatchers(new String[] { "/resources/**" }).permitAll()
      .antMatchers(new String[] { "/", "/product/**", "/category/**", "/addtocart/***,/register-page", "/register" }).permitAll()
      .antMatchers(new String[] { "/orders/**" ,"/user/**","/mycart/checkout/**", "/mycart/totalQty", "/paypal/**" }).authenticated()
      .antMatchers("/admin/**").hasAuthority("ADMIN")
      .and()
      .formLogin().loginPage("/login-page").usernameParameter("username").passwordParameter("pass")
      .loginProcessingUrl("/perform-login")
      .failureHandler(new AuthenticationFailureHandler() {
		@Override
		public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
				org.springframework.security.core.AuthenticationException exception)
				throws IOException, ServletException {
			// TODO Auto-generated method stub
			
		}
        }).failureUrl("/login-page?error")
      .and()
      .logout().logoutUrl("/perform-logout").invalidateHttpSession(true)
      .deleteCookies(new String[] { "JSESSIONID" }).logoutSuccessUrl("/");
  }
}

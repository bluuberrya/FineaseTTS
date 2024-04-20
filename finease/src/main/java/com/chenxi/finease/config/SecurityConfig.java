package com.chenxi.finease.config;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	/*
	 * @Autowired private Environment env;
	 */

	private static final String SALT = "salt"; // Salt should be protected carefully

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));

	}

	// @Override
	// protected void configure(HttpSecurity http) throws Exception {
    //     http.authorizeRequests(requests -> requests
    //             .antMatchers(PUBLIC_MATCHERS).permitAll()
    //             .anyRequest().authenticated()
    //             .and()
    //             .formLogin().loginPage("/index").permitAll().failureUrl("/index?error").defaultSuccessUrl("/userFront")
    //             .and()
    //             .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/index?logout").deleteCookies("remember-me").permitAll()
    //             .and()
    //             .rememberMe());
	// }

	// @Autowired
	// public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

	// 	// auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
	// 	// //This is in-memory authentication

	// 	auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder());

	// }

}

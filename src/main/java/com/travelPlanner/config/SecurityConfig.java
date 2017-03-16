package com.travelPlanner.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;

import com.travelPlanner.security.CsrfHeaderFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private LogoutSuccessHandler successHandler;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**").antMatchers("/webjars/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//@formatter:off
		http.authorizeRequests()
				.antMatchers("/", "/all/view/user").permitAll()
				// RESTful API Requests
//				.antMatchers(HttpMethod.GET, "/api/**").permitAll() //TODO: will need to filter by user (A user can't display other user's stuff)
				.antMatchers(HttpMethod.POST, "/api/users/**").permitAll()
				.antMatchers(HttpMethod.PUT, "/api/users/**").hasAnyRole("USER_MANAGER", "ADMIN")
				.antMatchers(HttpMethod.DELETE, "/api/users/**").hasAnyRole("USER_MANAGER", "ADMIN")
				.antMatchers(HttpMethod.GET, "/api/users").hasAnyRole("USER_MANAGER", "ADMIN")
				.antMatchers(HttpMethod.POST, "/api/trips/**").hasAnyRole("REGULAR_USER", "ADMIN")
				.antMatchers(HttpMethod.PUT, "/api/trips/**").hasAnyRole("REGULAR_USER", "ADMIN")
				.antMatchers(HttpMethod.DELETE, "/api/trips/**").hasAnyRole("REGULAR_USER", "ADMIN")
				// AngularJS routes
				.antMatchers("/usr/**").hasAnyRole("REGULAR_USER", "ADMIN")
				.antMatchers("/usrmgr/**").hasAnyRole("USER_MANAGER", "ADMIN")
				.anyRequest().authenticated()
			.and()
				.formLogin()
					.loginPage("/all/view/login").permitAll()
			.and()
				.httpBasic()
			.and()
				.logout()
					.logoutUrl("/logout").logoutSuccessHandler(successHandler)
					.deleteCookies("JSESSIONID","CURRENT_USER").invalidateHttpSession(false).permitAll()
			.and()
				.csrf()
					.disable()
				.addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
				.exceptionHandling()
					.authenticationEntryPoint(authenticationEntryPoint);
		//@formatter:on
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

}

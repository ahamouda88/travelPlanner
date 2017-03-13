package com.travelPlanner.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.Filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.travelPlanner.config.SecurityConfig;
import com.travelPlanner.config.SpringBootConfig;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { SpringBootConfig.class, SecurityConfig.class })
@TestExecutionListeners(
		listeners = { DependencyInjectionTestExecutionListener.class, WithSecurityContextTestExecutionListener.class })
@ActiveProfiles("test")
public class AuthenticationTest {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private Filter springSecurityFilterChain;

	private MockMvc mockMvc;

	private final static String USERS_PATH = "/api/users";
	private final static String TRIPS_PATH = "/api/trips";

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).addFilters(springSecurityFilterChain).build();
	}

	// TODO: This should fail, bec we need to check the role first!!!!
	@Test
	public void requiresAuthentication() throws Exception {
		mockMvc.perform(get(USERS_PATH)).andExpect(status().isUnauthorized());
	}

	@Test
	public void httpBasicAuthenticationSuccess() throws Exception {
		mockMvc.perform(get(USERS_PATH + "/currentuser").with(httpBasic("user", "password")))
				.andExpect(status().isNotFound()).andExpect(authenticated().withUsername("user"));
	}

	@Test
	public void authenticationSuccess() throws Exception {
		mockMvc.perform(get(USERS_PATH + "/currentuser").with(httpBasic("user", "password"))).andExpect(status().isOk())
				.andExpect(authenticated().withUsername("user"));
	}

	@Test
	public void authenticationFailed() throws Exception {
		mockMvc.perform(formLogin().user("user").password("invalid")).andExpect(status().isUnauthorized());
	}

	@EnableWebSecurity
	@Configuration
	public static class SecurityTestConfig extends WebSecurityConfigurerAdapter {
		@Autowired
		public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
			auth.inMemoryAuthentication().withUser("user").password("password").roles("ADMIN");
		}
	}
}

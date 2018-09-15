package hu.mik.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;

import hu.mik.constants.LdapConstants;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private static final String LOGIN_PAGE = "/login";
	private static final String REGISTRATION_PAGE = "/registration";
	private static final String[] FREE_PAGES_ANT = { "/VAADIN/**", "/vaadinServlet/**", LOGIN_PAGE, REGISTRATION_PAGE };
	private static final String DEFAULT_PAGE_URL = "/main#!home";
	private static final String ADMIN_PAGE_URL = "/**admin**";
	private static final String SECURITY_URL = "/j_spring_security_check";

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers(FREE_PAGES_ANT).permitAll().antMatchers(ADMIN_PAGE_URL).hasRole("ADMINS")
				.anyRequest().authenticated().and().formLogin().loginPage(LOGIN_PAGE).loginProcessingUrl(SECURITY_URL)
				.defaultSuccessUrl(DEFAULT_PAGE_URL, true).permitAll();
		http.headers().frameOptions().sameOrigin();
		http.csrf().disable();

//		SecurityContextPersistenceFilter filter=new SecurityContextPersistenceFilter(http.getSharedObject(SecurityContextRepository.class));
//		filter.setForceEagerSessionCreation(true);
//		http.addFilter(filter);
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		  auth.jdbcAuthentication().dataSource(dataSource)
//		  .passwordEncoder(passwordEncoder())
//		  .usersByUsernameQuery("select username, passwd, enabled from t_user where username=?")
//		  .authoritiesByUsernameQuery("select username, role from t_role where username=?");
		auth.ldapAuthentication().userSearchBase(LdapConstants.OU_USERS).userSearchFilter("uid={0}")
				.groupSearchBase(LdapConstants.OU_GROUPS).groupSearchFilter("member={0}").contextSource()
				.url("ldap://localhost:10389/" + LdapConstants.SEARCH_BASE).and().passwordCompare()
				.passwordEncoder(new LdapShaPasswordEncoder()).passwordAttribute("userPassword");
	}
}
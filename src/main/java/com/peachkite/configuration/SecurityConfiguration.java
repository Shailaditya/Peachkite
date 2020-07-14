package com.peachkite.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import com.peachkite.security.PeachkiteAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	private static String REMEMBER_ME_KEY="eosdn)(*7sj#@";
	private static String REMEMBER_ME_COOKIE_NAME="REMEMBER-ME";
	private static Integer REMEMBER_ME_TOKEN_VALIDITY=60 * 60 * 24 *365 * 10;

	@Autowired
	PeachkiteAuthenticationProvider authenticationProvider;

	@Autowired
	UserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/api/secure/**").hasAnyRole("USER")
				.antMatchers("/api/super_admin/**").hasRole("ADMIN")
				.antMatchers("/api/admin/**").hasAnyRole("COMPANY_ADMIN","ADMIN")
				.anyRequest()
				.permitAll()
				.and()
				.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
				.and()
				.rememberMe()
				.rememberMeServices(rememberMeServices())
				.key(REMEMBER_ME_KEY)
				.tokenValiditySeconds(REMEMBER_ME_TOKEN_VALIDITY)
				.and()
				.exceptionHandling();
	}

	@Bean
	public TokenBasedRememberMeServices rememberMeServices() {
		TokenBasedRememberMeServices rememberMeService=new TokenBasedRememberMeServices(REMEMBER_ME_KEY, userDetailsService);
		rememberMeService.setCookieName(REMEMBER_ME_COOKIE_NAME);
		rememberMeService.setTokenValiditySeconds(REMEMBER_ME_TOKEN_VALIDITY);
		rememberMeService.setAlwaysRemember(true);
		return rememberMeService;
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider);
	}

}

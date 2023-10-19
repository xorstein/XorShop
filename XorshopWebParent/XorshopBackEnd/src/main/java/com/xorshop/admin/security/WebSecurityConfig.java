package com.xorshop.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

	@Bean
	public UserDetailsService userDetailsService() {
		return new XorshopUserDetailsService();
	}

	@Bean
	public PasswordEncoder PasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider autprovider = new DaoAuthenticationProvider();
		autprovider.setUserDetailsService(userDetailsService());
		autprovider.setPasswordEncoder(PasswordEncoder());
		return autprovider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	/*
	 * @Override public void configure(HttpSecurity http) throws Exception {
	 * http.authorizeRequests().antMatchers("/users/**","/roles/**","/setting/**").
	 * hasAuthority("Super User")
	 * .anyRequest().authenticated().and().formLogin().loginPage("/login").
	 * usernameParameter("emailid").permitAll().and().logout().permitAll().and().
	 * rememberMe().key("AbcDefgHijKlmOpqrs_1234567890").tokenValiditySeconds(7*24*
	 * 60*60);; //http.authorizeRequests().anyRequest().permitAll();
	 * 
	 * 
	 * }
	 */
	/*
	 * @Bean public SecurityFilterChain filterChain(HttpSecurity http) throws
	 * Exception { http.csrf(csrf -> csrf.disable()) .exceptionHandling(exception ->
	 * exception.authenticationEntryPoint(unauthorizedHandler))
	 * .sessionManagement(session ->
	 * session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	 * .authorizeHttpRequests(auth ->
	 * auth.requestMatchers("/api/auth/**").permitAll()
	 * .requestMatchers("/api/test/**").permitAll() .anyRequest().authenticated() );
	 * 
	 * http.authenticationProvider(authenticationProvider());
	 * 
	 * http.addFilterBefore(authenticationJwtTokenFilter(),
	 * UsernamePasswordAuthenticationFilter.class);
	 * 
	 * return http.build(); }
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		/*
		 * http.authorizeHttpRequests()
		 * .requestMatchers("/users/**","/roles/**","/setting/**","/admins/**",
		 * "/menus/**","/footer/**").hasAuthority("Admin") .anyRequest().authenticated()
		 * .and() .formLogin() .loginPage("/login") .defaultSuccessUrl("/", true)
		 * .usernameParameter("emailid") .and() .logout() .invalidateHttpSession(true)
		 * .clearAuthentication(true) .logoutRequestMatcher(new
		 * AntPathRequestMatcher("/logout")) .logoutSuccessUrl("/login?logout")
		 * .deleteCookies("JSESSIONID") .permitAll();
		 */
//         .and()
//         .rememberMe()
//         .rememberMeParameter("login-remember")
//         .key("AbcDefgHijKlmOpqrs_1234567890")
//         .tokenValiditySeconds(7*24*60*60);
		http.authorizeHttpRequests(authConfig -> {
			authConfig.requestMatchers("/users/**", "/roles/**", "/setting/**", "/admins/**", "/menus_website/**",
					"/footer_website/**", "/countries/**", "/states/**").hasAuthority("Admin");
			authConfig.requestMatchers("/users/**", "/settings/**", "/countries/**", "/states/**")
					.hasAuthority("Admin");
			authConfig.requestMatchers("/categories/**", "/brands/**", "/menus/**", "/articles/**")
					.hasAnyAuthority("Admin", "Editeur");
			authConfig.requestMatchers("/products/new", "/products/delete/**").hasAnyAuthority("Admin", "Editeur");
			authConfig.requestMatchers("/products/edit/**", "/products/save", "/products/check_unique")
					.hasAnyAuthority("Admin", "Editeur", "Vendeur");
			authConfig.requestMatchers("/products", "/products/", "/products/detail/**", "/products/page/**")
					.hasAnyAuthority("Admin", "Editeur", "Vendeur", "Livreur");
			authConfig.requestMatchers("/products/**", "/articles/**", "/menus/**", "/sections/**")
					.hasAnyAuthority("Admin", "Editeur");
			authConfig.requestMatchers("/customers/**", "/shipping/**", "/articles/**", "/get_shipping_cost",
					"/reports/**").hasAnyAuthority("Admin", "Vendeur");
			authConfig.requestMatchers("/orders", "/orders/", "/orders/page/**", "/orders/detail/**")
					.hasAnyAuthority("Admin", "Vendeur", "Livreur");
			authConfig.requestMatchers("/states/list_by_country/**").hasAnyAuthority("Admin", "Vendeur");
			authConfig.requestMatchers("/orders_Livreur/update/**").hasAuthority("Livreur");
			authConfig.requestMatchers("/products/detail/**", "/customers/detail/**").hasAnyAuthority("Admin",
					"Editeur", "Vendeur", "Assistant");
			authConfig.requestMatchers("/reviews/**", "/questions/**").hasAnyAuthority("Admin", "Assistant");
			authConfig.anyRequest().authenticated();
		}).formLogin(login -> {
			login.loginPage("/login");
			login.defaultSuccessUrl("/", true);
			login.usernameParameter("emailid");
			login.permitAll();
		}).logout(logout -> {
			logout.invalidateHttpSession(true);
			logout.clearAuthentication(true);
			logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
			logout.logoutSuccessUrl("/login?logout");
			logout.deleteCookies("JSESSIONID");
			logout.permitAll();
		}).rememberMe(rememberme -> {
			rememberme.key("AbcDefgHijKlmdserOpqrs_1234567890");
			rememberme.rememberMeCookieName("rememberlogin"); // it is name of the cookie
			// rememberme.rememberMeParameter("login-remember");
			rememberme.userDetailsService(userDetailsService());// IF token valid and authenticate correctly pull the
																// user from this service.
			rememberme.tokenValiditySeconds(7 * 24 * 60 * 60); // remember for number of seconds
		});
		http.headers().frameOptions().sameOrigin();

		return http.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/assets/**", "/webjars/**");
	}

}

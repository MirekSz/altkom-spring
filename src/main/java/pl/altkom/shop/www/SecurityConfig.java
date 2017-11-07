package pl.altkom.shop.www;

import javax.servlet.http.HttpSessionEvent;

import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.management.HttpSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionCreatedEvent;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;

import pl.altkom.shop.lib.Profiles;

@Configuration
// @ImportResource("classpath:security.xml")
@Profile(Profiles.WEB)
@EnableWebSecurity
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {
	@Bean
	@Override
	protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
		return new RegisterSessionAuthenticationStrategy(buildSessionRegistry());
	}

	@Bean
	protected SessionRegistry buildSessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(keycloakAuthenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		http.authorizeRequests().antMatchers("/api/**").hasAnyRole("REST").antMatchers("/product/**")
				.hasAnyRole("USER", "ADMIN").anyRequest().permitAll().and().logout().logoutUrl("/logout");
	}

	@EventListener
	public void sessionCreated(HttpSessionCreatedEvent se) {
		getApplicationContext().getBean(HttpSessionManager.class).sessionCreated(new HttpSessionEvent(se.getSession()));

	}

	@EventListener
	public void sessionDestroyed(HttpSessionDestroyedEvent se) {
		getApplicationContext().getBean(HttpSessionManager.class)
				.sessionDestroyed(new HttpSessionEvent(se.getSession()));

	}
}

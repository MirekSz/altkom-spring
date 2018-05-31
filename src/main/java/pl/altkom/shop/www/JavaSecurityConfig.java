package pl.altkom.shop.www;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionCreatedEvent;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.web.client.RestTemplate;

import pl.altkom.shop.jwt.JWTAuthenticationFilter;
import pl.altkom.shop.jwt.JWTLoginFilter;
import pl.altkom.shop.lib.Profiles;

@Configuration
@Profile(Profiles.WEB)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class JavaSecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		String idForEncode = "bcrypt";
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put(idForEncode, new BCryptPasswordEncoder());
		encoders.put("noop", NoOpPasswordEncoder.getInstance());
		encoders.put(null, NoOpPasswordEncoder.getInstance());
		encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
		encoders.put("scrypt", new SCryptPasswordEncoder());

		PasswordEncoder passwordEncoder = new DelegatingPasswordEncoder(idForEncode, encoders);
		return passwordEncoder;
	}

	@Bean
	public OAuth2AuthorizedClientService authorizedClientService() {
		return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
	}

	@Bean
	public ClientRegistrationRepository clientRegistrationRepository() {
		return new InMemoryClientRegistrationRepository(this.githubClientRegistration());
	}

	private ClientRegistration githubClientRegistration() {
		return CommonOAuth2Provider.GITHUB.getBuilder("github").clientId("81643f82708112d21007")
				.clientSecret("78fc4732b593024a709d2315e2ea50111a4ac532").build();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("user").password("user").roles("USER").and().withUser("admin")
				.password("{bcrypt}$2a$10$irVe7adKKLHJOVcz8F36t.Yj5VmqukcTRQwWSFC39duynKqFuzWzu").roles("ADMIN").and()
				.withUser("rest").password("rest").roles("REST");
	}

	@Configuration
	@Order(1)
	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/api/**").authorizeRequests().anyRequest().hasRole("REST").and().httpBasic().and()
					.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
					.addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		}
	}

	@Configuration
	public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().antMatchers("/public/**").permitAll().anyRequest().authenticated().and()
					.formLogin().loginPage("/login").permitAll().and().logout().logoutUrl("/logout").and()
					.sessionManagement().maximumSessions(1).and().and()
					.addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
					.addFilterAfter(new JWTLoginFilter(authenticationManager()),
							UsernamePasswordAuthenticationFilter.class)
					.oauth2Login().loginPage("/login").userInfoEndpoint()
					.customUserType(GitHubOAuth2User.class, "github");

		}

		@Bean
		public RoleHierarchy roleHierarchy() {
			RoleHierarchyImpl roleHierarchyImpl = new RoleHierarchyImpl();
			roleHierarchyImpl.setHierarchy(""//
					+ "ROLE_ADMIN > ROLE_STAFF \n"//
					+ "ROLE_STAFF > ROLE_USER \n"//
					+ "ROLE_USER > ROLE_GUEST");
			return roleHierarchyImpl;
		}

		@Bean
		public RoleHierarchyVoter roleVoter() {
			return new RoleHierarchyVoter(roleHierarchy());
		}

		@Bean
		public DefaultMethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler() {
			DefaultMethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler = new DefaultMethodSecurityExpressionHandler();
			defaultMethodSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
			return defaultMethodSecurityExpressionHandler;
		}

		@Bean
		public DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler() {
			DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
			defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
			return defaultWebSecurityExpressionHandler;
		}

	}

	@Bean
	public RestTemplate restTemplate(OAuth2AuthorizedClientService clientService) {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();

		messageConverters.add(new MappingJackson2HttpMessageConverter());
		RestTemplate template = new RestTemplate();
		template.setMessageConverters(messageConverters);
		template.getInterceptors().add(new ClientHttpRequestInterceptor() {

			@Override
			public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
					throws IOException {
				try {
					Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
					OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

					OAuth2AuthorizedClient client = clientService
							.loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());

					String headerValue = "Bearer " + client.getAccessToken().getTokenValue();
					request.getHeaders().set("Authorization", headerValue);
				} catch (Exception e) {

				}
				return execution.execute(request, body);
			}
		});
		return template;
	}

	@EventListener
	public void sessionCreated(HttpSessionCreatedEvent se) {
		System.out.println(se);

	}

	@EventListener
	public void sessionDestroyed(HttpSessionDestroyedEvent se) {
		System.out.println(se);

	}

	public static void main(String[] args) {
		String encode = new BCryptPasswordEncoder().encode("admin");
		System.out.println(encode);
	}

}

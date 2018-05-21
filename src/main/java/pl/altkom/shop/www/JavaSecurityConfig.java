package pl.altkom.shop.www;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

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

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("user").password("user").roles("USER").and().withUser("admin")
				.password("{bcrypt}$2a$10$irVe7adKKLHJOVcz8F36t.Yj5VmqukcTRQwWSFC39duynKqFuzWzu").roles("ADMIN").and()
				.withUser("rest").password("rest").roles("REST");
	}

	@Configuration
	@Profile(Profiles.WEB)
	@Order(1)
	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/api/**").authorizeRequests().anyRequest().hasRole("REST").and().httpBasic()
					.realmName("Altkom");
		}
	}

	@Configuration
	@Profile(Profiles.WEB)
	public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().antMatchers("/public/**").permitAll().anyRequest().authenticated().and()
					.formLogin().loginPage("/login").permitAll().and().logout().logoutUrl("/logout");

		}

	}

	public static void main(String[] args) {
		String encode = new BCryptPasswordEncoder().encode("admin");
		System.out.println(encode);
	}

}

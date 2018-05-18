package pl.altkom.shop.www;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import pl.altkom.shop.lib.Profiles;

@Configuration
@ImportResource("classpath:security.xml")
@Profile(Profiles.WEB)
public class SecurityConfig {
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
}

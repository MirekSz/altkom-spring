package pl.altkom.shop;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import pl.altkom.shop.lib.Profiles;

@Configuration
@Profile(Profiles.TEST)
public class TestConfig {

	@Bean
	@Primary
	public DataSource testDS() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1;MODE=MYSQL");
		dataSource.setUsername("sa");
		dataSource.setPassword("sa");
		return dataSource;

	}

}

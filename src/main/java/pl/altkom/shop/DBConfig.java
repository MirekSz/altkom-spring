package pl.altkom.shop;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@PropertySource(value = { "classpath:application.properties" })
@EnableJpaRepositories("pl.altkom.shop.repo")
public class DBConfig {

	@Value("${db.driverClassName}")
	private String driverClassName;
	@Value("${db.url}")
	private String url;
	@Value("${db.username}")
	private String username;
	@Value("${db.password}")
	private String password;

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource());
	}

	@Bean
	public DataSource dataSource() {
		// // DriverManagerDataSource dataSource = new
		// DriverManagerDataSource();
		// // dataSource.setDriverClassName(driverClassName);
		// // dataSource.setUrl(url);
		// // dataSource.setUsername(username);
		// // dataSource.setPassword(password);
		// //
		// // return dataSource;
		//
		// Pool
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(url);
		config.setDriverClassName(driverClassName);
		config.setUsername(username);
		config.setPassword(password);
		config.setMaximumPoolSize(10);
		return new HikariDataSource(config);

		// JNDI
		// JndiTemplate jndiTemplate = new JndiTemplate();
		// return (DataSource)
		// jndiTemplate.lookup("java:jboss/datasources/UsersDB");
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean emFactory = new LocalContainerEntityManagerFactoryBean();
		emFactory.setPersistenceUnitName("products");
		emFactory.setDataSource(dataSource());
		emFactory.setPackagesToScan(new String[] { "pl.altkom.shop.model" });
		emFactory.setJpaVendorAdapter(createHibernateAdapter());
		emFactory.getJpaPropertyMap().putAll(getHibernateProperties());
		return emFactory;
	}

	private HibernateJpaVendorAdapter createHibernateAdapter() {
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter.setGenerateDdl(true);
		hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
		return hibernateJpaVendorAdapter;
	}

	// jpa
	public Map<String, Object> getHibernateProperties() {
		Map<String, Object> properties = new HashMap();
		properties.put("hibernate.show_sql", true);
		properties.put("hibernate.format_sql", true);

		return properties;
	}

}

package pl.altkom.shop;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("pl.altkom.shop.mrepo")
public class MyBatisConfig {
	@Bean
	public SqlSessionFactoryBean sqlSessionFactory(DataSource ds) {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(ds);
		sqlSessionFactoryBean.setDatabaseIdProvider(new VendorDatabaseIdProvider());
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
		configuration.setLazyLoadingEnabled(true);
		sqlSessionFactoryBean.setConfiguration(configuration);
		sqlSessionFactoryBean.setDatabaseIdProvider(new VendorDatabaseIdProvider());
		return sqlSessionFactoryBean;
	}
}

package com.springbook.configuration;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

// application.properties 파일을 읽어서 동작하도록 @Configuration
@Configuration
@PropertySource("classpath:/application.properties")
public class DatasourceConfiguration {
	@Autowired
	private ApplicationContext applicationContext;
	
	// 컨테이너가 자동으로 bean 객체로 인식
	// hikari로 시작하는 것들을 읽게끔하는 hikariConfig()
	@Bean
	@ConfigurationProperties(prefix="spring.datasource.hikari")
	public HikariConfig hikariConfig() {
		return new HikariConfig();
	}
	
	// hikariConfig로 만든것을 DataSource()로 읽어들일 수 있게
	@Bean
	public DataSource dataSource() throws Exception {
		DataSource dataSource = new HikariDataSource(hikariConfig());
		return dataSource;
	}
	

	
	// 프레임 워크에서는 설정파일로 부트는 자바파일로 

	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception{
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:/mapper/**/*-mapping.xml"));
		return sqlSessionFactoryBean.getObject();
	}
	
	// sqlSessionFactory 생성자로 SqlSessionTemplate 사용하도록 설정
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}

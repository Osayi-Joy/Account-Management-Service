package com.interswitchgroup.accountmanagementsystem.common.config.database;

/**
 * @author Joy Osayi
 * @createdOn April-12(Fri)-2024
 */

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;

@Configuration
@ComponentScan("com.interswitchgroup.accountmanagementsystem")
@EntityScan("com.interswitchgroup.accountmanagementsystem")
@EnableJpaRepositories(basePackages = "com.interswitchgroup.accountmanagementsystem")
@EnableJpaAuditing
public class DataSourceConfig {

  @Value("${spring.datasource.url}")
  private String jdbcUrl;

  @Value("${spring.datasource.username}")
  private String username;

  @Value("${spring.datasource.password}")
  private String password;

  @Value("${spring.datasource.driver-class-name}")
  private String driverClassName;

  @Bean
  @Profile({"dev", "pilot", "prod", "test"})
  public DataSource dataSource() {
    return getHikariDataSource();
  }

  private DataSource getHikariDataSource() {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setDriverClassName(driverClassName);
    dataSource.setJdbcUrl(jdbcUrl);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    dataSource.setConnectionTimeout(30000L);
    return dataSource;
  }
}

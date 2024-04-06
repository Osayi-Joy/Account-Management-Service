package com.interswitchgroup.accountmanagementsystem;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AccountManagementSystemApplicationTests {

	@Autowired
	private DataSource dataSource;
	private final Logger LOG = LoggerFactory.getLogger(AccountManagementSystemApplicationTests.class);

	@Test
	void contextLoads() {
	}

	@Test
	void databaseConnectionTest() {
		LOG.info("Datasource connection --> {}", dataSource);
		assertThat(dataSource).isNotNull();

		try {
			Connection connection = dataSource.getConnection();
			assertThat(connection.getCatalog()).isEqualToIgnoringCase("account-management-db");
			LOG.info("connection --> {}", connection.getCatalog());
		}catch (SQLException ex){
			LOG.info("An error occurred --> {}", ex);
		}
	}
}


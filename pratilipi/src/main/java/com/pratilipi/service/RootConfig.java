package com.pratilipi.service;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.DbUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;

@Configuration
@Slf4j
public class RootConfig {

    protected JdbcTemplate jdbcTemplate;
    protected HikariDataSource bnDataSource;
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String USER = "nb";
    private static final String PASS = "";

    @Bean
    public JdbcTemplate getJdbcTemplate() {
        bnDataSource = new HikariDataSource();
        bnDataSource.setDriverClassName(JDBC_DRIVER);
        bnDataSource.setUsername(USER);
        bnDataSource.setPassword(PASS);
        bnDataSource.setJdbcUrl(DB_URL);
        jdbcTemplate = new JdbcTemplate(bnDataSource);
        createTables();
        return jdbcTemplate;
    }

    public void createTables() {
        createStoriesTable();
        createUsersTable();
    }

    public void createStoriesTable() {
        String createEntitiesTable = "CREATE MEMORY TABLE IF NOT EXISTS Stories ( id BIGINT PRIMARY KEY AUTO_INCREMENT , title VARCHAR(50) NOT NULL ,"
                + "url varchar(250) DEFAULT NULL,"
                + "live_set CLOB DEFAULT NULL ,"
                + "read_set CLOB DEFAULT NULL ,"
                + "updated_at TIMESTAMP DEFAULT now() , created_at TIMESTAMP DEFAULT now());";
        PreparedStatement statement = null;
        Connection connection = null;
        try {
            connection = bnDataSource.getConnection();
            statement = connection.prepareStatement(createEntitiesTable);
            statement.execute();
        } catch (Exception ex) {
            log.error("error occured in setup of Stories table, {}", ex);
        } finally {
            DbUtils.closeQuietly(statement);
            DbUtils.closeQuietly(connection);
        }
    }

    public void createUsersTable() {
        String createEntitiesTable = "CREATE MEMORY TABLE IF NOT EXISTS Users ( id BIGINT PRIMARY KEY AUTO_INCREMENT , username VARCHAR(50) NOT NULL ,"
                + "password CLOB DEFAULT NULL ,"
                + "updated_at TIMESTAMP DEFAULT now() , created_at TIMESTAMP DEFAULT now());";
        PreparedStatement statement = null;
        Connection connection = null;
        try {
            connection = bnDataSource.getConnection();
            statement = connection.prepareStatement(createEntitiesTable);
            statement.execute();
        } catch (Exception ex) {
            log.error("error occured in setup of Users table, {}", ex);
        } finally {
            DbUtils.closeQuietly(statement);
            DbUtils.closeQuietly(connection);
        }
    }
}

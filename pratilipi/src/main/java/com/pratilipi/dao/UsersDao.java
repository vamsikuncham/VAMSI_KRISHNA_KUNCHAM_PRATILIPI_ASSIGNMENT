package com.pratilipi.dao;

import com.pratilipi.dao.model.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class UsersDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static final String insertQuery = "INSERT INTO Users(username, password) VALUES (?,?)";
    public static final String UPDATE_QUERY = "UPDATE Users SET username = ? , password =  ? WHERE id = ?";
    public static final String SELECT_QUERY = "SELECT * FROM Users WHERE username = ?";

    public Long save(Users user) {
        Object key = getKey(user);
        KeyHolder holder = new GeneratedKeyHolder();
        if (Objects.isNull(key)) {
            jdbcTemplate.update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement statement = connection.prepareStatement(insertQuery, new String[]{"id"});
                    statement.setString(1, user.getUsername());
                    statement.setString(2, user.getPassword());
                    return statement;
                }
            }, holder);
            return holder.getKey().longValue();
        } else {
            jdbcTemplate.update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);
                    statement.setString(1, user.getUsername());
                    statement.setString(2, user.getPassword());
                    statement.setLong(3, Long.valueOf(key.toString()).longValue());

                    return statement;
                }
            });
            return Long.valueOf(key.toString());
        }
    }

    public Long getKey(Users user) {
        return (user.getId() == null || user.getId() == 0) ? null : user.getId();
    }

    public Users getUserDetails(String username) {
        return (Users) jdbcTemplate.queryForObject(SELECT_QUERY, new Object[]{username}, new BeanPropertyRowMapper(Users.class));
    }

}

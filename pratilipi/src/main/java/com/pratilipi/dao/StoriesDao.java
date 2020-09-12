package com.pratilipi.dao;

import com.pratilipi.dao.model.Stories;
import lombok.AllArgsConstructor;
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
public class StoriesDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static final String insertQuery = "INSERT INTO Stories(title, url, live_set, read_set) VALUES (?,?,?,?)";
    public static final String UPDATE_QUERY = "UPDATE Stories SET title = ? , url = ?, live_set =  ?, read_set = ? WHERE id = ?";
    public static final String SELECT_QUERY = "SELECT * FROM Stories WHERE title = ?";
    public static final String SELECT_ALL_QUERY = "SELECT * FROM Stories";

    public Long save(Stories story) {
        Object key = getKey(story);
        KeyHolder holder = new GeneratedKeyHolder();
        if (Objects.isNull(key)) {
            jdbcTemplate.update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement statement = connection.prepareStatement(insertQuery, new String[]{"id"});
                    statement.setString(1, story.getTitle());
                    statement.setString(2, story.getUrl());
                    statement.setString(3, story.getLiveSet());
                    statement.setString(4, story.getReadSet());
                    return statement;
                }
            }, holder);
            return holder.getKey().longValue();
        } else {
            jdbcTemplate.update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);
                    statement.setString(1, story.getTitle());
                    statement.setString(2, story.getUrl());
                    statement.setString(3, story.getLiveSet());
                    statement.setString(4, story.getReadSet());
                    statement.setLong(5, Long.valueOf(key.toString()).longValue());

                    return statement;
                }
            });
            return Long.valueOf(key.toString());
        }
    }

    public Long getKey(Stories story) {
        return (story.getId() == null || story.getId() == 0) ? null : story.getId();
    }

    public Stories getStoryDetails(String title) {
        return (Stories) jdbcTemplate.queryForObject(SELECT_QUERY, new Object[]{title}, new BeanPropertyRowMapper(Stories.class));
    }

    public List<Stories> getAllStories() {
        return jdbcTemplate.query(SELECT_ALL_QUERY, new BeanPropertyRowMapper(Stories.class));
    }
}

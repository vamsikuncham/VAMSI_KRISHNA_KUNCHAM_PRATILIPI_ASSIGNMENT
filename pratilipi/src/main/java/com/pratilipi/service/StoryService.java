package com.pratilipi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pratilipi.dao.StoriesDao;
import com.pratilipi.dao.model.Stories;
import com.pratilipi.model.StoryReadCountRequest;
import com.pratilipi.model.UserLoginRequest;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class StoryService {

    @Autowired
    private StoriesDao storiesDao;

    @Autowired
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    public static final String LIVE_COUNT="live_count";
    public static final String READ_COUNT="read_count";

    public void insertStoryData() {
        List<Stories> stories = storiesDao.getAllStories();
        if (Objects.isNull(stories) || stories.size()==0) {
            //insert dummy data
            Stories story1 = new Stories();
            story1.setTitle("Hare");
            story1.setUrl("Story1");
            storiesDao.save(story1);

            Stories story2 = new Stories();
            story2.setTitle("Monkey");
            story2.setUrl("Story2");
            storiesDao.save(story2);

            Stories story3 = new Stories();
            story3.setTitle("Pigeon");
            story3.setUrl("Story3");
            storiesDao.save(story3);
        }
    }

    //method  to get the read count based on username and also gets the live read count
    public Map<String, Integer> getReadCount(StoryReadCountRequest readCountRequest) throws IOException {
        //validate user creds
        UserLoginRequest loginRequest = new UserLoginRequest(readCountRequest.getUserName(), readCountRequest.getPassword());
        userService.checkUserDetails(loginRequest);

        insertStoryData();

        Map<String, Integer> counts = new HashMap<>();
        boolean dbUpdate = false;
        //get the story detail from DB
        Stories story = storiesDao.getStoryDetails(readCountRequest.getStoryTitle());

        //update the count in DB
        List<String> liveSet = new ArrayList<>();
        if (Objects.nonNull(story.getLiveSet()))
            liveSet = objectMapper.readValue(story.getLiveSet(), List.class);
        if (liveSet.contains(readCountRequest.getUserName()))
            counts.put(LIVE_COUNT, liveSet.size());
        else {
           liveSet.add(readCountRequest.getUserName());
            counts.put(LIVE_COUNT, liveSet.size());
           dbUpdate = true;
        }

        List<String> readSet = new ArrayList<>();
        if (Objects.nonNull(story.getReadSet()))
            readSet = objectMapper.readValue(story.getReadSet(), List.class);
        if (readSet.contains(readCountRequest.getUserName()))
            counts.put(READ_COUNT, readSet.size());
        else {
            readSet.add(readCountRequest.getUserName());
            counts.put(READ_COUNT, readSet.size());
            dbUpdate = true;
        }

        if(dbUpdate) {
            story.setLiveSet(objectMapper.writeValueAsString(liveSet));
            story.setReadSet(objectMapper.writeValueAsString(readSet));
            storiesDao.save(story);
        }
        //return the new counts
        return counts;
    }

    public void updateLiveCounts(UserLoginRequest request) throws IOException {
        //validate user creds
        userService.checkUserDetails(request);

        insertStoryData();

        //get all stories and remove user from live set
        List<Stories> stories = storiesDao.getAllStories();
        for (Stories story : stories) {
            if (Objects.nonNull(story.getLiveSet())) {
                List<String> liveSet = objectMapper.readValue(story.getLiveSet(), List.class);
                if (liveSet.contains(request.getUsername())) {
                    liveSet.remove(request.getUsername());
                    story.setLiveSet(objectMapper.writeValueAsString(liveSet));
                    storiesDao.save(story);
                }
            }
        }
    }

    public List<Stories> getStories() {
        insertStoryData();
        return storiesDao.getAllStories();
    }
}

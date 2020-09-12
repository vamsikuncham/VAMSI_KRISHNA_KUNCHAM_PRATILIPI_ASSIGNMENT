package com.pratilipi.controller;

import com.pratilipi.dao.model.Stories;
import com.pratilipi.model.StoryReadCountRequest;
import com.pratilipi.model.UserLoginRequest;
import com.pratilipi.service.StoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/v1/story")
public class StoryController {

    @Autowired
    public StoryService storyService;

    @RequestMapping(value = "/readCount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Integer> getReadCount(@RequestBody StoryReadCountRequest readCountRequest, @RequestHeader("X-AUTH-TOKEN") String authToken) throws Exception {
        if (!authToken.equals("abcd")) {
            throw new Exception("Unauthorized, wrong headers");
        }
        return storyService.getReadCount(readCountRequest);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void updateLiveCount(@RequestBody UserLoginRequest loginRequest, @RequestHeader("X-AUTH-TOKEN") String authToken) throws Exception {
        if (!authToken.equals("abcd")) {
            throw new Exception("Unauthorized, wrong headers");
        }
        storyService.updateLiveCounts(loginRequest);
    }

    @RequestMapping(value = "/stories", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Stories> getAllStories(@RequestHeader("X-AUTH-TOKEN") String authToken) throws Exception {
        if (!authToken.equals("abcd")) {
            throw new Exception("Unauthorized, wrong headers");
        }
        return storyService.getStories();
    }
}

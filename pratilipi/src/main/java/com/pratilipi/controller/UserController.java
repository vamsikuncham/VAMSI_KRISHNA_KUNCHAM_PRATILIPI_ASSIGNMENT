package com.pratilipi.controller;


import com.pratilipi.model.UserLoginRequest;
import com.pratilipi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void loginUser(@RequestParam("username") String username, @RequestParam("password") String password,
                          @RequestHeader("X-AUTH-TOKEN") String authToken) throws Exception {
        if (!authToken.equals("abcd")) {
            throw new Exception("Unauthorized, wrong headers");
        }
        UserLoginRequest userLoginRequest = new UserLoginRequest(username, password);
        userService.checkUserDetails(userLoginRequest);
    }

    @RequestMapping(value = "/validateUser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void validateUser(@RequestParam("username") String username,
                          @RequestHeader("X-AUTH-TOKEN") String authToken) throws Exception {
        if (!authToken.equals("abcd")) {
            throw new Exception("Unauthorized, wrong headers");
        }
        userService.getUserDetails(username);
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void validateUser(@RequestBody UserLoginRequest loginRequest,
                             @RequestHeader("X-AUTH-TOKEN") String authToken) throws Exception {
        if (!authToken.equals("abcd")) {
            throw new Exception("Unauthorized, wrong headers");
        }
        userService.signUp(loginRequest);
    }
}

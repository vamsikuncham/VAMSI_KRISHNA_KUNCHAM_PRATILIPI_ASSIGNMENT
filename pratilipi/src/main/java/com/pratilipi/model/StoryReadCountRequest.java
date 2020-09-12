package com.pratilipi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoryReadCountRequest {
    private String userName;
    private String password;
    private String storyTitle;
}

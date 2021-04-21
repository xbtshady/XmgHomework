package com.example.oauth.po;

import lombok.Data;

@Data
public class UserInfo {
    private Integer id;
    private String avatarUrl;
    private String login;
    private String bio;
    private String createdAt;
    private String htmlUrl;
}

package com.example.oauth;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Component
public class GiteeUtil {
    private static final String GITEE_CLIENT_ID = "f681c4d3d6f52702b16c04ebc3624c6a6aa58c154db70cbf9afdd0b628360c96";
    private static final String GITEE_CLIENT_SECRET = "5d8983fe7460dfe4f42dab64c63128798b7d47c0fe54fca892d86e0d568ba8de";

    private static final String REDIRECT_URI = "http://127.0.0.1:8080/gitee/callback";

    @Resource
    RestTemplate restTemplate;

    private String accessToken(String code) {
        return "https://gitee.com/oauth/token?grant_type=authorization_code&code=" + code + "&client_id=" + GITEE_CLIENT_ID + "&redirect_uri=" + REDIRECT_URI + "&client_secret=" + GITEE_CLIENT_SECRET;
    }

    private String userInfo(String accessToken) {
        return "https://gitee.com/api/v5/user?access_token=" + accessToken;
    }

    public String authorizeUri() {
        return "https://gitee.com/oauth/authorize?client_id=" + GITEE_CLIENT_ID + "&response_type=code&redirect_uri=" + REDIRECT_URI;
    }


    public String  getUserInfo(String accessToken) {
        String userInfo = userInfo(accessToken);
        ResponseEntity<String> forEntity = restTemplate.exchange(userInfo, HttpMethod.GET, httpEntity(), String.class);
        String body = forEntity.getBody();
        return body;
    }

    public String getAccessToken(String code) {
        String token = accessToken(code);
        ResponseEntity<Object> entity = restTemplate.postForEntity(token, httpEntity(), Object.class);
        Object body = entity.getBody();
        assert body != null;
        String string = body.toString();
        String[] split = string.split("=");
        String accessToken = split[1].toString().split(",")[0];
        return accessToken;
    }

    public static HttpEntity httpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");

        HttpEntity<HttpHeaders> request = new HttpEntity<>(headers);
        return request;
    }

}

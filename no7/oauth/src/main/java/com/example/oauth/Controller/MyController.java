package com.example.oauth.Controller;

import com.example.oauth.GiteeUtil;
import com.example.oauth.po.UserInfo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class MyController {

    @Resource
    GiteeUtil giteeUtil;

    @GetMapping(value = "/login")
    public String loginHtml() {
        return "login";
    }

    @GetMapping(value = "/oauth/login")
    public String login() {
        return "redirect:" + giteeUtil.authorizeUri();
    }

    @GetMapping(value = "/gitee/callback")
    public String giteeCallBack(HttpServletRequest request, Model model) {
        String code = request.getParameter("code");
        String userInfo = giteeUtil.getUserInfo(giteeUtil.getAccessToken(code));
        UserInfo info = new Gson().fromJson(userInfo, UserInfo.class);
        model.addAttribute("gitee", info);
        return "index";
    }

}

package com.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.domain.User;
import com.example.util.MyUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * <p>描述类的信息</p>
 *
 * <pre>
 * @author wuxiongbo
 * @date 2021/11/26
 * </pre>
 */
@RestController
@RequestMapping("/callback")
@Log4j2
public class CallbackController {

    /**
     * 欢迎页面, 检查后端服务是否启动
     * @see User
     * @return
     */
    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String welcome() {
        return "welcome mongo";
    }



    //    @MyAnnotation(value = "value_demo", name = "name_demo")
    @PostMapping("/oss")
    public String OSSCallback(
            @RequestBody Map<String, Object> body, HttpServletRequest request)
    {

        System.out.println(body);

        String ossCallbackBody = JSONObject.toJSONString(body);
        try {
            MyUtil.verifyOSSCallbackRequest(request,ossCallbackBody);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "{\"Status\":\"OK\"}";
    }




}

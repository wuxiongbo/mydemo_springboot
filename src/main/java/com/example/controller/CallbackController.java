package com.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.util.MyUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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


    /**
     * 127.0.0.1:9292/callback/redirect?redirect=0
     * @param redirect
     * @param httpServletResponse
     * @return
     * @throws IOException
     */
    @GetMapping("/redirect")
    public String redirect(@RequestParam(value = "redirect")Integer redirect,HttpServletResponse httpServletResponse) throws IOException {

        if(redirect==1){
            httpServletResponse.sendRedirect("http://388j3m2771.qicp.vip/callback/redirectUrl?code=23132132&dd=1");
        }
        return "xxxxx";

    }

    @GetMapping("/redirectUrl")
    public String redirectUrl(@RequestParam(value = "code")String code,@RequestParam(value = "dd")String device) {
        return "code:"+code+"; dd:"+device;
    }
}

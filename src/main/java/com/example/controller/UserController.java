package com.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.example.annotation.MyAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

/**
 * <p>swagger2 演示案例</p>
 *
 * @Api注解 用来标记当前Controller的功能。
 * @ApiOperation注解 用来描述一个方法的作用。
 * @ApiImplicitParam注解 用来描述一个参数，可以配置参数的中文含义，也可以给参数设置默认值，这样在接口测试的时候可以避免手动输入。
 *                      如果有多个参数，则需要使用多个@ApiImplicitParam注解来描述，
 *                      多个@ApiImplicitParam注解需要放在一个@ApiImplicitParams注解中。
 *
 * 注意：@ApiImplicitParam注解中虽然可以指定参数是必填的，但是却不能代替@RequestParam(required = true)，
 *      前者的必填只是在Swagger2框架内必填，抛弃了Swagger2，这个限制就没用了，
 *      所以假如开发者需要指定一个参数必填，@RequestParam(required = true)注解还是不能省略。
 *
 * https://blog.csdn.net/u012702547/article/details/88775298
 *
 * <pre>
 * @author wuxiongbo
 * @date 2021/7/7
 * </pre>
 */
@RestController
@RequestMapping("/user")
// 用来标记当前Controller的功能
@Api(tags = "用户管理相关接口")
@Log4j2
public class UserController {

    @PostMapping("/add")
//    用来描述方法。
    @ApiOperation("添加用户的接口")
//    用来描述参数。可以配置参数的中文含义，也可以给参数设置默认值
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", defaultValue = "李四"),
            @ApiImplicitParam(name = "address", value = "用户地址", defaultValue = "深圳", required = true)
    })
//    @MyAnnotation(value = "value_demo", name = "name_demo")
    public String addUser(
            /*@RequestParam("username")
                    String username,

            @RequestParam(value = "address",required = true)
                    String address*/
            @RequestBody Map<String, Object> body,HttpServletRequest request)
    {

        System.out.println(body);

        String ossCallbackBody = JSONObject.toJSONString(body);
        try {
            VerifyOSSCallbackRequest(request,ossCallbackBody);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "{\"Status\":\"OK\"}";
    }

    /**
     * 验证上传回调的Request
     *
     * @param request
     * @param ossCallbackBody    oss 回调请求 应用服务器时，带的 消息体  以及请求头。
     * @return
     * @throws NumberFormatException
     * @throws IOException
     */
    protected boolean VerifyOSSCallbackRequest(HttpServletRequest request, String ossCallbackBody)
            throws NumberFormatException, IOException {
        boolean ret = false;

        // 获取 签名
        String autorizationInput = request.getHeader("Authorization");
        byte[] authorization = BinaryUtil.fromBase64String(autorizationInput); //base64解码后的签名


        // 获取 公钥
        //   1. 获取公钥地址
        String pubKeyInput = request.getHeader("x-oss-pub-key-url");
        byte[] pubKey = BinaryUtil.fromBase64String(pubKeyInput); // 公钥URL ，base64解码
        String pubKeyAddr = new String(pubKey); // 公钥URL
        // 为了确认该public_key是由OSS颁发的，用户需要校验 x-oss-pub-key-url头的值必须以 http://gosspublic.alicdn.com/ 或者 https://gosspublic.alicdn.com/ 开头。
        if (!pubKeyAddr.startsWith("http://gosspublic.alicdn.com/")
                && !pubKeyAddr.startsWith("https://gosspublic.alicdn.com/")) {
            System.out.println("pub key addr must be oss addrss");
            return false;
        }
        //   2. 获取公钥
        String publicKey  = executeGet(pubKeyAddr);
        publicKey  = publicKey.replace("-----BEGIN PUBLIC KEY-----", "");
        publicKey  = publicKey.replace("-----END PUBLIC KEY-----", "");


        //  拼接待签名字符串。  签名字符串 = uri + 请求参数 + 回调消息体
        String authStr = "";  // 签名字符串

        // 1)uri
        String uri = request.getRequestURI();
        String decodeUri = java.net.URLDecoder.decode(uri, "UTF-8");
        authStr += decodeUri;
        // 2)请求参数
        String queryString = request.getQueryString();
        if (queryString != null && !queryString.equals("")) {
            authStr += "?" + queryString;
        }
        // 3)回调消息体
        authStr += "\n" + ossCallbackBody;


        // 校验， 确保回调请求是由OSS发起
        ret = doCheck(authStr, authorization, publicKey );

        return ret;
    }
    /**
     * 验证RSA
     *
     * @param content     内容
     * @param sign        签名
     * @param publicKey   公钥
     * @return
     */
    public static boolean doCheck(String content, byte[] sign, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = BinaryUtil.fromBase64String(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature = java.security.Signature.getInstance("MD5withRSA");
            signature.initVerify(pubKey);
            signature.update(content.getBytes());
            boolean bverify = signature.verify(sign);

            return bverify;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public static String executeGet(String url) {
        BufferedReader in = null;

        String content = null;
        try {
            // 定义HttpClient
            @SuppressWarnings("resource")
            DefaultHttpClient client = new DefaultHttpClient();
            // 实例化HTTP方法
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));
            HttpResponse response = client.execute(request);

            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            // 拼接 公钥字符串
            StringBuffer sb = new StringBuffer("");
            String line = "";
            // 获取系统分隔符
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
            content = sb.toString();

        } catch (Exception e) {
        } finally {
            if (in != null) {
                try {
                    in.close();// 最后要关闭BufferedReader
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return content;
        }
    }

    /**
     * @RequestParam 修饰 get请求的参数
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @ApiOperation("根据id查询用户的接口")
    @ApiImplicitParam(name = "id", value = "用户id", defaultValue = "99", required = true)
    public User getUserById(@PathVariable @RequestParam("id")Integer id, HttpServletResponse response, HttpServletRequest request) {

        User user = new User();
        user.setId(id);
        return user;
    }

    /**
     * @RequestBody  修饰 post请求的参数
     *
     * 如果参数是一个对象，对于参数的描述也可以放在 实体类User 中
     * @param user
     * @return
     */
    @PutMapping("/update/{id}")
    @ApiOperation("根据id更新用户的接口")
    public User updateUserById(@RequestBody User user, @PathVariable Integer id) {
        user.setId(id);
        return user;
    }


    /**
     * 测试 自定义注解 aop
     */
    @MyAnnotation(value = "value_demo", name = "name_demo")
    @RequestMapping(value = "/annotation", method = RequestMethod.GET)
    public String annotationDemo(@RequestParam("id")Integer id,@RequestParam("address")String address) {
        System.out.println("===开始执行 annotationDemo方法===");

        System.out.println("id:"+id);
        System.out.println("address:"+address);

        System.out.println("===结束执行 annotationDemo方法===");
        return "success";
    }

}


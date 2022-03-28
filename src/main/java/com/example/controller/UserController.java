package com.example.controller;

import com.example.annotation.MyAnnotation;
import com.example.domain.User;
import com.example.domain.XAccountInfo;
import com.example.service.XAccountInfoService;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Arrays;
import java.util.List;

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
@Api(tags = "用户管理相关接口")  // swagger： Api 用来标记当前Controller的功能
@Log4j2
public class UserController {

    @Autowired
    XAccountInfoService xAccountInfoService;


//    swagger： ApiOperation 用来描述方法。
    @ApiOperation("添加用户的接口")
//    swagger： ApiResponses 用来描述返回参数。可以配置参数的中文含义，也可以给参数设置默认值
    @ApiResponses({
            @ApiResponse(code = 1, message = "添加成功，"),
            @ApiResponse(code = 2, message = "添加失败"),
            @ApiResponse(code = 3, message = "内部错误"),
            @ApiResponse(code = 4, message = "其他错误")
    })
    @PostMapping("/add")
    public User add(@RequestBody User user) {

        System.out.println(user);

        return user;
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
    public User getUserById(@RequestParam(value = "id",required = false)Integer id,
                            @RequestParam(value = "address",required = false)String address,
                            @RequestParam(value = "username",defaultValue = "123")String username) {

        User user = new User();
        user.setId(id);
        user.setAddress(address);
        user.setUsername(username);
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
    @MyAnnotation(value = "value_demo", name = "name_demo",primaryKey="user.id")
    public User updateUserById(@RequestBody User user, @PathVariable Integer id) {
        user.setId(id);
        return user;
    }


    /**
     * 测试 自定义注解 aop
     */
    //    swagger： ApiImplicitParams 用来描述 方法参数。
    //             name          接收参数名
    //             value         接收参数的意义描述
    //             defaultValue  参数的默认值
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", defaultValue = "0"),
            @ApiImplicitParam(name = "address", value = "用户地址", defaultValue = "深圳", required = true)
    })
    @MyAnnotation(value = "value_demo", name = "name_demo")
    @GetMapping("/annotation")   // @RequestMapping(value = "/annotation", method = RequestMethod.GET)
    public String annotationDemo(@RequestParam("id")Integer id,@RequestParam("address")String address) {
        System.out.println("===开始执行 annotationDemo方法===");

        System.out.println("id:"+id);
        System.out.println("address:"+address);

        System.out.println("===结束执行 annotationDemo方法===");
        return "success";
    }



    @PostMapping("/selectAccount")
    public List<XAccountInfo> selectAccount(@RequestBody XAccountInfo req) {
        System.out.println(req);
        return xAccountInfoService.selectByCondition(req);
    }


    @PostMapping("/urlDemo")
    public String urldemo(HttpServletRequest request) {
        PrintStream out = System.out;

        String getContextPath = request.getContextPath();
        String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+getContextPath+"/";
        String getRemoteAddress=request.getRemoteAddr();

        String getServletPath =request.getServletPath();
        String getRequestURI =request.getRequestURI();

        String getQueryString =request.getQueryString();
        String getRemoteUser =request.getRemoteUser();
        out.println("getContextPath:"+ getContextPath +"<br>");
        out.println("basePath:"+basePath+"<br>");
        out.println("getRemoteAddress:"+ getRemoteAddress +"<br>");
        out.println("getServletPath:"+ getServletPath +"<br>");
        out.println("getRequestURI:"+ getRequestURI +"<br>");
        out.println("getQueryString:"+ getQueryString +"<br>");
        out.println("getRemoteUser:"+ getRemoteUser +"<br>");
        return null;
    }

    // 127.0.0.1:9292/user/redirectDemo?data=-26,-75,-117,-24,-81,-107,49
    @GetMapping("/redirectDemo")
    public String redirectDemo(byte[] data) {
        System.out.println(Arrays.toString(data));

        String s = new String(data);
        System.out.println(s);

        byte a = Byte.MAX_VALUE;
        byte b = Byte.MIN_VALUE;
        int size = Byte.SIZE;


        String binaryString = Integer.toBinaryString(24);
        System.out.println(binaryString);

        // 先,把它变为字符数组
        char[] strToChar = s.toCharArray();
        // 然后,通过 Integer 中的 toBinaryString 方法来一个一个转
        for (char c : strToChar) {
            String binStr = Integer.toBinaryString(c);
            System.out.print(binStr);
        }

        return "success";
    }



    @PostMapping("/postDemo")
    @ApiOperation("测试")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "String", name = "name", value = "用户名称", required = true)
    })
    public String postDemo(@RequestParam("name")String name) {
        System.out.println(name);
        return "success";
    }

}


package com.example.controller;

import com.example.annotation.MyAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

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
    public String addUser(
            @RequestParam("username")
                    String username,

            @RequestParam(value = "address",required = true)
                    String address)
    {
        return "success";
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
    public User getUserById(@PathVariable @RequestParam("id")Integer id) {
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


package com.example.requestUtil;

/**
 * <p>描述类的信息</p>
 *
 * <pre>
 * @author wuxiongbo
 * @date 2021/8/16
 * </pre>
 */
import com.google.gson.Gson;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author www.exception.site (exception 教程网)
 * @date 2019/2/12
 * @time 14:03
 * @discription
 **/
//@Aspect
//@Component
public class WebLogAspect {

    private final static Logger logger = LoggerFactory.getLogger(WebLogAspect.class);

    /** 以 controller 包下定义的所有请求为切入点 */
//    @Pointcut("execution(public * com.example.controller..*.*(..))")
    public void webLog() {}

    /**
     * 在切点之前织入
     * @param joinPoint
     * @throws Throwable
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 打印请求相关参数
        logger.info("========================================== Start ==========================================");
        // 打印请求 url
        logger.info("URL            : {}", request.getRequestURL().toString());
        // 打印 Http method
        logger.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        logger.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        // 打印请求的 IP
        logger.info("IP             : {}", request.getRemoteAddr());
        // 打印请求入参
//        logger.info("Request Args   : {}", new Gson().toJson(joinPoint.getArgs()));
        String username = request.getParameter("username");
        logger.info("username             : {}", username);
        String address = request.getParameter("address");
        logger.info("address             : {}", address);

        // 获取Post请求的消息体
//        String ossCallbackBody = GetPostBody(request.getInputStream(), Integer.parseInt(request.getHeader("content-length")));
//        logger.info("ossCallbackBody             : {}", ossCallbackBody);


        /*// 获取 签名
        String autorizationInput = request.getHeader("Authorization");
        byte[] authorization = BinaryUtil.fromBase64String(autorizationInput); //base64解码后的签名
        logger.info("authorization             : {}", autorizationInput);
        // 获取 公钥
        //   1. 获取公钥地址
        String pubKeyInput = request.getHeader("x-oss-pub-key-url");
        byte[] pubKey = BinaryUtil.fromBase64String(pubKeyInput); // 公钥URL ，base64解码
        String pubKeyAddr = new String(pubKey); // 公钥URL
        logger.info("pubKeyAddr             : {}", pubKeyAddr);*/
    }


    /**
     * 获取Post消息体
     *
     * @param is
     * @param contentLen
     * @return
     */
//    public String GetPostBody(InputStream is, int contentLen) {
//        if (contentLen > 0) {
//            int readLen = 0;
//            int readLengthThisTime = 0;
//            byte[] message = new byte[contentLen];
//            try {
//                while (readLen != contentLen) {
//                    readLengthThisTime = is.read(message, readLen, contentLen - readLen);
//
//                    if (readLengthThisTime == -1) {// Should not happen.
//                        break;
//                    }
//
//                    readLen += readLengthThisTime;
//                }
//
//                return new String(message);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return "";
//    }

    /**
     * 在切点之后织入
     * @throws Throwable
     */
    @After("webLog()")
    public void doAfter() throws Throwable {
        logger.info("=========================================== End ===========================================");
        // 每个请求之间空一行
        logger.info("");
    }

    /**
     * 环绕
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        // 打印出参
        logger.info("Response Args  : {}", new Gson().toJson(result));
        // 执行耗时
        logger.info("Time-Consuming : {} ms", System.currentTimeMillis() - startTime);
        return result;
    }

}


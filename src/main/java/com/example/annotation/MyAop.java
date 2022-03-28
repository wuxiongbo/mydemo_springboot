package com.example.annotation;

import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.asm.ClassReader;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> 解析@MyAnnotation注解  使用 </p>
 *
 * <pre>
 * @author wuxiongbo
 * @date 2021/7/21
 * </pre>
 * com.example.annotation.MyAop
 *
 * aop+自定义注解
 * https://www.cnblogs.com/chenglc/p/9642891.html
 *
 */
@Aspect //来定义一个切面
@Component
public class MyAop {

    /**
     * 定义切入点
     *
     * 两种写法。
     *
     * */
//    @Pointcut("( execution(* com.example..*(..)) ) && @annotation(com.example.annotation.MyAnnotation)")
    @Pointcut("@annotation(com.example.annotation.MyAnnotation)")
    public void auditAspect() {
        System.out.println("=====Pointcut=====");
    }

    /**
     * 前置通知
     *
     * @param joinPoint 切点
     */
    @Before("auditAspect()")
    public void doBefore(JoinPoint joinPoint) {
        System.out.println("==开始== @Before(\"auditAspect()\")");

        try {
            AopUtils.getControllerMethodDescription(joinPoint);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("==结束== @Before(\"auditAspect()\")");
    }

    /**
     * 后置通知
     *
     * @param joinPoint 切点
     */
    @AfterReturning("auditAspect()")
    public void doAfterReturning(JoinPoint joinPoint) {
        System.out.println("==开始== @AfterReturning(\"auditAspect()\")");

        try {
            AopUtils.getControllerMethodDescription(joinPoint);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("==结束== @AfterReturning(\"auditAspect()\")");
    }


    @Around("auditAspect()")
    public Object around (ProceedingJoinPoint joinPoint) {
        Object result = null;

        try {

            System.out.println("开始触发 @Around(\"auditAspect()\")，  before  proceed()");
            System.out.println("======================@Around开始===========================");

            result = joinPoint.proceed();

            System.out.println("======================@Around结束===========================");
            System.out.println("结束触发 @Around(\"auditAspect()\")，   after  proceed() ");

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        System.out.println("result：" + result);
        return result;
    }
/*
       // 伪代码。 @around 包含了， @Before、@After、@AfterRunning、@AfterThrowing 这四个全部通知。

        try { // 环绕增强 @around  放行。  proceed()开始。


            try {//前置增强  @Before
                System.out.println(target.getClass().getName()+": The"+methodName+"method begins");

                // 方法本体。

            } finally {//后置增强  @After
                System.out.println(target.getClass().getName()+"：The "+methodName+" method ends.");
            }

            //返回增强  @AfterRunning
            System.out.println(target.getClass().getName()+"：Result of the "+methodName+" method："+result);

        } catch (Throwable e) {//异常增强  @AfterThrowing
            System.out.println(target.getClass().getName()+"：Exception of the method "+methodName+": "+e);



        } // 环绕增强 @around  放行。  proceed()结束。 可获取 被增强方法本体执行的结果 result

*/




}

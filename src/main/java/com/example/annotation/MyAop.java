package com.example.annotation;

import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * <p> 解析@MyAnnotation注解  使用 </p>
 *
 * <pre>
 * @author wuxiongbo
 * @date 2021/7/21
 * </pre>
 * com.example.annotation.MyAop
 * https://www.cnblogs.com/chenglc/p/9642891.html
 *
 */
@Aspect //来定义一个切面
@Component
public class MyAop {

    /**
     * 定义切入点
     * */
//    @Pointcut("( execution(* com.example..*(..)) ) && @annotation(com.example.annotation.MyAnnotation)")
    @Pointcut("@annotation(com.example.annotation.MyAnnotation)")
    public void auditAspect() {
        System.out.println("1221212132");
    }

    /**
     * 前置通知
     *
     * @param joinPoint 切点
     */
    @Before("auditAspect()")
    public void doBefore(JoinPoint joinPoint) {
        Object target = joinPoint.getTarget();
        String methodName = joinPoint.getSignature().getName();

        System.out.println("方法执行前，触发 @Before(\"auditAspect()\")");
    }

    /**
     * 后置通知
     *
     * @param joinPoint 切点
     */
    @AfterReturning("auditAspect()")
    public void doAfterReturning(JoinPoint joinPoint) {

        System.out.println("方法执行后，触发 @AfterReturning(\"auditAspect()\")");

        Object[] args = joinPoint.getArgs();
        System.out.println("参数个数:"+args.length);

        try {
            getControllerMethodDescription(joinPoint);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Around("auditAspect()")
    public Object around (ProceedingJoinPoint joinPoint) {
        Object result = null;
        Object target = joinPoint.getTarget();
        String methodName = joinPoint.getSignature().getName();

        try {

            System.out.println("触发 @Around(\"auditAspect()\")，before  proceed()");
            System.out.println("=================================================");

            result = joinPoint.proceed();

            System.out.println("=================================================");
            System.out.println("触发 @Around(\"auditAspect()\")，after  proceed() ");

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



        } // 环绕增强 @around  放行。  proceed()结束。 可获取 result

*/


    /**
     * 获取注解中对方法的描述信息
     *     全部信息均从 JoinPoint 中获取
     * @param joinPoint 切点
     * @return 方法描述
     */
    public static void getControllerMethodDescription(JoinPoint joinPoint)
            throws ClassNotFoundException {

        //获得执行方法所属的类实例
        Object target = joinPoint.getTarget();
           //获得执行方法的类名（全限定名）
        Class<?> targetClass = target.getClass();
           // class to className
        String className = targetClass.getName();
           // className to class
        Class<?> targetClass1 = Class.forName(className);

        System.out.println("类名：" + className);


        //返回当前连接点签名
        Signature signature = joinPoint.getSignature();

           //获得执行方法的方法名方式二（短名）：先获取 Method ，再通过 Method获取 methodName
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }



        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        String targetMethodName = targetMethod.getName();
        System.out.println("MethodSignature 获取的方法名："+targetMethodName);
            // 获取方法的  入参  类型
        Class[] parameterTypes = targetMethod.getParameterTypes();
            // 获取方法的  返回值  类型
        Class<?> returnType = targetMethod.getReturnType();

           //获得执行方法的方法名方式一（短名）：直接通过 Signature 签名获取 methodName
        targetMethodName = signature.getName();
        System.out.println("Signature 获取的方法名："+targetMethodName);



        // 获取 参数名。 用spring的方式
        ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(targetMethod);
        // 获取 参数值。 通过切点获取
        Object[] arguments = joinPoint.getArgs();

        for (int i = 0; i < arguments.length; i++) {
            String parameterName = parameterNames[i];
            System.out.println("参数名"+ (i+1) +"："+parameterName);

            Object argument = arguments[i];
            System.out.println("参数的值或实例"+ (i+1) +"："+argument);

            Class<?> clazz = arguments[i].getClass();
            System.out.println("参数的值类型"+ (i+1) +"：" + clazz);
        }

        // 获取注解
        MyAnnotation annotation;
        String annotationName;
        try {
            // 获取注解，方式一：获取执行方法的全部值类型 以及 执行方法名。然后，用反射， 用目标类对象直接获取执行方法。
            Method targetMethod1 = targetClass.getMethod(targetMethodName, parameterTypes);
            annotation = targetMethod1.getAnnotation(MyAnnotation.class);
            //  问题： 通过签名获取的 targetMethod ，与 通过反射获取的 targetMethod1 有什么区别呢？
            //  targetMethod 是接口的方法
            //  targetMethod1 是具体的实现类的方法。
            //  具体原因不清楚。


            //还可以获取方法参数上的注解。
            // https://www.cnblogs.com/kelelipeng/p/12009680.html
            //获取方法的所有参数注解。
            Annotation[][] parameterAnnotations = targetMethod1.getParameterAnnotations();
            for (Annotation[] parameterAnnotation: parameterAnnotations) {
                int paramIndex= ArrayUtils.indexOf(parameterAnnotations, parameterAnnotation);
                System.out.println("参数的值："+arguments[paramIndex]);
                for (Annotation annotation1 : parameterAnnotation) {
                    if (annotation1 instanceof RequestParam){
                        String value = ((RequestParam) annotation1).value();
                        System.out.println("参数上注解的值："+value);
                    }
                }
            }


            // 打印后可以看出，获取的annotation 实际上是 Clc 的代理类。
            annotationName = annotation.getClass().getName();
            System.out.println("方式一获取的 注解名"+annotationName);


            // 获取注解，方式二：  利用反射，获取目标类的所有公共方法。 不包括私有的
            Method[] methods = targetClass.getMethods();
            for (Method m : methods) {
                // 获取 名称为 annotationDemo 的方法
                if (m.getName().equals(targetMethodName)) {
                    // 对比 annotationDemo方法 中参数的个数。(因为可能方法重载，导致相同方法名存在多个方法)
                    if (parameterTypes.length == arguments.length) {
                        // 获取注解
                        annotation = m.getAnnotation(MyAnnotation.class);

                        // 打印后可以看出，获取的annotation 实际上是 Clc 的代理类。
                        annotationName = annotation.getClass().getName();
                        System.out.println("方式二获取的 注解名"+annotationName);

                        break;

                    }
                }
            }

            // 获取方法注解 各个属性的值
            String  value = annotation.value();
            String  name = annotation.name();

            System.out.println("方法"+targetMethodName+"上，注解"+annotationName+"的属性 value=" + value);
            System.out.println("方法"+targetMethodName+"上，注解"+annotationName+"的属性 name=" + name);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 本示例的切点为  annotationDemo 方法。
     *   方法全名： com.example.controller.UserController#annotationDemo
     *   方法参数：(Integer id,String address)
     *
     *   parameterNames 存的是 参数名，即 "id"、"address"
     *   argValues 存的是 参数的值或实例，即 11111、"中国"
     *
     */
    // 参数名 列表
    private List<String> parameterNames;
    // 参数值 列表
    private Object[] argValues;

    private Object resolveFromObject() {
        String key = "userInfo.id";
        String[] split = key.split("\\.");
        // 参数1   类名  UserInfo
        String className = split[0];
        // 参数2   属性名  id
        String propertyName = split[1];

        // 参数1 UserInfo 的实例
        int classNameIndex = parameterNames.indexOf(className);
        Object classInstance= argValues[classNameIndex];
        // 参数1 UserInfo 的类型  com.example.annotation.UserInfo
        Class<?> clazz = classInstance.getClass();

        try {
            // 获取 实例 userInfo 属性id  的值
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(propertyName, clazz);
            Method readMethod = propertyDescriptor.getReadMethod();
            Object propertyValue = readMethod.invoke(classInstance);

            return propertyValue;


        } catch (Exception e) {
            System.out.println("获取参数失败：===>"+ e.toString());
            return argValues[0];
        }

    }

}

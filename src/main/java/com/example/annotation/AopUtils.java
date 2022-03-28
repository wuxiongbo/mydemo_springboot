package com.example.annotation;

import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.web.bind.annotation.RequestParam;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>描述类的信息</p>
 *
 * <pre>
 * @author wuxiongbo
 * @date 2022/3/28
 * </pre>
 */
public class AopUtils {
    /**
     * 获取注解中，对方法的描述信息   （全部信息  均从 JoinPoint 中获取）
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


        //获得执行方法的方法名方式一（短名）：直接 通过 Signature 签名，获取 methodName
        String targetMethodName1 = signature.getName();
        System.out.println("Signature 获取的方法名："+targetMethodName1);


        //获得执行方法的方法名方式二（短名）：先 通过 Signature 签名 ，获取 Method ；再 通过 Method，获取 methodName
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();

        // 获取方法的  方法名
        String targetMethodName = targetMethod.getName();
        System.out.println("MethodSignature 获取的方法名："+targetMethodName);

        // 获取方法的  入参  类型
        Class[] parameterTypes = targetMethod.getParameterTypes();
        // 获取方法的  返回值  类型
        Class<?> returnType = targetMethod.getReturnType();


        // 获取方法的 参数名 列表。 用spring的方式获取
        ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(targetMethod);

        // 获取方法的 参数值 列表。 通过切点获取
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
        try {
            // 获取注解，方式一：通过 切点方法的 入参类型 以及 方法名，匹配方法签名。然后，通过反射技术，直接用目标类对象获取切点方法。
            Method targetMethod1 = targetClass.getMethod(targetMethodName, parameterTypes);
            MyAnnotation annotation = targetMethod1.getAnnotation(MyAnnotation.class);
            //  问题： 通过签名获取的 targetMethod ，与 通过反射获取的 targetMethod1 有什么区别呢？
            //  targetMethod 是接口的方法
            //  targetMethod1 是具体的实现类的方法。
            //  具体原因不清楚。


            //还可以获取切点方法中，参数上的注解。
            // https://www.cnblogs.com/kelelipeng/p/12009680.html
            //获取方法的 参数上的注解。
            Annotation[][] parameterAnnotations = targetMethod1.getParameterAnnotations();
            for (Annotation[] parameterAnnotation: parameterAnnotations) {
                int paramIndex= ArrayUtils.indexOf(parameterAnnotations, parameterAnnotation);

                System.out.println("参数的值："+arguments[paramIndex]);

                for (Annotation annotationOnParameter : parameterAnnotation) {
                    if (annotationOnParameter instanceof RequestParam){
                        String value = ((RequestParam) annotationOnParameter).value();
                        System.out.println("参数上 @RequestParam注解的值："+value);
                    }
                }
            }


            // 打印后可以看出，获取的annotation 实际上是 Clc 的代理类。
            String annotationName = annotation.getClass().getName();
            System.out.println("方式一获取的 注解名"+annotationName);


            // 获取注解，方式二：  利用反射，获取目标类的所有公共方法（不包括私有的）
            Method[] methods = targetClass.getMethods();
            // 遍历目标类的所有公共方法。
            for (Method m : methods) {
                // 找到 方法名 与  参数的个数 都 匹配 的。(因为可能方法重载，导致相同方法名，多个方法参数)
                if (m.getName().equals(targetMethodName) && parameterTypes.length == arguments.length) {
                    // 获取注解
                    annotation = m.getAnnotation(MyAnnotation.class);

                    // 打印后可以看出，获取的annotation 实际上是 Clc 的代理类。
                    annotationName = annotation.getClass().getName();
                    System.out.println("方式二获取的 注解名"+annotationName);

                    break;
                }
            }


            assert parameterNames != null;
            Resolver resolver = new Resolver(Arrays.asList(parameterNames), arguments);
            resolver.resolveFromObject();


            // 获取方法注解 各个属性的值
            String  value = annotation.value();
            String  name = annotation.name();

            System.out.println("方法"+targetMethodName+"上，注解"+annotationName+"的属性 value=" + value);
            System.out.println("方法"+targetMethodName+"上，注解"+annotationName+"的属性 name=" + name);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static class Resolver{
        /**
         * 本示例的切点为  annotationDemo 方法。
         *   方法全名： com.example.controller.UserController#annotationDemo
         *   方法参数：(Integer id,String address)
         *
         *   parameterNames  存 参数名。       即 "id"、"address"
         *   argValues       存 参数的 值/实例。即 11111、"中国"
         *
         */
        // 参数名 列表。   可以用spring获取
        private List<String> parameterNames;
        // 参数值 列表。   可以通过切点获取
        private Object[] argValues;


        public Resolver(List<String> parameterNames, Object[] argValues) {
            this.parameterNames = parameterNames;
            this.argValues = argValues;
        }

        /**
         *
         * @param key   eg: String key = "user.id";
         * @return
         */
        private Object resolveFromObject(String key) {
            // =====解析=========

            String[] split = key.split("\\.");
            // 参数1   类名  UserInfo
            String className = split[0];
            // 参数2   属性名  id
            String propertyName = split[1];


            // 参数1 UserInfo 的
            // 1）参数 实例
            int classNameIndex = parameterNames.indexOf(className);
            Object classInstance= argValues[classNameIndex];
            // 2）参数 类型  com.example.annotation.UserInfo
            Class<?> clazz = classInstance.getClass();

            try {
                // 3）获取 参数  user 实例 的 id 属性值
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


}

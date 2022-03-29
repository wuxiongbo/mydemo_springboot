package com.example.annotation;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.web.bind.annotation.RequestParam;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
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

        // 获得执行方法所属的类实例
        Object target = joinPoint.getTarget();
        // Object to class
        Class<?> targetClass = target.getClass();

        printTargetClass(targetClass);

        // 0)获取 切点方法
        Signature signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        // 获取 切点方法的
        // 1)方法名
        String targetMethodName = targetMethod.getName();
        // 2)方法 入参的 的类型
        Class<?>[] parameterTypes = targetMethod.getParameterTypes();
        // 3)方法 返回值 的类型
        Class<?> returnType = targetMethod.getReturnType();
        // 4)方法 参数名 列表。 用spring的方式获取
        ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(targetMethod);
        // 5)方法 参数值 列表。 通过切点获取
        Object[] arguments = joinPoint.getArgs();

        assert parameterNames != null;
        
        //打印参数
        printParameter(parameterNames,arguments);

        // 获取注解
        try {
            // 获取注解：
            Method targetMethod1 = targetClass.getMethod(targetMethodName, parameterTypes);
            MyAnnotation annotation = targetMethod1.getAnnotation(MyAnnotation.class);
            //  问题： 通过签名获取的 targetMethod 与 通过反射获取的 targetMethod1 有什么区别呢？
            //  答： targetMethod 是接口的方法
            //      targetMethod1 是具体的实现类的方法。
            //     具体原因不清楚。

            //读取 切点方法 参数上的注解。
            printParameterAnnotations(targetMethod1,parameterNames);

            if(StringUtils.isNotBlank(annotation.primaryKey())){
                Resolver resolver = new Resolver(Arrays.asList(parameterNames), arguments);
                Object propertyValue = resolver.resolveFromObject(annotation.primaryKey());
                System.out.println("primaryKey: " + propertyValue);
            }

            printAnnotation(annotation,targetMethodName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printTargetClass(Class<?> targetClass) throws ClassNotFoundException {
        // class to className
        String className = targetClass.getName();
        // className to class
        targetClass = Class.forName(className);
        // 获取目标类的所有公共方法（不包括私有的）
        Method[] methods = targetClass.getMethods();
        System.out.println("切点所在的类名：" + className);
    }

    private static void printAnnotation(MyAnnotation annotation,String targetMethodName){
        // 打印后可以看出，获取的annotation 实际上是 Clc 的代理类。
        String annotationName = annotation.getClass().getName();
        System.out.println("获取的 注解的类名："+annotationName);
        // 获取方法注解 各个属性的值
        String  value = annotation.value();
        String  name  = annotation.name();
        String  primaryKey = annotation.primaryKey();
        System.out.println("方法"+targetMethodName+"上，注解"+annotationName+"的属性 value 的值为：" + value);
        System.out.println("方法"+targetMethodName+"上，注解"+annotationName+"的属性 name 的值为：" + name);
        System.out.println("方法"+targetMethodName+"上，注解"+annotationName+"的属性 primaryKey 的值为：" + primaryKey);
    }


    private static void printParameterAnnotations(Method targetMethod,String[] parameterNames){
        // https://www.cnblogs.com/kelelipeng/p/12009680.html
        //获取方法的 参数上的注解。
        Annotation[][] parameterAnnotations = targetMethod.getParameterAnnotations();

        // 遍历参数索引
        for (Annotation[] parameterAnnotation: parameterAnnotations) {

            // 参数索引位置。
            int paramIndex= ArrayUtils.indexOf(parameterAnnotations, parameterAnnotation);

            // 遍历当前参数上的全部注解。
            for (Annotation annotationOnParameter : parameterAnnotation) {
                if (annotationOnParameter instanceof RequestParam){
                    String value = ((RequestParam) annotationOnParameter).value();
                    System.out.println("参数"+parameterNames[paramIndex]+"上, @RequestParam 注解的值："+value);
                }
            }
        }
    }

    private static void printParameter(String[] parameterNames,Object[] arguments){
        for (int i = 0; i < arguments.length; i++) {
            String parameterName = parameterNames[i];
            System.out.println("参数"+ (i+1) +"的名称："+parameterName);

            Object argument = arguments[i];
            System.out.println("参数"+ (i+1) +"的值/实例："+argument);

            Class<?> clazz = arguments[i].getClass();
            System.out.println("参数"+ (i+1) +"的值类型：" + clazz);
        }
    }


    public static class Resolver{
        /**
         * 本示例的切点为  annotationDemo 方法。
         *   方法全名： {@link com.example.controller.UserController#annotationDemo}
         *   方法参数：(Integer id,String address)
         *
         *   parameterNames  存 参数名。       即 "id"、"address"
         *   argValues       存 参数的 值/实例。即 11111、"中国"
         *
         */
        // 参数名 列表。   可以用spring获取
        private final List<String> parameterNames;
        // 参数值 列表。   可以通过切点获取
        private final Object[] argValues;


        public Resolver(List<String> parameterNames, Object[] argValues) {
            this.parameterNames = parameterNames;
            this.argValues = argValues;
        }

        /**
         *
         * @param primaryKey   eg: String primaryKey = "user.id";
         * @return
         */
        private Object resolveFromObject(String primaryKey) {
            // =====解析=========

            String[] split = primaryKey.split("\\.");
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
                // 3）读取  切点方法参数，即 user实例 id属性 的 值
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(propertyName, clazz);
                Method readMethod = propertyDescriptor.getReadMethod();

                return readMethod.invoke(classInstance);

            } catch (Exception e) {
                System.out.println("获取参数失败：===>"+ e);
                return argValues[0];
            }

        }
    }


}

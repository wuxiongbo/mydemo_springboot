package com.example.cycle_demo.com.tuling.circulardependencies;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
public class MainStart {

    private static Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    /**
     * 读取bean定义，当然在spring中肯定是根据配置 动态扫描注册
     */
    public static void loadBeanDefinitions() {
        RootBeanDefinition aBeanDefinition=new RootBeanDefinition(InstanceA.class);
        RootBeanDefinition bBeanDefinition=new RootBeanDefinition(InstanceB.class);
        beanDefinitionMap.put("instanceA",aBeanDefinition);
        beanDefinitionMap.put("instanceB",bBeanDefinition);
    }


    // 一级缓存 单例池   成熟态Bean
    private static Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    // 二级缓存   纯净态Bean (存储不完整的Bean用于解决循环依赖中多线程读取一级缓存的脏数据)
    // 所以当有了三级缓存后，它还一定要存在，  因为它要存储的 aop创建的动态代理对象,  不可能重复创建
    private static Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>(256);

    // 三级缓存
    private static Map<String, ObjectFactory<?>> factoryEarlySingletonObjects = new ConcurrentHashMap<>(256);


    // 标识当前是不是循环依赖   如果正在创建并且从一级缓存中没有拿到是不是说明是依赖
    private static Set<String> singletonsCurrentlyInCreation =
            Collections.newSetFromMap(new ConcurrentHashMap<>(16));

    /**
     * 创建Bean
     * @param beanName
     * @return
     */
    private static Object getBean(String beanName) throws Exception {

        Object singleton = getSingleton(beanName);
        if(singleton!=null){
            return singleton;
        }

        // 正在创建
        if(!singletonsCurrentlyInCreation.contains(beanName)){
            singletonsCurrentlyInCreation.add(beanName);
        }

        // 开始创建Bean

        singletonsCurrentlyInCreation.add(beanName);

        // 1.实例化
        RootBeanDefinition beanDefinition = (RootBeanDefinition) beanDefinitionMap.get(beanName);
        Class<?> beanClass = beanDefinition.getBeanClass();
        Object instanceBean = beanClass.newInstance();  // 通过无参构造函数


        //  只是循环依赖才创建动态代理？   //创建动态代理

        // Spring 为了解决 aop下面循环依赖会在这个地方创建动态代理 Proxy.newProxyInstance
        // Spring 是不会将aop的代码跟ioc写在一起
        // 不能直接将Proxy存入二级缓存中
        // 是不是所有的Bean都存在循环依赖  当存在循环依赖才去调用aop的后置处理器创建动态代理

        // 存入二级缓存
        earlySingletonObjects.put(beanName,instanceBean);


        // 创建动态代理  （耦合 、BeanPostProcessor)    Spring还是希望正常的Bean 还是再初始化后创建
        // 只在循环依赖的情况下在实例化后创建proxy   判断当前是不是循环依赖
        factoryEarlySingletonObjects.put(beanName,
                () -> new JdkProxyBeanPostProcessor().getEarlyBeanReference(earlySingletonObjects.get(beanName),beanName)
        );


        // 2.属性赋值 解析Autowired
        // 拿到所有的属性名
        Field[] declaredFields = beanClass.getDeclaredFields();


        // 循环所有属性
        for (Field declaredField : declaredFields) {
            // 从属性上拿到@Autowired
            Autowired annotation = declaredField.getAnnotation(Autowired.class);

            // 说明属性上面有@Autowired
            if(annotation!=null){
                declaredField.setAccessible(true);
                // byname  bytype  byconstrator
                // instanceB
                String name = declaredField.getName();
                Object fileObject= getBean(name);   //拿到B得Bean
                declaredField.set(instanceBean,fileObject);
            }

        }

        // 初始化   init-mthod
        // 放在这里创建已经完了  B里面的A 不是proxy
        // 正常情况下会再 初始化之后创建proxy


        // 由于递归完后A 还是原实例，， 所以要从二级缓存中拿到proxy 。
        if(earlySingletonObjects.containsKey(beanName)){
            instanceBean = earlySingletonObjects.get(beanName);
        }

        // 添加到一级缓存   A
        singletonObjects.put(beanName,instanceBean);


        // remove 二级缓存和三级缓存

        return instanceBean;
    }

    private  static Object getSingleton(String beanName){

        Object bean = singletonObjects.get(beanName);

        // 如果一级缓存没有拿到  是不是就说明当前是循环依赖创建
        if(bean==null && singletonsCurrentlyInCreation.contains(beanName)){

            // 如果二级缓存没有就从三级缓存中拿
            bean=earlySingletonObjects.get(beanName);
            if(bean==null){

                // 从三级缓存中拿
                ObjectFactory<?> factory = factoryEarlySingletonObjects.get(beanName);
                if (factory != null) {
                    bean=factory.getObject(); // 拿到动态代理
                    earlySingletonObjects.put(beanName, bean);
                }

            }

        }
        return bean;
    }

    public static void main(String[] args) throws Exception {

        // 加载了BeanDefinition
        loadBeanDefinitions();
        // 注册Bean的后置处理器

        // 循环创建Bean
        for (String key : beanDefinitionMap.keySet()){
            // 先创建A
            getBean(key);
        }

        InstanceA instanceA = (InstanceA) getBean("instanceA");
        instanceA.say();
    }

}

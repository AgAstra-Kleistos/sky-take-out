package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 自定义切面，实现公共字段自动填充处理逻辑
 */
@Slf4j
@Component
@Aspect
public class AutoFillAspect {

    /**
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    //execution:指定包的的类的方法 && @annotation注解限制：需要添加annocation这个包下定义的AutoFill注解
    public void autoFillPointCut(){}

    /**
     * 前置通知
     */

    @Before("autoFillPointCut()")
    //@Before:前置通知； autoFillPointCut:指定切入点
    public void autoFill(JoinPoint joinPoint){
        //Joinpoint: 封装了连接点方法调用的详细信息

        log.info("开始进行公共字段填充....");

        //获取当前被拦截的方法上的数据库操作类型
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();  //方法签名
        //signature调用getMethod()方法获取，再调用getAnnotation()方法，通过参数获取注解
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);  //获取方法上的注解对象
        OperationType operationType = autoFill.value(); //获取数据库操作类型

        //获取当前被拦截的方法的参数--实体对象
        Object[] args = joinPoint.getArgs(); //获取所有的参数，约定：实体对象置于第一个参数
        if(args == null || args.length == 0){
            //空指针判断

            return;
        }

        Object entity = args[0];

        //只处理实体对象，跳过 List/集合
        if (entity instanceof List || entity.getClass().isArray()) {
            return;
        }

        //准备赋值数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        //根据操作类型通过反射进行赋值
        if(operationType == OperationType.INSERT){
            try{
                /*Method setCreateTime = entity.getClass().getDeclaredMethod("setCreateTime", LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod("setCreateUser", Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);*/

                //对于字符串，尽可能设置为固定值，避免错误的同时便于统一管理
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME , LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //通过反射为对象赋值
                setCreateTime.invoke(entity,now);
                setUpdateTime.invoke(entity,now);
                setCreateUser.invoke(entity,currentId);
                setUpdateUser.invoke(entity,currentId);
            }catch(Exception e){
                e.printStackTrace();
            }

        }else if(operationType == OperationType.UPDATE){

            try{
                /*Method setUpdateTime = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);*/

                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME , LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);


                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);

            }catch(Exception e){
                e.printStackTrace();
            }


        }
    }
}

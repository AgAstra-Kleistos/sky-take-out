package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于标识某个方法需要进行公共字段自动填充处理
 */
@Target(ElementType.METHOD)  //指定注解添加位置【ElementType: 添加类型；此处method表示该注解只能加在方法上】
@Retention(RetentionPolicy.RUNTIME)  //指定注解保留时间【RetentionPolicy: 保留时间；此处RUNTIME表示该注解在运行时保留】
public @interface AutoFill {

    //指定数据库操作类型
    OperationType value();


}

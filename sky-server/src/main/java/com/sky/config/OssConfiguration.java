package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类，用于创建AliOssUtil对象
 */
@Slf4j
@Configuration  // 表示当前类是一个配置类
public class OssConfiguration {

    @Bean
    @ConditionalOnMissingBean  // 表示当前方法生成的对象，如果容器中已经存在这个对象，则不创建
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {

        log.info("开始创建aliyun文件上传工具类对象");
        return new AliOssUtil(aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName());


    }
}

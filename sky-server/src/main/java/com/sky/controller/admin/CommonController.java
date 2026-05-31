package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * 通用接口
 */
@Slf4j
@Api(tags = "通用接口")
@RequestMapping("/admin/common")
@RestController
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    @Operation(description = "文件上传")
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){

        log.info("文件上传：{}",file.getOriginalFilename());

        try {

            //原始文件名
            String originalFilename = file.getOriginalFilename();

            /*//防止文件名为空，增加判断
            if (originalFilename == null || !originalFilename.contains(".")) {
                return Result.error("文件格式错误");
            }*/

            //截取原始文件名的后缀
            String extension =originalFilename.substring(originalFilename.lastIndexOf("."));
            //String extension = aliOssUtil.upload(file.getBytes(),null);

            //构造新的文件名：随机字符串 + 文件后缀
            String objectName =  UUID.randomUUID().toString() + extension;

            //文件请求路径
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(filePath);

        } catch (IOException e) {
            log.error("文件上传失败：{}", e);
            //throw new RuntimeException(e);
        }

        return Result.error(MessageConstant.UPLOAD_FAILED);

    }

}

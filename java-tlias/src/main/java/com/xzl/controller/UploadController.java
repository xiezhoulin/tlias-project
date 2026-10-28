package com.xzl.controller;

import com.xzl.pojo.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/** 文件上传本地*/
@RestController
public class UploadController {

    @Value("${app.image-upload}")
    private String uploadDir;

    @Value("${app.image-path}")
    private String imageUrl;


    @PostMapping("/upload")
    private Result upload(MultipartFile file) throws IOException {

        if(!file.isEmpty()){
            String originalFilename = file.getOriginalFilename(); //拿到文件的原始名称
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".")); //拿到的是 .后缀
            String uniqueFileName = UUID.randomUUID().toString().replace("-", "") + extName;// 重新生成文件的唯一名称

            // 拼接完整的文件路径
            File targetFile = new File(uploadDir + uniqueFileName);

            // 如果目标目录不存在，则创建它
            if (!targetFile.getParentFile().exists()){
                targetFile.getParentFile().mkdirs();
            }
            //保存文件
            file.transferTo(targetFile);
            return Result.success(imageUrl + uniqueFileName);
        }

        return Result.success();
    }

}

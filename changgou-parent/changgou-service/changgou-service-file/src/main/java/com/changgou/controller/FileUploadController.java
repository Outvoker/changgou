package com.changgou.controller;

import com.changgou.file.FastDFSFile;
import com.changgou.util.FastDFSUtil;
import entity.Result;
import entity.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.csource.common.MyException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Xu Rui
 * @date 2021/2/20 23:27
 */
@RestController
@RequestMapping("/upload")
@CrossOrigin
@Slf4j
public class FileUploadController {

    @PostMapping
    public Result<?> upload(@RequestParam("file")MultipartFile file) throws IOException, MyException {
        log.info("upload({})", file);
        FastDFSFile fastDFSFile = new FastDFSFile(
                file.getOriginalFilename(), //获取文件名
                file.getBytes(),    //获取文件字节数组
                StringUtils.getFilenameExtension(file.getOriginalFilename())    //获取文件扩展名
        );
        String[] uploads = FastDFSUtil.upload(fastDFSFile);
        //拼接访问地址
        String url = FastDFSUtil.getTrackerInfo() + '/' + uploads[0] + '/' + uploads[1];
        return new Result<>(true, StatusCode.OK, "文件上传成功", url);
    }
}

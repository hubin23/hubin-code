package com.just4cool.v1.controller;

import com.alibaba.fastjson.JSONObject;
import com.just4cool.v1.util.OkHttpHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * Created by admin on 2017/6/29.
 */
@RestController
@RequestMapping(value = "/")
public class TestController {

    /**
     * 接收文件接口
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "getFile",method= RequestMethod.POST)
    public JSONObject getFile(@RequestParam("file") MultipartFile file) throws Exception{
        JSONObject result = new JSONObject();
        result.put("fileName",file.getOriginalFilename());
        result.put("fileSize",file.getSize());
        File a = new File("MARVEL_VS_DC.jpg");
        FileOutputStream fos = new FileOutputStream(a);
        InputStream is = file.getInputStream();
        byte[] buffer = file.getBytes();
        int bytesum = 0;
        int byteread = 0;
        while ((byteread = is.read(buffer)) != -1) {
            bytesum += byteread;
            fos.write(buffer, 0, byteread);
            fos.flush();
        }
        fos.close();
        is.close();
        return result;
    }

    @RequestMapping(value = "sendFile",method= RequestMethod.POST)
    public JSONObject sendFile() throws Exception{
        JSONObject result = new JSONObject();
        JSONObject requestObj = new JSONObject();
        File file = new File("68DC_57F7EA370.jpg");
        OkHttpHelper helper = OkHttpHelper.getInstance();
        helper.sendImg("http://192.168.3.89:9090/getFile",file);
        return result;
    }
}

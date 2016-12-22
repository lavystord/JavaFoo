package com.urent.server.controller;

import com.urent.server.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/31.
 */
@Controller
public class FileAccessController {

    private final static String idProperty = "id";
    @Autowired
    FileStorageService fileStorageService;


    /**
     * 上传一个文件，这个接口可能要对用户关闭，否则有安全隐患
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/file", method = RequestMethod.POST)
    @ResponseBody
    public String uploadFile( @RequestParam("file")MultipartFile file) throws IOException {
        String id = fileStorageService.saveFile(file);
        //Map<String, String> map = new HashMap<String, String>(1);
       // map.put(idProperty, id);
        //return map;
        return id;
    }


    /**
     * 访问一个文件
     * @param id
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/file", method = RequestMethod.GET)
    public void getFile(@RequestParam(value = "id") String id, HttpServletResponse response) throws IOException {
        byte [] content = fileStorageService.readFile(id);
        response.setContentType(fileStorageService.getContentTypeById(id));
        response.getOutputStream().write(content);
        response.getOutputStream().flush();

    }

}

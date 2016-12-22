package com.urent.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urent.server.domain.CheckAppCompatibleInfo;
import com.urent.server.domain.Version;
import com.urent.server.service.FileStorageService;
import com.urent.server.service.VersionService;
import com.urent.server.util.CommonDataFormatTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/12.
 */
@RestController
public class VersionController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    VersionService versionService;

    /**
     * 增加一个版本号，由于可能上传文件，EXTJS要求这里返回的contentType必须是text/html，所以必须这样写
     * @param firmwareFile 上传的镜像文件
     * @param major  主版本号
     * @param minor  副版本号
     * @param comment   备注
     * @return
     * @throws JsonProcessingException
     */
    @RequestMapping(value = "/version", method = RequestMethod.POST)
    @ResponseBody
    public String addVersion(@RequestParam(value = "firmwareFile", required = false) MultipartFile firmwareFile,
                             @RequestParam(value = "major") Short major,
                             @RequestParam(value = "minor") Short minor,
                             @RequestParam(value = "type") Integer type,
                             @RequestParam(value = "comment")String comment
    ) throws IOException {
        String id = null;
        if(firmwareFile != null && firmwareFile.getSize() >  0) {
            id = fileStorageService.saveFile(firmwareFile);
        }

        Version version = new Version();
        version.setMajor(major);
        version.setMinor(minor);
        version.setComment(comment);
        version.setFirmwareFileId(id);
        version.setType(type);

        try {
            version = versionService.addVersion(version);
        }catch (RuntimeException e) {
            if(id != null)
                fileStorageService.removeFile(id);
            throw e;
        }

        String json = objectMapper.writeValueAsString(Collections.singletonMap("success", true));
        return json;
    }

    @RequestMapping(value = "/version", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getVersions(@RequestParam(value = "start")Integer start, @RequestParam(value = "limit") Integer limit,
                                           @RequestParam(value = "filter", required = false)String filterText,
                                           @RequestParam(value = "sort", required = false) String sortText){
        Map<String, Object> queryFilter = CommonDataFormatTool.formatQueryFilter(start, limit, filterText, sortText,
                objectMapper);
        List<Version> list = versionService.getVersions(queryFilter);
        Long total = versionService.getVersionCount(queryFilter);
        return CommonDataFormatTool.formatListResult(total, list);
    }

    @RequestMapping(value = "/version/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Version updateVersion(@RequestBody Version version, @PathVariable("id") Long id) {
        return versionService.updateVersion(version);
    }


    @RequestMapping(value = "/version/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Version getVersion(@PathVariable("id") Long id) {
        return versionService.getVersionById(id);
    }



    @RequestMapping(value = "/checkCompatible", method = RequestMethod.GET)
    @ResponseBody
    public CheckAppCompatibleInfo checkCompatible(@RequestParam(value = "version")String version,
                                                  @RequestParam(value = "type")Integer type) {
        return versionService.checkCompatible(version, type);
    }
}

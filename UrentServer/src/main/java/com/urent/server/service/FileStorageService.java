package com.urent.server.service;

import com.urent.server.USException;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by Administrator on 2015/7/31.
 *
 * 用来存取文件的类，这个类未来要独立成包
 */
public class FileStorageService {

    public static String base = "0123456789";
    private String rootDirectory;
    private File mFile;
    private String [] acceptSuffixes;

    public FileStorageService(String rootDirectory, String suffixesArray) {
        this.rootDirectory = rootDirectory;
        mFile = new File(rootDirectory);
        if(!mFile.exists() || !mFile.isDirectory()) {
            throw new RuntimeException(rootDirectory+"不是一个有效的文件路径");
        }

        acceptSuffixes = suffixesArray.split(",");
    }



    private String generateRandomString(int length) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    private String generateFileId(String suffix) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String name = format.format(new Date());
        name += generateRandomString(6);
        name += suffix;

        return name;
    }

    public String saveFile(MultipartFile file) throws IOException {
        String saveSuffix = null;
        String fileOriginalName = file.getOriginalFilename().toLowerCase();
        for(String suffix: acceptSuffixes) {
            if(fileOriginalName.endsWith(suffix)) {
                saveSuffix = suffix;
                break;
            }
        }

        if(saveSuffix == null) {
            throw new USException(USException.ErrorCode.FileTypeNotAcceptable);
        }

        String filename;
        do {
            final String filename2 = generateFileId(saveSuffix);
            if(mFile.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.equalsIgnoreCase(filename2);
                }
            }) != null) {
                filename = filename2;
                break;
            }
        }
        while(true);

        file.transferTo(new File(mFile, filename));

        return filename;
    }


    public byte[] readFile(String id) throws IOException {
        File file = new File(mFile, id);

        if(!file.exists() || file.isDirectory()) {
            throw new USException(USException.ErrorCode.FileIdNotFound);
        }

        return IOUtils.toByteArray(new FileInputStream(file));
    }


    public void removeFile(String id) throws IOException {
        if(id == null)
            return;
        File file = new File(mFile, id);

        if(!file.exists() || file.isDirectory()) {
            throw new USException(USException.ErrorCode.FileIdNotFound);
        }

        file.delete();

        return;
    }


    public String getContentTypeById(String id) {

        for(String suffix: acceptSuffixes) {
            if(id.toLowerCase().endsWith(suffix)) {
                if (suffix.equals(".jpg") || suffix.equals(".jpeg")) {
                    return "image/jpeg";
                }
                else if(suffix.equals(".png")) {
                    return "image/png";
                }
                else {
                    return "application/octet-stream";
                }
            }
        }
        return "application/octet-stream";
    }

}

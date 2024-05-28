package com.hzboiler.erp.module.base.storage;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class AbstractStorageService implements StorageService, InitializingBean {

    @Override
    public void init() throws Exception {
    }

    @Override
    public String store(MultipartFile multipartFile) throws Exception {
        return null;
    }

    @Override
    public Resource loadAsResource(String filename) throws Exception {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    static String getFilename(MultipartFile multipartFile) {
        return UUID.randomUUID() + getFilenameSuffix(multipartFile.getOriginalFilename());
    }

    private static String getFilenameSuffix(String filename) {
        String suffix = "";
        if (filename != null) {
            int index = filename.lastIndexOf(".");
            if (index >= 0) {
                suffix = filename.substring(index);
            }
        }
        return suffix;
    }
}

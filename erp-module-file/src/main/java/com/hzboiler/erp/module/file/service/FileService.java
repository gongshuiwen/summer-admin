package com.hzboiler.erp.module.file.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    void init() throws Exception;

    String store(MultipartFile multipartFile) throws Exception;

    Resource loadAsResource(String filename) throws Exception;
}

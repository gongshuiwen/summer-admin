package com.hzboiler.module.base.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    void init() throws Exception;

    String store(MultipartFile multipartFile) throws Exception;

    Resource loadAsResource(String filename) throws Exception;
}

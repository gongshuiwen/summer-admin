package com.hzboiler.module.base.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
@Profile(value = {"dev", "test"})
public class FileStorageService extends AbstractStorageService {

    private File basePath;

    @Autowired
    public void setBasePath(@Value("${reggie.base-path:./data/}") String path) {
        this.basePath = new File(path);
    }

    @Override
    public void init() throws IOException {
        log.info("Files Stored in: {}", basePath.getCanonicalPath());
    }

    @Override
    public String store(MultipartFile multipartFile) throws IOException {
        String filename = getFilename(multipartFile);
        File file = new File(basePath, filename);
        multipartFile.transferTo(file.getAbsoluteFile());
        return filename;
    }

    @Override
    public Resource loadAsResource(String filename){
        return new FileSystemResource(new File(basePath, filename));
    }
}

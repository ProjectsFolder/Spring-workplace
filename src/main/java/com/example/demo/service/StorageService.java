package com.example.demo.service;

import com.example.demo.exception.StorageFileNotFoundException;
import com.example.demo.service.interfaces.StorageServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class StorageService implements StorageServiceInterface {

    @Autowired
    Environment environment;

    @Override
    public void store(MultipartFile file) throws IOException {
        var directoryPath = this.environment.getProperty("app.uploads.directory");
        if (null == directoryPath) {
            throw new IOException("Directory name is empty");
        }

        var directory = new File(directoryPath);
        if (!directory.exists()) {
            var result = directory.mkdirs();
            if (!result) {
                throw new IOException("Could not create directory");
            }
        }

        var timestamp = new Timestamp(System.currentTimeMillis());
        var filepath = Paths.get(directoryPath, timestamp.getTime() + "_" + file.getOriginalFilename());
        file.transferTo(filepath);
    }

    @Override
    public Stream<Path> loadAll() {
        var list = new ArrayList<Path>();
        var directoryPath = this.environment.getProperty("app.uploads.directory");
        if (null != directoryPath) {
            var directory = new File(directoryPath);
            if (null != directory.listFiles()) {
                for (final File file : directory.listFiles()) {
                    list.add(file.toPath());
                }
            }
        }

        return list.stream();
    }

    @Override
    public Path load(String filename) throws StorageFileNotFoundException {
        var directoryPath = this.environment.getProperty("app.uploads.directory");
        if (null != directoryPath) {
            var file = new File(directoryPath + filename);
            if (file.exists()) {
                return file.toPath();
            }
        }

        throw new StorageFileNotFoundException();
    }

    @Override
    public Resource loadAsResource(String filename) throws StorageFileNotFoundException {
        var filePath = this.load(filename);
        if (null != filePath) {
            return new FileSystemResource(filePath);
        }

        throw new StorageFileNotFoundException();
    }
}

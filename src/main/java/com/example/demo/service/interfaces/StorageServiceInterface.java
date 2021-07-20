package com.example.demo.service.interfaces;

import com.example.demo.exception.StorageFileNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageServiceInterface {

    void store(MultipartFile file) throws IOException;

    Stream<Path> loadAll();

    Path load(String filename) throws StorageFileNotFoundException;

    Resource loadAsResource(String filename) throws StorageFileNotFoundException;

}

package com.example.demo.service.interfaces;

import com.example.demo.entity.interfaces.FileInterface;
import com.example.demo.exception.StorageFileNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface StorageServiceInterface {

    FileInterface store(String thread, MultipartFile file) throws IOException;

    List<FileInterface> loadAll();

    FileInterface load(String identifier) throws StorageFileNotFoundException;

}

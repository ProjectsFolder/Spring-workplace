package com.example.demo.dto;

import com.example.demo.validator.filenotempty.FileNotEmpty;
import org.springframework.web.multipart.MultipartFile;

public class FileDto {
    @FileNotEmpty
    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}

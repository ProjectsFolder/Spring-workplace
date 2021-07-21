package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.entity.interfaces.FileInterface;
import com.example.demo.exception.StorageFileNotFoundException;
import com.example.demo.repository.FileRepository;
import com.example.demo.service.interfaces.StorageServiceInterface;
import com.example.demo.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
public class StorageService implements StorageServiceInterface {
    @Autowired
    private FileRepository fileRepository;

    private final String uploadDirectory;

    public StorageService(@Autowired Environment environment)
    {
        this.uploadDirectory = environment.getProperty("app.uploads.directory");
    }

    @Override
    public FileInterface store(String thread, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }

        var directoryPath = this.uploadDirectory;
        if (null == directoryPath) {
            throw new IOException("Directory name is empty");
        }

        String hash = "00";
        try {
            var md = MessageDigest.getInstance("SHA-256");
            md.update(file.getBytes());
            var digest = md.digest();
            hash = String.format("%064x", new BigInteger(1, digest));
        } catch (NoSuchAlgorithmException ignored) { }
        var storedFile = fileRepository.findByHash(hash);
        if (null != storedFile) {
            return storedFile;
        }

        directoryPath += (hash.charAt(0) + "/" + hash.charAt(1) + "/");
        var directory = new File(directoryPath);
        if (!directory.exists()) {
            var result = directory.mkdirs();
            if (!result) {
                throw new IOException("Could not create directory");
            }
        }

        var filePath = Paths.get(directoryPath, hash);
        file.transferTo(filePath);

        storedFile = new com.example.demo.entity.File();
        storedFile.setThread(thread);
        storedFile.setHash(hash);
        storedFile.setSize(file.getSize());
        storedFile.setMimeType(file.getContentType());
        storedFile.setFileName(file.getOriginalFilename());
        storedFile.setFilePath(filePath.toFile().getPath());

        var extension = StringUtils.getExtensionByFilename(file.getOriginalFilename());
        storedFile.setExtension(extension.orElse(""));

        var user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user instanceof User) {
            storedFile.setUser((User) user);
        }

        fileRepository.save(storedFile);

        return storedFile;
    }

    @Override
    public List<FileInterface> loadAll() {
        return new ArrayList<>(fileRepository.findAll());
    }

    @Override
    public FileInterface load(String identifier) throws StorageFileNotFoundException {
        var storedFile = fileRepository.findById(Long.parseLong(identifier));
        if (storedFile.isPresent()) {
            return storedFile.get();
        }

        throw new StorageFileNotFoundException();
    }
}

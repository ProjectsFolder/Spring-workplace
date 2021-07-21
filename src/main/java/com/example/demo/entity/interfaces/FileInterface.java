package com.example.demo.entity.interfaces;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface FileInterface {

    String getFileName();

    String getFilePath();

    String getMimeType();

    Long getSize();

    String getExtension();

    String getIdentifier();

    Date getCreatedAt();

    UserDetails getCreator();

}

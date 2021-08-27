package com.example.demo.classes;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

public class ApiErrorResponse {

    private String message;

    public ApiErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getSuccess() {
        return 0;
    }

    public void toOutput(ServletOutputStream outputStream) throws IOException {
        var mapper = new ObjectMapper();
        mapper.writeValue(outputStream, this);
        outputStream.flush();
    }
}

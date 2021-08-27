package com.example.demo.classes;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ApiSuccessResponse {

    private Map<?, ?> data;

    public ApiSuccessResponse() {
        //noinspection rawtypes
        this.data = new HashMap();
    }

    public ApiSuccessResponse(Map<?, ?> data) {
        this.data = data;
    }

    public Map<?, ?> getData() {
        return data;
    }

    public void setData(Map<?, ?> data) {
        this.data = data;
    }

    public Integer getSuccess() {
        return 1;
    }

    public void toOutput(ServletOutputStream outputStream) throws IOException {
        var mapper = new ObjectMapper();
        mapper.writeValue(outputStream, this);
        outputStream.flush();
    }
}

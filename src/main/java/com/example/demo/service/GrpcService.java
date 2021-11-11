package com.example.demo.service;

import grpc.testservice.Request;
import grpc.testservice.TestServiceGrpc;
import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class GrpcService {
    @GrpcClient("grpc-server")
    private Channel serverChannel;

    public String send(String name) {
        var request = Request.newBuilder()
                .setName(name)
                .setBeautiful(true)
                .build();
        var stub = TestServiceGrpc.newBlockingStub(this.serverChannel);

        return stub.do_(request).getMessage();
    }
}

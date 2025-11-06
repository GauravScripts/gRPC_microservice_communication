package com.example.servicea;

import com.example.grpc.HelloProto;
import com.example.grpc.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;

@Component
public class HelloClient {

    private final ManagedChannel channel;
    private final HelloServiceGrpc.HelloServiceBlockingStub stub;

    public HelloClient() {
        this.channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        this.stub = HelloServiceGrpc.newBlockingStub(channel);
    }

    public String sayHelloGrpc(String name) {
        HelloProto.HelloRequest request = HelloProto.HelloRequest.newBuilder()
                .setName(name)
                .build();
        HelloProto.HelloResponse response = stub.sayHello(request);
        return response.getMessage();
    }

    public void shutdown() {
        channel.shutdown();
    }
}
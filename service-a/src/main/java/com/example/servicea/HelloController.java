package com.example.servicea;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HelloController {

    @Autowired
    private HelloClient grpcClient;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/grpc")
    public String callGrpc(@RequestParam String name) {
        return grpcClient.sayHelloGrpc(name);
    }
}
package com.example.servicea;

import com.example.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import org.springframework.http.converter.HttpMessageConverter;
import com.example.servicea.config.ProtobufHttpMessageConverter;

@RestController
public class TestController {

    @Autowired
    private EmployeeGrpcClient grpcClient;

    private final RestTemplate restTemplate;

    public TestController() {
        // Configure RestTemplate to understand protobuf JSON via our converter
        this.restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> converters = this.restTemplate.getMessageConverters();
        converters.add(0, new ProtobufHttpMessageConverter());
    }

    // gRPC call
    @GetMapping("/grpc/employee/{id}")
    public Employee getViaGrpc(@PathVariable int id) {
        return grpcClient.getEmployee(id);
    }

    // REST call
    @GetMapping("/rest/employee/{id}")
    public Employee getViaRest(@PathVariable int id) {
        return restTemplate.getForObject(
                "http://localhost:8081/employee/" + id,
                Employee.class);
    }

    // Add new employee via gRPC
    @PostMapping("/grpc/employee")
    public Employee addViaGrpc(@RequestBody Employee emp) {
        return grpcClient.addEmployee(emp);
    }

    // Add via REST
    @PostMapping("/rest/employee")
    public Employee addViaRest(@RequestBody Employee emp) {
        return restTemplate.postForObject(
                "http://localhost:8081/employee",
                emp,
                Employee.class);
    }
}
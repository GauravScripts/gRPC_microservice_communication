package com.example.servicea;

import com.example.Employee;
import com.example.EmployeeList;
import com.example.EmployeeServiceGrpc;
import com.example.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;

@Component
public class EmployeeGrpcClient {

    private final EmployeeServiceGrpc.EmployeeServiceBlockingStub stub;

    public EmployeeGrpcClient() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .intercept(new LoggingClientInterceptor())
                .build();
        this.stub = EmployeeServiceGrpc.newBlockingStub(channel);
    }

    public Employee getEmployee(int id) {
        Employee request = Employee.newBuilder().setId(id).build();
        return stub.getEmployee(request);
    }

    public Employee addEmployee(Employee emp) {
        return stub.addEmployee(emp);
    }

    public EmployeeList getAllEmployees() {
        return stub.getAllEmployees(Empty.newBuilder().build());
    }
}
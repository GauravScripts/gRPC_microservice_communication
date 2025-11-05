package org.example.serviceb.grpc;

import com.example.Department;
import com.example.Employee;
import com.example.EmployeeList;
import com.example.EmployeeServiceGrpc;
import com.example.Empty;
import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class EmployeeServiceImpl extends EmployeeServiceGrpc.EmployeeServiceImplBase {

    private final AtomicInteger idGenerator = new AtomicInteger(1);
    private final ConcurrentHashMap<Integer, Employee> employeeDb = new ConcurrentHashMap<>();

    public EmployeeServiceImpl() {
        // Preload one employee for testing
        Department dept = Department.newBuilder().setId(1).setName("Engineering").build();
        Employee emp = Employee.newBuilder()
                .setId(1)
                .setName("Gaurav")
                .setSalary(95000.0)
                .addDepartments(dept)
                .putAddressMap("city", "Bangalore")
                .putAddressMap("country", "India")
                .setIsActive(true)
                .setJoinDate(Timestamp.newBuilder().setSeconds(Instant.now().getEpochSecond()).build())
                .build();
        employeeDb.put(1, emp);
    }

    @Override
    public void getEmployee(Employee request, StreamObserver<Employee> responseObserver) {
        Employee emp = employeeDb.get(request.getId());
        if (emp != null) {
            responseObserver.onNext(emp);
        } else {
            responseObserver.onError(new RuntimeException("Employee not found"));
        }
        responseObserver.onCompleted();
    }

    @Override
    public void addEmployee(Employee request, StreamObserver<Employee> responseObserver) {
        Employee emp = request.toBuilder().setId(idGenerator.getAndIncrement()).build();
        employeeDb.put(emp.getId(), emp);
        responseObserver.onNext(emp);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllEmployees(Empty request, StreamObserver<EmployeeList> responseObserver) {
        EmployeeList list = EmployeeList.newBuilder()
                .addAllEmployees(new ArrayList<>(employeeDb.values()))
                .build();
        responseObserver.onNext(list);
        responseObserver.onCompleted();
    }
}
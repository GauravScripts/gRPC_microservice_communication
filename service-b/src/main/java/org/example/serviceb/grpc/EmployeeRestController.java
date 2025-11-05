package org.example.serviceb.grpc;


import com.example.Department;
import com.example.Employee;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class EmployeeRestController {

    private final List<Employee> employees = new CopyOnWriteArrayList<>();

    public EmployeeRestController() {
        // Same as gRPC preload
        var dept = Department.newBuilder().setId(1).setName("Engineering").build();
        var emp = Employee.newBuilder()
                .setId(1)
                .setName("Gaurav")
                .setSalary(95000.0)
                .addDepartments(dept)
                .putAddressMap("city", "Bangalore")
                .putAddressMap("country", "India")
                .setIsActive(true)
                .build();
        employees.add(emp);
    }

    @GetMapping("/employee/{id}")
    public Employee getEmployee(@PathVariable int id) {
        return employees.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElseThrow();
    }

    @PostMapping("/employee")
    public Employee addEmployee(@RequestBody Employee emp) {
        Employee newEmp = emp.toBuilder().setId((int) (employees.size() + 1)).build();
        employees.add(newEmp);
        return newEmp;
    }

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return employees;
    }
}

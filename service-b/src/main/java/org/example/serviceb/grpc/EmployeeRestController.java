package org.example.serviceb.grpc;


import com.example.Department;
import com.example.Employee;
import org.example.serviceb.api.DepartmentDto;
import org.example.serviceb.api.EmployeeDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

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

    private static EmployeeDto toDto(Employee e) {
        List<DepartmentDto> departments = e.getDepartmentsList().stream()
                .map(d -> new DepartmentDto(d.getId(), d.getName()))
                .collect(Collectors.toList());
        return new EmployeeDto(
                e.getId(),
                e.getName(),
                e.getSalary(),
                departments,
                e.getAddressMapMap(),
                e.getIsActive()
        );
    }

    private static Employee fromDto(EmployeeDto dto) {
        Employee.Builder builder = Employee.newBuilder()
                .setId(dto.id())
                .setName(dto.name() == null ? "" : dto.name())
                .setSalary(dto.salary() == null ? 0.0 : dto.salary())
                .setIsActive(Boolean.TRUE.equals(dto.isActive()));

        if (dto.departments() != null) {
            for (DepartmentDto d : dto.departments()) {
                builder.addDepartments(Department.newBuilder()
                        .setId(d.id())
                        .setName(d.name() == null ? "" : d.name())
                        .build());
            }
        }
        if (dto.addressMap() != null) {
            for (Map.Entry<String, String> entry : dto.addressMap().entrySet()) {
                builder.putAddressMap(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }

    @GetMapping("/employee/{id}")
    public EmployeeDto getEmployee(@PathVariable int id) {
        Employee e = employees.stream()
                .filter(emp -> emp.getId() == id)
                .findFirst()
                .orElseThrow();
        return toDto(e);
    }

    @PostMapping("/employee")
    public EmployeeDto addEmployee(@RequestBody EmployeeDto dto) {
        int newId = employees.size() + 1;
        Employee newEmp = fromDto(new EmployeeDto(
                newId,
                dto.name(),
                dto.salary(),
                dto.departments(),
                dto.addressMap(),
                dto.isActive()
        ));
        employees.add(newEmp);
        return toDto(newEmp);
    }

    @GetMapping("/employees")
    public List<EmployeeDto> getAllEmployees() {
        return employees.stream().map(EmployeeRestController::toDto).toList();
    }
}

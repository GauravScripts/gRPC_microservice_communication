package org.example.serviceb.api;

import java.util.List;
import java.util.Map;

public record EmployeeDto(
        int id,
        String name,
        Double salary,
        List<DepartmentDto> departments,
        Map<String, String> addressMap,
        Boolean isActive
) {}


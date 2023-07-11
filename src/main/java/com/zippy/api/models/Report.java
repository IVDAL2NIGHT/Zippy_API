package com.zippy.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    private EmployeeReport employeeReport;
    private String userReport;

    public Report(String userReport) {
        this.userReport = userReport;
    }

    public Report(EmployeeReport employeeReport) {
        this.employeeReport = employeeReport;
    }

}

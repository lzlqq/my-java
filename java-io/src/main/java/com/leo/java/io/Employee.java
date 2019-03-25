package com.leo.java.io;

import java.io.Serializable;

/**
 * Created by LSH7120 on 2019/3/12.
 */
public class Employee implements Serializable{
    private int employeeId;
    private String employeeName;
    private String department;

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getDepartment() {
        return department;
    }
}

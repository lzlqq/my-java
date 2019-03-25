package com.leo.java.io;

/**
 * Created by LSH7120 on 2019/3/12.
 */


import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class HessianSerializeDeserializeMain {

    /**
     * Hessian实现序列化
     *
     * @param employee
     * @return
     * @throws IOException
     */
    public static byte[] serialize(Employee employee) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        HessianOutput hessianOutput = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            // Hessian的序列化输出
            hessianOutput = new HessianOutput(byteArrayOutputStream);
            hessianOutput.writeObject(employee);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                hessianOutput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Hessian实现反序列化
     *
     * @param employeeArray
     * @return
     */
    public static Employee deserialize(byte[] employeeArray) {
        ByteArrayInputStream byteArrayInputStream = null;
        HessianInput hessianInput = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(employeeArray);
            // Hessian的反序列化读取对象
            hessianInput = new HessianInput(byteArrayInputStream);
            return (Employee) hessianInput.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                byteArrayInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                hessianInput.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) {

        Employee employee = new Employee();
        employee.setEmployeeId(1);
        employee.setEmployeeName("赵新国");
        employee.setDepartment("软件工程师");
        // 序列化
        byte[] serialize = serialize(employee);
        System.out.println(serialize);
        // 反序列化
        Employee deserialize = deserialize(serialize);
        System.out.println(deserialize.toString());

    }

}

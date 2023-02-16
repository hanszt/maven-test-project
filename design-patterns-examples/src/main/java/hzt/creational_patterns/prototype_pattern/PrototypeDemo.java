package hzt.creational_patterns.prototype_pattern;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class PrototypeDemo {
    
    public static void main(String... args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter Employee Id: ");
        int id = Integer.parseInt(reader.readLine());
        System.out.println();

        System.out.print("Enter Employee Name: ");
        String name = reader.readLine();
        System.out.println();

        System.out.print("Enter Employee Designation: ");
        String designation = reader.readLine();
        System.out.println();

        System.out.print("Enter Employee Address: ");
        String address = reader.readLine();
        System.out.println();

        System.out.print("Enter Employee Salary: ");
        double salary = Double.parseDouble(reader.readLine());
        System.out.println();

        EmployeeRecord record1 = new EmployeeRecord(id, name, designation, salary, address);

        record1.showRecord();
        System.out.printf("%n%n");
        EmployeeRecord record2 = (EmployeeRecord) record1.getClone();
        record2.showRecord();
    }
}

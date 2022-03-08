package hzt.creational_patterns.prototype_pattern;

class EmployeeRecord implements Prototype {

    private int id;
    private String name;
    private String designation;
    private double salary;
    private String address;

    private static final int ID_SPACER = 10;
    private static final int SALARY_SPACER = 12;
    private static final int SPACER = 15;

    public EmployeeRecord() {
        System.out.println(header());
    }

    private String header() {
        return String.format("   Employee Records of Oracle Corporation %n" +
                        "---------------------------------------------%n" +
                        "%-" + ID_SPACER + "s%-" + SPACER + "s%-" + SPACER + "s%-" + SALARY_SPACER + "s%-" + SPACER + "s",
                "Id", "Name", "Designation", "Salary", "Address");
    }

    public EmployeeRecord(int id, String name, String designation, double salary, String address) {
        this();
        this.id = id;
        this.name = name;
        this.designation = designation;
        this.salary = salary;
        this.address = address;
    }

    public void showRecord() {
        System.out.printf("%-" +ID_SPACER + "d%-" + SPACER + "s%-" + SPACER + "s%-" + SALARY_SPACER + "s%-" + SPACER + "s%n",
                id, name, designation, salary, address);
    }

    @Override
    public Prototype getClone() {
        return new EmployeeRecord(id, name, designation, salary, address);
    }
}

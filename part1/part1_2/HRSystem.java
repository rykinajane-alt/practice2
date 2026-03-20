package part1.part1_2;

public class HRSystem {
    public static void main(String[] args) {
        EmployeeFixed emp = new EmployeeFixed("Иван", 30, 80000, "secret");

        System.out.println(emp.getName());
        System.out.println(emp.getAge());
        System.out.println(emp.getSalary());
        System.out.println(emp.getRole());

        emp.promote(5000);
        System.out.println("После повышения: " + emp.getSalary()); // 85000.0

        emp.printSummary();
        System.out.println("Аутентификация: " + emp.authenticate("secret")); // true
    }
}
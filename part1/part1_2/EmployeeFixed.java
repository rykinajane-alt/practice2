package part1.part1_2;

public class EmployeeFixed {
    private String name;
    private int age;
    private double salary;
    private String password;

    public EmployeeFixed(String name, int age, double salary, String password) {
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.password = password;
    }

    // Геттеры для полей (без password)
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getSalary() {
        return salary;
    }

    public String getRole() {
        return "Employee";
    }

    // Публичный метод для повышения зарплаты
    public void promote(double raise) {
        if (raise > 0) {
            this.salary += raise;
        }
    }

    // Публичный метод для вывода информации
    public void printSummary() {
        System.out.println(name + ", " + age + " лет, " + salary + " руб.");
    }

    // Приватный метод проверки пароля
    private boolean validatePassword(String input) {
        return password.equals(input);
    }

    // Публичный метод аутентификации
    public boolean authenticate(String input) {
        return validatePassword(input);
    }
}
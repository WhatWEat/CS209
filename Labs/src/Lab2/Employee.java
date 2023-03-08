package Lab2;

import java.util.Objects;

public class Employee {

    private Long id;
    private String name;

    public void setId(Long id) {
        this.id = id;
    }

    public Employee(Long id, String name) {
        this.name = name;
        this.id = id;
    }

}
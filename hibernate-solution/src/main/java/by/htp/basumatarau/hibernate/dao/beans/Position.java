package by.htp.basumatarau.hibernate.dao.beans;

import java.util.Objects;
import java.util.Set;

public class Position {
    private int id;
    private String name;
    private Set<Employee> employees;

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return id == position.id &&
                name.equals(position.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}

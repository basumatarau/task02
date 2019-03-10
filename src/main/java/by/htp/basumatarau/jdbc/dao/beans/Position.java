package by.htp.basumatarau.jdbc.dao.beans;

import java.util.Objects;

public class Position {
    private int id;
    private String name;

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

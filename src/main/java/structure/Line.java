package structure;

import java.util.Objects;

public class Line {
    private String number;
    private String name;

    public Line(String number, String name) {
        this.number = number;
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
        {
            return true;
        }
        Line line = (Line) obj;
        return Objects.equals(number, line.number) && Objects.equals(name, line.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, name);
    }
}

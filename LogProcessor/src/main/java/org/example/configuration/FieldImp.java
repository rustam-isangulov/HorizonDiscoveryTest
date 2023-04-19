package org.example.configuration;

public class FieldImp implements Field {
    private String name;
    private int index;

    public FieldImp( String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return this.name;
    }
    public int getIndex() {
        return index;
    }

    public String toString() {
        return "[" + this.name + "] [" + this.index + "]";
    }
}

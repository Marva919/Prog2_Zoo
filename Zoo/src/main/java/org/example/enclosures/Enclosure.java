package org.example.enclosures;

import org.example.animals.Animal;
import java.util.HashSet;
import java.util.Set;

public class Enclosure<T extends Animal> {
    private  String name;
    private Set<T> inhabitants = new HashSet<>();

    public boolean addInhabitant(T t) {
        return inhabitants.add(t);
    }

    public boolean removeInhabitant(T t) {
        return inhabitants.remove(t);
    }

    public Set<T> getInhabitants() {
        return inhabitants;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}

package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.example.animals.Animal;
import org.example.animals.Mammal;
import org.example.enclosures.*;

public class Zoo {

    Logger log = Logger.getLogger(Zoo.class.getName());
    private final List<Enclosure<?>> enclosures = new ArrayList<>();

    public boolean addEnclosure(Enclosure<? extends Animal> a) {
        log.info("addEnclosure aufgerufen: " + a.getName());
        boolean result = this.enclosures.add(a);
        log.fine("Zoo enthaelt jetzt " + enclosures.size() + " Gehege");  // Zustand danach -> FINE
        return result;
    }

    public List<Enclosure<?>> getEnclosures() {
        log.info("getEnclosures aufgerufen");
        List<Enclosure<?>> result = List.copyOf(enclosures);
        log.fine("getEnclosures lieferte " + result.size() + " Gehege zurueck");
        return result;
    }

    public Enclosure<?> findEnclosureByName(String name) {
        log.info("findEnclosureByName aufgerufen: " + name);
        for (Enclosure<?> enclosure : this.enclosures) {
            if (enclosure.getName().equals(name)) {
                log.fine("findEnclosureByName hat Gehege gefunden: " + enclosure.getName());
                return enclosure;
            }
        }
        log.warning("findEnclosureByName: kein Gehege gefunden mit Namen " + name);
        return null;
    }

    public List<Animal> getAllAnimals() {
        log.info("getAllAnimals aufgerufen");
        List<Animal> result = enclosures.stream()
                .flatMap(e -> e.getInhabitants().stream())
                .<Animal>map(a -> a)// Hebt den Typ auf Animal an.
                .toList();
        log.fine("getAllAnimals lieferte " + result.size() + " Tiere zurueck");
        return result;
    }

    public List<Mammal> getAllMammals() {
        log.info("getAllMammals aufgerufen");
        List<Mammal> result = enclosures.stream()
                .flatMap(e -> e.getInhabitants().stream())
                .filter(a -> a instanceof Mammal)
                .map(a -> (Mammal) a)
                .toList();
        log.fine("getAllMammals lieferte " + result.size() + " Saeugetiere zurueck");
        return result;
    }

    public List<Animal> getAnimalsByPredicate(Predicate<Animal> predicate) {
        log.info("getAnimalsByPredicate aufgerufen");
        List<Animal> result = enclosures.stream()
                .flatMap(e -> e.getInhabitants().stream())
                .filter(predicate)
                .<Animal>map(a -> a)
                .toList();
        log.fine("getAnimalsByPredicate lieferte " + result.size() + " Tiere zurueck");
        return result;
    }

    public Map<Class<? extends Animal>, Long> countAnimalsByType() {
        log.info("countAnimalsByType aufgerufen");
        Map<Class<? extends Animal>, Long> result = enclosures.stream()
                .flatMap(e -> e.getInhabitants().stream())
                .collect(Collectors.groupingBy(
                        Animal::getClass,
                        Collectors.counting()
                ));
        log.fine("countAnimalsByType lieferte " + result.size() + " unterschiedliche Typen");
        return result;
    }

    public List<Enclosure<?>> getOvercrowdedEnclosures(int maxAnimals) {
        log.info("getOvercrowdedEnclosures aufgerufen mit maxAnimals=" + maxAnimals);
        if (maxAnimals < 0) {
            log.severe("getOvercrowdedEnclosures mit negativem maxAnimals aufgerufen: " + maxAnimals);
        }
        List<Enclosure<?>> result = enclosures.stream()
                .filter(e -> e.getInhabitants().size() > maxAnimals)
                .toList();
        log.fine("getOvercrowdedEnclosures lieferte " + result.size() + " ueberfuellte Gehege");
        return result;
    }

    public String summary() {
        log.info("summary aufgerufen");
        long enclosureCount = enclosures.size();
        long animalCount = getAllAnimals().size();

        Map<Class<? extends Animal>, Long> byType = countAnimalsByType();

        String typeSummary = byType.entrySet().stream()
                .map(e -> e.getValue() + " " + e.getKey().getSimpleName() + "s")
                .collect(Collectors.joining(", "));

        String result = "Zoo mit " + enclosureCount + " Gehegen und "
                + animalCount + " Tieren: "
                + typeSummary;

        log.fine("summary erzeugt: " + result);
        return result;
    }
}

package org.example;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.example.animals.*;
import org.example.enclosures.*;

public class Main {
    public static void main(String[] args) {

        // ---- Logger-Konfiguration ----
        Logger zooLogger = Logger.getLogger(Zoo.class.getName());
        zooLogger.setUseParentHandlers(false);

        ConsoleHandler handler = new ConsoleHandler();
        zooLogger.addHandler(handler);

        // Phase 1: alles sehen (INFO + FINE + WARNING + SEVERE)
        zooLogger.setLevel(Level.ALL);
        handler.setLevel(Level.ALL);

        // ---- Zoo aufbauen ----
        Zoo zoo = new Zoo();

        Aquarium aquarium = new Aquarium();
        aquarium.setName("Aquarium 1");
        Terrarium terrarium = new Terrarium();
        terrarium.setName("Terrarium 1");
        MammalHouse mammalHouse = new MammalHouse();
        mammalHouse.setName("Saeugetierhaus");
        CatHouse<Lion> catHouse = new CatHouse<>();
        catHouse.setName("Loewengehege");

        zoo.addEnclosure(aquarium);
        zoo.addEnclosure(terrarium);
        zoo.addEnclosure(mammalHouse);
        zoo.addEnclosure(catHouse);

        // Tiere hinzufuegen (addInhabitant ist public auf Enclosure)
        aquarium.addInhabitant(new Traut("Nemo"));
        aquarium.addInhabitant(new Salmon("Dorie"));

        terrarium.addInhabitant(new Gecko("Gex"));
        terrarium.addInhabitant(new Python("Kaa"));

        mammalHouse.addInhabitant(new Elephant("Dumbo"));

        catHouse.addInhabitant(new Lion("Simba"));
        catHouse.addInhabitant(new Lion("Mufasa"));

        // ---- Zoo-Methoden demonstrieren ----
        IO.println(zoo.summary());
        IO.println("Alle Tiere: " + zoo.getAllAnimals());
        IO.println("Alle Saeugetiere: " + zoo.getAllMammals());
        IO.println("Ueberfuellte Gehege (>1 Tier): " + zoo.getOvercrowdedEnclosures(1));
        IO.println("Anzahl pro Typ: " + zoo.countAnimalsByType());

        Enclosure<?> found = zoo.findEnclosureByName("Loewengehege");
        IO.println("Gefundenes Gehege: " + (found != null ? found.getName() : "nicht gefunden"));

        // Provoziert WARNING (Gehege existiert nicht)
        zoo.findEnclosureByName("Nicht existent");

        // Beispiel fuer Predicate-Nutzung
        IO.println("Alle Loewen: "
                + zoo.getAnimalsByPredicate(a -> a instanceof Lion));

        // Provoziert SEVERE (unsinniger negativer Parameter)
        zoo.getOvercrowdedEnclosures(-1);

        // ---- Phase 2: Log-Level umschalten ----
        // Nur noch WARNING und SEVERE werden angezeigt
        zooLogger.setLevel(Level.WARNING);
        handler.setLevel(Level.WARNING);

        IO.println("\n--- Nach Umschalten auf WARNING-Level ---");
        zoo.summary();                       // INFO/FINE jetzt unsichtbar
        zoo.findEnclosureByName("Geist");     // WARNING weiterhin sichtbar
    }
}

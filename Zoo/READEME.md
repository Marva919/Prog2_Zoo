# Blatt 07: Zoo-Verwaltung – Generics, Sealed Types, Stream-API, Logging

## Aufgabe 3: Reflektion

### 1. Generics

**Wo helfen Generics, Fehler bereits zur Compile-Zeit zu vermeiden?**

- `Enclosure<T extends Animal>` stellt sicher, dass nur `Animal`-Subtypen
  als Gehegebewohner eingesetzt werden können. Ein `Enclosure<String>`
  würde gar nicht erst kompilieren.
- Die spezialisierten Gehege (`Aquarium extends Enclosure<Fish>`,
  `Terrarium extends Enclosure<Reptile>`, `MammalHouse extends Enclosure<Mammal>`)
  binden den Typparameter fest an die jeweilige Tierfamilie. Ein Fisch
  kann so nicht versehentlich in ein `Terrarium` eingefügt werden.
- `CatHouse<T extends Cat>` geht noch einen Schritt weiter: Es erlaubt
  nur **eine konkrete** Cat-Unterart pro Instanz (z. B. nur `Lion`,
  nicht gemischt mit `Tiger`), da `T` bei der Instanziierung auf einen
  Typ festgelegt wird.

**Beispiel aus der Implementierung:**

```java
Aquarium aquarium = new Aquarium();
aquarium.addInhabitant(new Lion("Simba")); // Compilerfehler!
```

Dieser Aufruf schlägt fehl, weil `Aquarium` nur `Fish`-Objekte über
`addInhabitant(Fish f)` akzeptiert – `Lion` ist kein `Fish`. Der Fehler
wird also bereits beim Kompilieren erkannt, nicht erst zur Laufzeit.

### 2. Logging

**Warum ist systematisches Logging sinnvoller als `IO.println`?**

- Log-Level erlauben es, die Menge an Information je nach Bedarf zu
  steuern (z. B. im Produktivbetrieb nur `WARNING`/`SEVERE`, beim
  Debuggen zusätzlich `INFO`/`FINE`), ohne den Code zu verändern.
- Logger-Ausgaben enthalten automatisch Zeitstempel, Klassen- und
  Methodennamen – das erleichtert die Fehlersuche erheblich gegenüber
  reinen `println`-Ausgaben.
- Handler lassen sich flexibel konfigurieren (Konsole, Datei, ...),
  ohne den eigentlichen Anwendungscode anzufassen.
- `println`-Aufrufe müssten bei Bedarf manuell aus dem Code entfernt
  oder auskommentiert werden – Logging bleibt dauerhaft im Code und
  wird einfach je nach Level ein- oder ausgeblendet.

**Wann werden `INFO`, `WARNING` und `SEVERE` verwendet?**

- **`INFO`**: Zu Beginn jeder public-Methode in `Zoo`, um den Aufruf
  und die relevanten Parameter zu protokollieren (z. B.
  `"addEnclosure aufgerufen: Loewengehege"`).
- **`WARNING`**: Wenn ein angefragtes Gehege nicht gefunden wird, z. B.
  in `findEnclosureByName`, wenn kein Gehege mit dem gesuchten Namen
  existiert.
- **`SEVERE`**: Bei inhaltlich unsinnigen bzw. inkonsistenten
  Aufrufen, z. B. wenn `getOvercrowdedEnclosures` mit einem negativen
  `maxAnimals`-Wert aufgerufen wird – ein solcher Parameter deutet auf
  einen Programmierfehler beim Aufrufer hin.

### 3. Streams

**Wo haben Streams geholfen, wo wurde es unübersichtlich?**

- Besonders hilfreich waren Streams beim Verarbeiten verschachtelter
  Strukturen: `flatMap` erlaubt es, alle Tiere aus allen Gehegen in
  einem einzigen Ausdruck zu sammeln (`getAllAnimals`), ohne
  verschachtelte `for`-Schleifen mit manuellem Zwischenspeichern in
  einer Hilfsliste.
- `Collectors.groupingBy` in Kombination mit `Collectors.counting()`
  hat `countAnimalsByType` deutlich kompakter gemacht, als es eine
  manuelle `Map`-Verwaltung mit Schleife und `getOrDefault` gewesen
  wäre.
- Etwas unübersichtlich wurde es in `summary()`, da dort mehrere
  bereits geloggte Methoden (`getAllAnimals()`, `countAnimalsByType()`)
  intern erneut aufgerufen werden. Das führt zu verschachtelten,
  teils redundanten Log-Ausgaben für einen einzigen `summary()`-Aufruf
  und macht das Log-Protokoll an dieser Stelle schwerer lesbar.
- Bei der Typumwandlung von `Animal` auf konkrete Subtypen
  (`getAllMammals`) war ein expliziter Cast nach dem `filter`
  notwendig, da der Compiler die Typeinschränkung aus `instanceof`
  nicht automatisch auf den nachfolgenden `map`-Schritt überträgt –
  hier blieb der Stream-Ausdruck weniger elegant als bei rein
  funktionalen Operationen.

## Repository

GitHub: https://github.com/Marva919/Prog2-2026-MS

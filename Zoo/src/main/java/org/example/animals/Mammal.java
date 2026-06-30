package org.example.animals;

public sealed interface Mammal extends Animal permits Elephant, Cat {
}

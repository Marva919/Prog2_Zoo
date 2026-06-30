package org.example.animals;

public sealed interface Bird extends Animal permits Eagle, Parrot {
}

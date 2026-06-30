package org.example.animals;

public sealed interface Fish extends Animal permits Traut, Salmon {
}

package org.example.animals;

public sealed interface Reptile extends Animal permits Python, Gecko {
}

package org.example.animals;

public sealed interface Animal permits Fish, Reptile, Mammal, Bird
{
}

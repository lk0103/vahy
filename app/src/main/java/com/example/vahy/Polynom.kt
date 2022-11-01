package com.example.vahy

abstract class Polynom {
    abstract fun valueAt(variable : String) : Double?
    abstract fun evaluate() : Double
}
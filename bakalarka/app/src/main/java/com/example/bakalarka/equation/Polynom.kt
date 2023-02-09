package com.example.vahy.equation

abstract class Polynom {
    abstract fun valueAt(variable : String) : Double?
    abstract fun evaluate() : Double

    fun removeVariable() {

    }

    fun addVariable() {

    }

    fun removeConstant(value : Double) {

    }

    fun addConstant(value : Double ) {

    }

    fun substractFromConstant(fromValue: Double) {

    }

    fun addToConstant(toValue: Double) {

    }
}
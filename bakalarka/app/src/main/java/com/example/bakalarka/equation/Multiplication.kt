package com.example.vahy.equation

import com.example.bakalarka.equation.Bracket

class Multiplication(private var polynom : Polynom,
                     private var multiple : Constant) : Polynom(){

    override fun simplify(): Polynom? {
        if (multiple.evaluate(mutableMapOf()) == 0) return null
        else if (multiple.evaluate(mutableMapOf()) == 1) return polynom.simplify()
        polynom.simplify()
        return this
    }

    fun getMultiple() : Constant = multiple

    fun getPolynom() : Polynom = polynom

    override fun evaluate(variables : Map<String, Int>): Int =
        polynom.evaluate(variables) * multiple.evaluate(variables)

    override fun countNumVariableTypes(): MutableMap<String, Int> {
        val numVarTypes = mutableMapOf<String, Int>()
        polynom.countNumVariableTypes().forEach{
            val newValue = ((numVarTypes[it.key] ?: 0)
                    + it.value * multiple.evaluate(mapOf()))
            numVarTypes[it.key] = newValue
        }
        return numVarTypes
    }

    override fun countNumConsValues(): MutableMap<Int, Int> {
        val numConsValues = mutableMapOf<Int, Int>()
        polynom.countNumConsValues().forEach{
            val newValue = ((numConsValues[it.key] ?: 0)
                    + it.value * multiple.evaluate(mapOf()))
            numConsValues[it.key] = newValue
        }
        return numConsValues
    }

    override fun countNumBrackets(): MutableMap<Bracket, Int> {
        val numBrackets = mutableMapOf<Bracket, Int>()
        polynom.countNumBrackets().forEach{
            val newValue = ((numBrackets[it.key] ?: 0)
                    + it.value * multiple.evaluate(mapOf()))
            numBrackets[it.key] = newValue
        }
        return numBrackets
    }

    override fun setAllBracketInsides(bracketInside: Addition) {
        polynom.setAllBracketInsides(bracketInside)
    }

    override fun toString(): String = multiple.toString() +
            (if (polynom is Constant) "*" else "") + polynom.toString()

    override fun equals(other: Any?): Boolean {
        if (!(other is Multiplication)) return false
        return multiple == other.multiple && polynom == other.polynom
    }

    override fun addToConstant(fromValue: Int, value : Int) : Polynom? {
        if (polynom is Bracket) return null
        val added = polynom.addToConstant(fromValue, value)
        return decrement(added)
    }

    override fun addConstant(value: Int): Polynom? {
        if (polynom is Bracket) return null
        val added = polynom.addConstant(value)
        return increment(added)
    }

    override fun removeConstant(value: Int): Polynom? {
        if (polynom is Bracket) return null
        val added = polynom.removeConstant(value)
        return decrement(added)
    }

    override fun addVariable(name: String): Polynom? {
        if (polynom is Bracket) return null
        val added = polynom.addVariable(name)
        return increment(added)
    }

    override fun removeVariable(name: String): Polynom? {
        if (polynom is Bracket) return null
        val added = polynom.removeVariable(name)
        return decrement(added)
    }


    override fun addBracket(bracket: Bracket): Polynom? {
        val added = polynom.addBracket(bracket)
        return increment(added)
    }

    override fun removeBracket(bracket: Bracket): Polynom? {
        val added = polynom.removeBracket(bracket)
        return decrement(added)
    }

    private fun increment(added: Polynom?): Polynom? {
        if (added != null) {
            multiple.increment()
        }
        return added
    }

    private fun decrement(added: Polynom?): Polynom? {
        if (added != null) {
            multiple.decrement()
        }
        return added
    }

    override fun copy(): Polynom {
        return Multiplication(polynom.copy(), multiple.copy() as Constant)
    }

}
package com.example.vahy.equation

import com.example.bakalarka.equation.Bracket

class Addition(var addends : MutableList<Polynom>) : Polynom(){

    override fun simplify() : Polynom?{
        val toRemove = mutableListOf<Polynom>()
        val toAdd = mutableListOf<Polynom>()
        addends.forEach { p ->
            val simplified = p.simplify()
            if (simplified == null) {
                toRemove.add(p)
            }else if (simplified != p){
                toRemove.add(p)
                toAdd.add(simplified)
            }
        }
        addends.removeAll(toRemove)
        addends.addAll(toAdd)
        return this
    }

    override fun evaluate(variables : Map<String, Int>): Int =
        addends.map { it.evaluate(variables) }.sum()

    override fun countNumVariableTypes(): MutableMap<String, Int> {
        val numVarTypes = mutableMapOf<String, Int>()
        addends.forEach { pol ->
            pol.countNumVariableTypes().forEach{
                val newValue = (numVarTypes.getOrDefault(it.key, 0) + it.value)
                numVarTypes[it.key] = newValue
            }
        }
        return numVarTypes
    }

    override fun countNumConsValues(): MutableMap<Int, Int> {
        val numConsValues = mutableMapOf<Int, Int>()
        addends.forEach { pol ->
            pol.countNumConsValues().forEach{
                val newValue = (numConsValues.getOrDefault(it.key, 0) + it.value)
                numConsValues[it.key] = newValue
            }
        }
        return numConsValues
    }

    override fun countNumBrackets(): MutableMap<Bracket, Int> {
        val numBrackets = mutableMapOf<Bracket, Int>()
        addends.forEach { pol ->
            pol.countNumBrackets().forEach{
                val newValue = (numBrackets.getOrDefault(it.key, 0) + it.value)
                numBrackets[it.key] = newValue
            }
        }
        return numBrackets
    }

    override fun setAllBracketInsides(bracketInside: Addition) {
        addends.forEach { it.setAllBracketInsides(bracketInside) }
    }

    override fun toString(): String =
        addends.map { it.toString() }.joinToString(separator = " + ")

    override fun equals(other: Any?): Boolean {
        if (!(other is Addition)) return false
        if (other.addends.size != addends.size) return false
        return (0 until addends.size).all { i ->
            addends[i] == other.addends[i]
        }
    }

    override fun addToConstant(fromValue: Int, value : Int) : Polynom?{
        for (polynom in addends.filter { it is Multiplication || it is Constant }){
            val added = polynom.addToConstant(fromValue, value)
            if (added != null) {
                if (polynom is Constant){
                    addends.remove(polynom)
                }
                if (added.evaluate(mapOf()) != 0) addends.add(added)
                return added
            }
        }
        return null
    }

    override fun addConstant(value: Int): Polynom? {
        for (polynom in addends.filter { it is Multiplication || it is Constant }){
            val added = polynom.addConstant(value)
            if (addObj(added, polynom)) return added
        }
        val added = Constant(value)
        addends.add(added)
        return added
    }

    override fun removeConstant(value: Int): Polynom? {
        for (polynom in addends.filter { it is Multiplication || it is Constant }){
            val added = polynom.removeConstant(value)
            if (removeObj(added, polynom)) return added
        }
        return null
    }

    override fun addVariable(name: String): Polynom? {
        for (polynom in addends.filter { it is Multiplication || it is Variable }){
            val added = polynom.addVariable(name)
            if (addObj(added, polynom)) return added
        }
        val added = Variable(name)
        addends.add(added)
        return added
    }

    override fun removeVariable(name: String): Polynom? {
        for (polynom in addends.filter { it is Multiplication || it is Variable }){
            val added = polynom.removeVariable(name)
            if (removeObj(added, polynom)) return added
        }
        return null
    }

    override fun addBracket(bracket: Bracket): Polynom? {
        for (polynom in addends.filter { it is Multiplication || it is Bracket }){
            val added = polynom.addBracket(bracket)
            if (addObj(added, polynom)) return added
        }
        addends.add(bracket)
        return bracket
    }

    override fun removeBracket(bracket: Bracket): Polynom? {
        for (polynom in addends.filter { it is Multiplication || it is Bracket }){
            val added = polynom.removeBracket(bracket)
            if (removeObj(added, polynom)) return added
        }
        return null
    }

    private fun removeObj(added: Polynom?, polynom: Polynom): Boolean {
        if (added != null) {
            if (!(polynom is Multiplication)) {
                addends.remove(added)
            } else if (polynom.getMultiple().evaluate(mapOf()) == 0) {
                addends.remove(polynom)
            } else if (polynom.getMultiple().evaluate(mapOf()) == 1) {
                addends.add(polynom.getPolynom())
                addends.remove(polynom)
            }
            return true
        }
        return false
    }


    private fun addObj(added: Polynom?, polynom: Polynom): Boolean {
        if (added != null) {
            if (!(polynom is Multiplication)) {
                addends.remove(added)
                addends.add(Multiplication(added, Constant(2)))
            }
            return true
        }
        return false
    }

    fun size() : Int = addends.size

    override fun copy(): Polynom {
        return Addition(addends.map { it.copy() }.toMutableList())
    }
}
package com.example.vahy.equation

import android.util.Log
import com.example.bakalarka.equation.Bracket

//lava strana bude variable, constant, bracked a prava strana bude nasobok
class Multiplication(private var polynom : Polynom,
                     private var multiple : Constant
) : Polynom(){

    override fun simplify(): Polynom? =
        if (multiple.evaluate(mutableMapOf()) == 0) null
        else if (multiple.evaluate(mutableMapOf()) == 1) polynom.simplify()
        else this


    fun getMultiple() : Constant = multiple

    fun getPolynom() : Polynom = polynom

    override fun evaluate(variables : Map<String, Int>): Int =
        polynom.evaluate(variables) * multiple.evaluate(variables)

    override fun findAllVariables(): Set<String> = polynom.findAllVariables()

    override fun findAllBrackets(): Set<Bracket> = polynom.findAllBrackets()

    override fun containsBracket(): Boolean = polynom.containsBracket()

    override fun toString(): String = multiple.toString() + "*" + polynom.toString()

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

}
package com.example.bakalarka.equation

import android.util.Log
import com.example.vahy.equation.Addition

class SystemOfEquations(var equations : List<Equation>) {
    var solutions = mutableMapOf<String, Int>()

    init {
        if (!allBracketsSame()){
            equations = listOf()
        }
        Log.i("rovnica", "init systemOfEq: " + this.toString())
    }

    fun hasSolution() : Boolean = solutions.size > 0

    fun findVariables() = equations.flatMap { it.findAllVariables() }.toSet().toList()

    fun containsBracket() : Boolean = equations.any { it.containsBracket() }

    fun allBracketsSame() : Boolean {
        val brackets = equations.flatMap { it.findAllBrackets()}.toList()
        return brackets.all { it == brackets[0] }
    }

    fun findBracket() : Bracket? = equations.flatMap { it.findAllBrackets() }
        .toList().firstOrNull()

    fun solve() : Boolean{
        Log.i("rovnica", "solve system rovnic: " + this.toString())
        val variables = findVariables()
        solutions = mutableMapOf()
        for (v in variables)
            solutions[v] = 0

        when (variables.size){
            1 -> solve1Var(variables[0])
            2 -> solve2Var(variables)
            3 -> solve3Var(variables)
        }

        if (solutions.size == 0) {
            solutions = mutableMapOf()
            return false
        }
        setEquationSolutions()
        return true
    }

    private fun setEquationSolutions() {
        equations.forEach { it.setSolution(solutions) }
    }

    fun solve1Var(variable: String){
        for (i in 1..30){
            solutions[variable] = i
            setEquationSolutions()
            if ((0 until equations.size).all { ix ->
                    equations[ix].leftEqualsRight()})
                return
        }
        solutions = mutableMapOf()
    }

    fun solve2Var(variable: List<String>){
        for (i in 1..30){
            solutions[variable[0]] = i
            for (j in 1..30) {
                solutions[variable[1]] = j
                setEquationSolutions()
                if ((0 until equations.size).all { ix ->
                        equations[ix].leftEqualsRight()
                    })
                    return
            }
        }
        solutions = mutableMapOf()
    }

    fun solve3Var(variable: List<String>){
        for (i in 1..30){
            solutions[variable[0]] = i
            for (j in 1..30) {
                solutions[variable[1]] = j
                for (k in 1..30) {
                    solutions[variable[2]] = k
                    setEquationSolutions()
                    if ((0 until equations.size).all { ix ->
                            equations[ix].leftEqualsRight()
                        })
                        return
                }
            }
        }
        solutions = mutableMapOf()
    }


    override fun toString() : String = equations.map { it.toString() }
        .joinToString(separator = ", ")
}
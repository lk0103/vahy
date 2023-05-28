package com.example.bakalarka.equation

import android.util.Log
import com.example.vahy.equation.Addition

private const val MAX_SOLUTION = 30

private const val MIN_SOLUTION = 1

class SystemOfEquations(var equations : List<Equation>) {
    var solutions = mutableMapOf<String, Int>()

    init {
        if (!allBracketsSame()){
            equations = listOf()
        }
    }

    fun hasValidSolution() : Boolean = equations.none { it.isIncomplete() }
            && sameNumberVariablesAsEquations() && allVariablesDifferentValues()
            && !notOneSolution()


    fun notOneSolution() : Boolean{
        if (solutions.isEmpty())
            return true
        val originalSolution = solutions.toMutableMap()

        solutions.keys.forEach { key ->
            listOf(-1, 1).forEach {
                val newValue = (solutions[key] ?: 0) + it
                solutions[key] = newValue
                setSolutions(solutions)

                if (newValue <= MAX_SOLUTION && newValue >= MIN_SOLUTION &&
                    equations.all { it.leftEqualsRight() }){
                    solutions = originalSolution
                    setSolutions(originalSolution)
                    return true
                }
                solutions[key] = (solutions[key] ?: 0) - it
                setSolutions(solutions)
            }
        }
        return false
    }

    private fun sameNumberVariablesAsEquations() = solutions.size == equations.size

    private fun allVariablesDifferentValues() = solutions.values.toSet().size == solutions.size

    fun findVariables() = equations.flatMap { it.findAllVariables() }.toSet().toList()

    fun containsBracket() : Boolean = equations.any { it.containsBracket() }

    fun allBracketsSame() : Boolean {
        val brackets = equations.flatMap { it.findAllBrackets()}.toList()
        return brackets.all { it == brackets[0] }
    }

    fun findBracket() : Bracket? = equations.flatMap { it.findAllBrackets() }
        .toList().firstOrNull()

    fun setAllBracketInsides(bracketInside : Addition){
        equations.forEach { it.setAllBracketInsides(bracketInside) }
    }

    fun solve(){
        Log.i("rovnica", "solve system rovnic: " + this.toString())
        val variables = findVariables()
        solutions = mutableMapOf()
        for (v in variables)
            solutions[v] = 0

        when (variables.size){
            MIN_SOLUTION -> solve1Var(variables[0])
            2 -> solve2Var(variables)
            3 -> solve3Var(variables)
        }

        if (solutions.size == 0) {
            solutions = mutableMapOf()
            return
        }
        setEquationSolutions()
    }

    @JvmName("setSolutions1")
    fun setSolutions(s : MutableMap<String, Int>){
        solutions = s
        setEquationSolutions()
    }

    private fun setEquationSolutions() {
        equations.forEach { it.setSolution(solutions) }
    }

    fun solve1Var(variable: String){
        for (i in MIN_SOLUTION..MAX_SOLUTION){
            solutions[variable] = i
            setEquationSolutions()
            if (equations.all { it.leftEqualsRight()})
                return
        }
        solutions = mutableMapOf()
    }

    fun solve2Var(variable: List<String>){
        for (i in MIN_SOLUTION..MAX_SOLUTION){
            solutions[variable[0]] = i
            for (j in MIN_SOLUTION..MAX_SOLUTION) {
                solutions[variable[MIN_SOLUTION]] = j
                setEquationSolutions()
                if (equations.all { it.leftEqualsRight()}){
                    return
                }
            }
        }
        solutions = mutableMapOf()
    }

    fun solve3Var(variable: List<String>){
        for (i in MIN_SOLUTION..MAX_SOLUTION){
            solutions[variable[0]] = i
            for (j in MIN_SOLUTION..MAX_SOLUTION) {
                solutions[variable[MIN_SOLUTION]] = j
                for (k in MIN_SOLUTION..MAX_SOLUTION) {
                    solutions[variable[2]] = k
                    setEquationSolutions()
                    if (equations.all { it.leftEqualsRight()}){
                        return
                    }
                }
            }
        }
        solutions = mutableMapOf()
    }

    fun copy() : SystemOfEquations{
        val copy = SystemOfEquations(equations.map { it.copy() })
        copy.solutions = solutions
        return copy
    }

    override fun toString() : String = equations.map { it.toString() }
        .joinToString(separator = ", ")
}
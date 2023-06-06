package com.vahy.bakalarka.tasks

import android.content.Context
import com.vahy.bakalarka.equation.Bracket
import com.vahy.bakalarka.equation.Equation
import com.vahy.bakalarka.equation.SystemOfEquations
import com.vahy.bakalarka.objects.*
import com.vahy.vahy.ScalesView
import com.vahy.vahy.equation.Addition
import com.vahy.vahy.equation.Constant
import java.lang.Exception

class ScaleEquations(private val context: Context, private val scaleView: ScalesView) {

    private var equationSystem = SystemOfEquations(mutableListOf())
    private var previousEqSys = mutableListOf<Pair<SystemOfEquations, Bracket>>()
    private var equation = Equation(Addition(mutableListOf()), Addition(mutableListOf()))
    private var solutionBuild = mutableMapOf<String, Int>()
    private var screenVariableToStringVar = mutableMapOf<String, String>(
        Ball(context, 1)::class.toString() to "x",
        Cube(context, 1)::class.toString() to "y",
        Cylinder(context, 1)::class.toString() to "z",
    )
    private var maxNumberOfVariableTypes = 1

    fun setEqWithIndex(sysOfEq : SystemOfEquations, indexEq : Int){
        equation = sysOfEq.equations[indexEq]
        equationSystem = sysOfEq
    }

    fun setStoredSolutions(){
        equationSystem.setSolutions(solutionBuild)
    }

    fun storeFirstEquation(){
        if (previousEqSys.isEmpty())
            scaleView.storeEquation()
    }

    fun storeEquation(additionOfBrackets : Addition = Addition(mutableListOf())){
        val bracket = if (additionOfBrackets.addends.isEmpty()) Bracket(Addition(mutableListOf()))
        else additionOfBrackets.addends[0].copy() as Bracket
        previousEqSys.add(Pair(equationSystem.copy(), bracket))
    }

    fun createMappingForScreenVariablesForNewEq(): MutableList<String> {
        ///TOOTO SOM ZMENILA POVODNE LEN -> equation.findVariablesStrings()
        val variables = equationSystem.equations.flatMap { it.findVariablesStrings()}
            .toSet().toList()

        maxNumberOfVariableTypes = Math.min(variables.size, 3)
        screenVariableToStringVar = mutableMapOf()
        val variableScreenTypes: MutableList<String> = mutableListOf(
            Ball(context, 1)::class.toString(),
            Cube(context, 1)::class.toString(),
            Cylinder(context, 1)::class.toString()
        ).shuffled()
            .subList(0, maxNumberOfVariableTypes).toMutableList()
        (0 until maxNumberOfVariableTypes).forEach { i ->
            screenVariableToStringVar[variableScreenTypes[i]] = variables[i]
        }
        return variableScreenTypes
    }

    fun removePreviousEquation() = previousEqSys.removeLast()

    fun removeLastStoredEquation(){
        if (previousEqSys.isEmpty())
            return
        previousEqSys.removeLast()
    }

    fun exchangeSolutionsXandY() {
        val shuffledSolutions = equation.solutions.toMutableMap()
        val temp = shuffledSolutions["x"] ?: 0
        shuffledSolutions["x"] = shuffledSolutions["y"] ?: 0
        shuffledSolutions["y"] = temp

        if (!equation.leftEqualsRight()) {
            equation.setSolution(shuffledSolutions)
            if (equation.leftEqualsRight()) {
                equationSystem.setSolutions(shuffledSolutions)
            }
        }
    }

    fun divideScaleValue(container: ContainerForEquationBoxes, scaleView : ScalesView) {
        val clickedObject = scaleView.getClickedObject()
        if (clickedObject !is ScaleValue) return

        val eqCopy = equationSystem.copy()
        val originalBracket = equationSystem.findBracket()?.copy() as Bracket?
        val value = Math.abs((clickedObject as ScaleValue).evaluate())
        val signOfValue = if ((clickedObject as ScaleValue).evaluate() >= 0) 1 else -1
        try {
            container.removeDraggedObject(equationSystem, true, clickedObject)
            container.addObjBasedOnPolynom(
                Constant(signOfValue * (value / 2)), equationSystem
            )
            container.addObjBasedOnPolynom(
                Constant(signOfValue * (value - value / 2)), equationSystem
            )
        } catch (e: Exception) {
            scaleView.setEquation(
                eqCopy, getIndexEquation(),
                screenVariableToStringVar, originalBracket
            )
        }
    }

    fun unpackPackage(container: ContainerForEquationBoxes, scaleView : ScalesView) {
        val bracket = equationSystem.findBracket()
        if (bracket != null) {
            val eqCopy = equationSystem.copy()
            val originalBracket = bracket.copy() as Bracket?
            try {
                container.removeDraggedObject(equationSystem, true,
                                                scaleView.getClickedObject())
                bracket.polynom.addends.forEach { polynom ->
                    container.addObjBasedOnPolynom(polynom, equationSystem)
                }
            } catch (e: Exception) {
                scaleView.setEquation(eqCopy, getIndexEquation(),
                    screenVariableToStringVar, originalBracket)
            }
        }
    }

    fun previousEquation(){
        if (getSizePreviousEq() <= 0)
            return

        val (previous, bracket) = removePreviousEquation()
        scaleView.setEquation(previous, getIndexEquation(),
            getScreenVarToString(), bracket)
    }

    fun setEquationSystem(eqSys : SystemOfEquations = SystemOfEquations(mutableListOf())){
        equationSystem = eqSys
    }

    fun setPreviousEqSys(previous : MutableList<Pair<SystemOfEquations, Bracket>> = mutableListOf()){
        previousEqSys = previous
    }

    fun setEquation(eq : Equation = Equation(Addition(mutableListOf()), Addition(mutableListOf()))){
        equation = eq
    }

    fun setSolutionBuild(solution : MutableMap<String, Int> = mutableMapOf()){
        solutionBuild = solution
    }

    fun setScreenVarToString(mapping : MutableMap<String, String>){
        screenVariableToStringVar = mapping
    }

    fun setInsideBrackets(bracket: Addition){
        equationSystem.setAllBracketInsides(bracket)
    }

    fun getEquationSystem() = equationSystem

    fun getEquation() = equation

    fun getScreenVarToString() = screenVariableToStringVar

    fun getScaleVariables() : List<ScaleVariable> =
        listOf(Ball(context, 1), Cube(context, 1), Cylinder(context, 1))
            .filter { containsVariableMapScreenObj(it) }

    fun containsVariableMapScreenObj(screenObj : EquationObject) =
        screenVariableToStringVar.keys.contains(screenObj::class.toString())

    fun hasSolution(): Boolean{
        equationSystem.solve()
        return equationSystem.hasValidSolution()
    }

    fun defaultScreenVarToString(){
        screenVariableToStringVar = mutableMapOf<String, String>(
            Ball(context, 1)::class.toString() to "x",
            Cube(context, 1)::class.toString() to "y",
            Cylinder(context, 1)::class.toString() to "z",
        )
    }

    fun getSecondEquation() = equationSystem.equations[nextEquationIndex()]

    fun nextEquationIndex() = (getIndexEquation() + 1) % 2

    fun getIndexEquation() = equationSystem.equations.indexOf(equation)

    fun getSizeEqSystem() = equationSystem.equations.size

    fun getSizePreviousEq() = previousEqSys.size

    fun getSolutions() : Map<String, Int> {
        val solutions = equationSystem.solutions
        return screenVariableToStringVar.mapValues { solutions[it.value] ?: 0 }
    }

    fun getEquations() : List<Equation> = equationSystem.equations
}
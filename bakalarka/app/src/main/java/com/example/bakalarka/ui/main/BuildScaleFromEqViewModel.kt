package com.example.bakalarka.ui.main

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.bakalarka.MainActivity
import com.example.bakalarka.R
import com.example.bakalarka.equation.Equation
import com.example.bakalarka.equation.SystemOfEquations
import com.example.bakalarka.tasks.*
import com.example.vahy.ScalesView
import com.example.vahy.equation.Addition

class BuildScaleFromEqViewModel : ViewModel() {
    lateinit var mainActivity: MainActivity
    var switchBetweenTasks = SwitchingBetweenTasks()


    fun generateNewEq(clickedView: View, event: MotionEvent, scalesView : ScalesView) : Boolean{
        val res = checkSolution(clickedView, event, scalesView)
        if (res) {
            Log.i("levels", "generate new eq build")
            val (level, continueOnTask) = switchBetweenTasks.storeTargetLevelAndTask()
            switchToChosenLevel(level, continueOnTask, clickedView)
        }
        return true
    }


    fun generateEquation(scalesView: ScalesView, buildScaleView : BuildScaleFromEqView) {
        val level = switchBetweenTasks.getLevelInt()

        val prefsTaskInLevel =  mainActivity.applicationContext.getSharedPreferences(
            "taskInLevel", Context.MODE_PRIVATE)
        val taskInLevel = prefsTaskInLevel.getInt("taskInLevel", 0)

        val levelInfo = switchBetweenTasks.getLevel(level)
        val generator = levelInfo.tasks[taskInLevel].second

        var sysEq = SystemOfEquations(mutableListOf())
        while (generator != null && sysEq.solutions.size <= 0) {
            sysEq = generator.generateLinearEquationWithNaturalSolution()
            Log.i("generate", sysEq.toString())
            Log.i("generate", sysEq.solutions.toString())
        }


        scalesView.setEquation(
            SystemOfEquations(listOf(Equation(
            Addition(mutableListOf()), Addition(mutableListOf())
        ))), 0)
        scalesView.setBuildEquationTask(true, sysEq.containsBracket())
        Log.i("generate", "vygenerovana rovnica: " + sysEq.equations.toString())
        buildScaleView.setEquations(sysEq.equations)
    }


    private fun switchToChosenLevel(level: Int, continueOnTask: Int,
                                    view: View) {
        val taskType1 = switchBetweenTasks.getLevel(level).tasks[continueOnTask].first

        if (taskType1 == "build") {
            switchToBuildEqFragment(view)
        }

        if (taskType1 == "solve") {
            switchToSolveEqFragment(view)
        }
    }


    private fun switchToBuildEqFragment(view: View) {
        val navController = Navigation.findNavController(view)
        if (navController.currentDestination?.id == R.id.buildScaleFromEqFragment) {
            val action = BuildScaleFromEqFragmentDirections
                .actionBuildScaleFromEqFragmentSelf()
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun switchToSolveEqFragment(view: View) {
        val navController = Navigation.findNavController(view)
        if (navController.currentDestination?.id == R.id.buildScaleFromEqFragment) {
            val action = BuildScaleFromEqFragmentDirections
                .actionBuildScaleFromEqFragmentToSolveEquationFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }

    fun onTouch(clickedView: View, event: MotionEvent, scalesView: ScalesView) : Boolean{
        clickedView.onTouchEvent(event)
        if (clickedView is TaskMainMenuView && clickedView.replayTask){
            restoreOriginalEquation(clickedView, scalesView)
            return true
        }

        if (clickedView is TaskMainMenuView && clickedView.leaveTask){
            leaveTask(clickedView)
            return true
        }
        return true
    }

    fun restoreOriginalEquation(clickedView: TaskMainMenuView, scalesView: ScalesView){
        clickedView.replayTask = false
        scalesView.restoreOriginalEquation()
    }

    fun leaveTask(clickedView: TaskMainMenuView) {
        clickedView.replayTask = false
        switchToMainMenuFragment(clickedView)
    }

    private fun switchToMainMenuFragment(view : View) {
        val navController = Navigation.findNavController(view)
        if (navController.currentDestination?.id == R.id.buildScaleFromEqFragment) {
            val action = BuildScaleFromEqFragmentDirections
                .actionBuildScaleFromEqFragmentToMainMenu()
            Navigation.findNavController(view).navigate(action)
        }
    }

    fun checkSolution(clickedView: View, event: MotionEvent, scalesView: ScalesView) : Boolean{
        val checker = CheckBuildTaskSolution()
        checker.mainActivity = mainActivity
        return checker.checkSolution(clickedView, event, scalesView)
    }
}
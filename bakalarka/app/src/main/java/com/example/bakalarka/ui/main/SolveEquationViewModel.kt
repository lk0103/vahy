package com.example.bakalarka.ui.main

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.bakalarka.MainActivity
import com.example.bakalarka.R
import com.example.bakalarka.equation.SystemOfEquations
import com.example.bakalarka.tasks.*
import com.example.vahy.ScalesView


class SolveEquationViewModel : ViewModel() {

    lateinit var mainActivity: MainActivity
    var switchBetweenTasks = SwitchingBetweenTasks()


    fun generateNewEq(clickedView: View, event: MotionEvent, scalesView : ScalesView) : Boolean{
        val res = checkSolution(clickedView, event, scalesView)
        if (res) {
            Log.i("levels", "generate new eq solve")
            val (level, continueOnTask) = switchBetweenTasks.storeTargetLevelAndTask()
            switchToChosenLevel(level, continueOnTask, clickedView)
        }
        return true
    }

    fun generateEquation(scalesView: ScalesView, taskSolveEquationView : TaskSolveEquationView) {
        //generovanie rovnice
        val level = switchBetweenTasks.getLevelInt()

        val prefsTaskInLevel =  mainActivity.applicationContext.getSharedPreferences(
            "taskInLevel", Context.MODE_PRIVATE)
        val taskInLevel = prefsTaskInLevel.getInt("taskInLevel", 0)

        val levelInfo = switchBetweenTasks.getLevel(level)
        val generator = levelInfo.tasks[taskInLevel].second
        Log.i("levels", " generate  level: " + level + " taskInLevel" + taskInLevel)

        var sysEq = SystemOfEquations(mutableListOf())
        while (sysEq.solutions.size <= 0) {
            sysEq = generator.generateLinearEquationWithNaturalSolution()
            Log.i("generate", sysEq.toString())
            Log.i("generate", sysEq.solutions.toString())
        }

        scalesView.setEquation(sysEq, 0)
        scalesView.setTouchabilityOfOpenPackage(false)

        val variables = scalesView.getScaleVariables()
        Log.i("right", "variables: " + (scalesView.getScaleVariables()))
        taskSolveEquationView.setMapSolutions(variables)
    }

    fun onTouch(clickedView: View, event: MotionEvent, scalesView: ScalesView) : Boolean{
        clickedView.onTouchEvent(event)
        if (clickedView is TaskMainMenuView && clickedView.replayTask){
            restoreOriginalEquation(clickedView, scalesView)
            return true
        }

        if (clickedView is TaskMainMenuView && clickedView.leaveTask){
            leaveTask(clickedView, scalesView)
            return true
        }
        return true
    }

    fun restoreOriginalEquation(clickedView: TaskMainMenuView, scalesView: ScalesView){
        clickedView.replayTask = false
        scalesView.restoreOriginalEquation()
    }

    fun leaveTask(clickedView: TaskMainMenuView, scalesView: ScalesView) {
        clickedView.replayTask = false
        switchToMainMenuFragment(clickedView, scalesView)
    }


    fun checkSolution(clickedView: View, event: MotionEvent, scalesView: ScalesView) : Boolean{
        clickedView.onTouchEvent(event)
        if (clickedView !is TaskSolveEquationView || !clickedView.checkSolution){
            return false
        }
        clickedView.checkSolution = false
        Toast.makeText(mainActivity, if (scalesView.getSolutions() == clickedView.getUserSolutions()) "right"
                            else "wrong", Toast.LENGTH_SHORT).show()

        return scalesView.getSolutions() == clickedView.getUserSolutions()
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
        if (navController.currentDestination?.id == R.id.solveEquationFragment) {
            val action = SolveEquationFragmentDirections
                .actionSolveEquationFragmentToBuildScaleFromEqFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun switchToSolveEqFragment(view: View) {
        val navController = Navigation.findNavController(view)
        if (navController.currentDestination?.id == R.id.solveEquationFragment) {
            val action = SolveEquationFragmentDirections
                .actionSolveEquationFragmentSelf()
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun switchToMainMenuFragment(view : TaskMainMenuView, scalesView: ScalesView) {
        val navController = Navigation.findNavController(view)
        if (navController.currentDestination?.id == R.id.solveEquationFragment) {
            val action = SolveEquationFragmentDirections
                .actionSolveEquationFragmentToMainMenu()
            Navigation.findNavController(scalesView).navigate(action)
        }
    }
}
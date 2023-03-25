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
import kotlinx.android.synthetic.main.fragment_build_scale_from_eq.*


class SolveEquationViewModel : ViewModel() {

    lateinit var mainActivity: MainActivity
    var switchBetweenTasks = SwitchingBetweenTasks()
    var controlMenuTasks = ControlMenuTasks()

    @JvmName("setMainActivity1")
    fun setMainActivity(mainactivity : MainActivity){
        mainActivity = mainactivity
        switchBetweenTasks.mainActivity = mainactivity
        controlMenuTasks.mainActivity = mainactivity
    }


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
        while (sysEq.solutions.size <= 0 || sysEq.equations.any { ! it.leftEqualsRight() }) {
            if (generator is EquationsGenerator)
                sysEq = generator.generateLinearEquationWithNaturalSolution()
            else if (generator is System2EqGenerator)
                sysEq = generator.generateSystem2DiophantineEquations()
            Log.i("generate", sysEq.toString())
            Log.i("generate", sysEq.solutions.toString())
        }

        if (sysEq.equations.size == 2)
            scalesView.setSystem2EqTask(true)
        scalesView.hasPackage = sysEq.containsBracket()
        scalesView.setEquation(sysEq, 0)
        scalesView.setTouchabilityOfOpenPackage(false)
        val variables = scalesView.getScaleVariables()
        Log.i("right", "variables: " + (scalesView.getScaleVariables()))
        taskSolveEquationView.setMapSolutions(variables)
    }


    fun checkSolution(clickedView: View, event: MotionEvent, scalesView: ScalesView) : Boolean{
        clickedView.onTouchEvent(event)
        if (clickedView !is TaskSolveEquationView || !clickedView.checkSolution){
            return false
        }
        clickedView.checkSolution = false
        val rightAnswer = scalesView.getSolutions() == clickedView.getUserSolutions()
        scalesView.failSuccessShow(rightAnswer)
        if (clickedView is TaskSolveEquationView)
            clickedView.failSuccessShow()
        return rightAnswer
    }

    fun onTouch(clickedView: View, event: MotionEvent, scalesView: ScalesView) : Boolean{
        return controlMenuTasks.onTouch(clickedView, event, scalesView)
    }

    private fun switchToChosenLevel(level: Int, continueOnTask: Int,
                                    view: View) {
        val taskType1 = switchBetweenTasks.getLevel(level).tasks[continueOnTask].first

        if (taskType1 == "build") {
            switchSolveToBuildEqFragment(view)
        }

        if (taskType1 == "solve") {
            switchSolveToSolveEqFragment(view)
        }
    }

    private fun switchSolveToBuildEqFragment(view: View) {
        val navController = Navigation.findNavController(view)
        if (navController.currentDestination?.id == R.id.solveEquationFragment) {
            val action = SolveEquationFragmentDirections
                .actionSolveEquationFragmentToBuildScaleFromEqFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun switchSolveToSolveEqFragment(view: View) {
        val navController = Navigation.findNavController(view)
        if (navController.currentDestination?.id == R.id.solveEquationFragment) {
            val action = SolveEquationFragmentDirections
                .actionSolveEquationFragmentSelf()
            Navigation.findNavController(view).navigate(action)
        }
    }

}
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
import com.example.bakalarka.equation.Equation
import com.example.bakalarka.equation.SystemOfEquations
import com.example.bakalarka.tasks.*
import com.example.vahy.ScalesView
import com.example.vahy.equation.Addition

class BuildScaleFromEqViewModel : ViewModel() {
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
        if (clickedView is BuildScaleFromEqView && clickedView.screenTouchDisabled)
            return true
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
            if (generator is EquationsGenerator)
                sysEq = generator.generateLinearEquationWithNaturalSolution()
            else if (generator is System2EqGenerator)
                sysEq = generator.generateSystem2DiophantineEquations()
            Log.i("generate", sysEq.toString())
            Log.i("generate", sysEq.solutions.toString())
        }

        var emptySysOfEquations = SystemOfEquations(listOf(
            Equation(Addition(mutableListOf()), Addition(mutableListOf()))
        ))

        if (sysEq.equations.size == 2) {
            scalesView.setSystem2EqTask(true)
            emptySysOfEquations = SystemOfEquations(listOf(
                Equation(Addition(mutableListOf()), Addition(mutableListOf())),
                Equation(Addition(mutableListOf()), Addition(mutableListOf()))))
        }
        scalesView.setBuildEquationTask(true, sysEq.containsBracket())

        scalesView.setEquation(
            emptySysOfEquations, 0)

        Log.i("generate", "vygenerovana rovnica: " + sysEq.equations.toString())
        buildScaleView.setEquations(sysEq.equations)
    }

    fun checkSolution(clickedView: View, event: MotionEvent, scalesView: ScalesView) : Boolean{
        clickedView.onTouchEvent(event)
        if (clickedView !is BuildScaleFromEqView || !clickedView.checkSolution){
            return false
        }
        clickedView.checkSolution = false
        val rightAnswer = CheckBuildTaskSolution().checkSolution(clickedView, event, scalesView)
        scalesView.failSuccessShow(rightAnswer)
        if (clickedView is BuildScaleFromEqView)
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
            switchBuildToBuildEqFragment(view)
        }

        if (taskType1 == "solve") {
            switchBuildToSolveEqFragment(view)
        }
    }


    private fun switchBuildToBuildEqFragment(view: View) {
        val navController = Navigation.findNavController(view)
        if (navController.currentDestination?.id == R.id.buildScaleFromEqFragment) {
            val action = BuildScaleFromEqFragmentDirections
                .actionBuildScaleFromEqFragmentSelf()
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun switchBuildToSolveEqFragment(view: View) {
        val navController = Navigation.findNavController(view)
        if (navController.currentDestination?.id == R.id.buildScaleFromEqFragment) {
            val action = BuildScaleFromEqFragmentDirections
                .actionBuildScaleFromEqFragmentToSolveEquationFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }
}
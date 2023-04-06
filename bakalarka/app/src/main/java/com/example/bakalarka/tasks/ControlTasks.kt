package com.example.bakalarka.tasks

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.navigation.Navigation
import com.example.bakalarka.MainActivity
import com.example.bakalarka.R
import com.example.bakalarka.equation.SystemOfEquations
import com.example.bakalarka.ui.main.BuildScaleFromEqFragmentDirections
import com.example.bakalarka.ui.main.SolveEquationFragmentDirections
import com.example.bakalarka.ui.main.TaskMainMenuView
import com.example.vahy.ScalesView

class ControlTasks {
    lateinit var mainActivity: MainActivity

    fun generateSystemEq(): SystemOfEquations {
        val switchBetweenTasks = SwitchingBetweenTasks()
        switchBetweenTasks.mainActivity = mainActivity
        val level = switchBetweenTasks.getLevelInt()

        val prefsTaskInLevel = getSharedPreferences("taskInLevel")
        val taskInLevel = prefsTaskInLevel.getInt("taskInLevel", 0)

        val levelInfo = switchBetweenTasks.getLevel(level)
        val generator = levelInfo.tasks[taskInLevel].second
        Log.i("levels", " generate  level: " + level + " taskInLevel" + taskInLevel)

        var sysEq = SystemOfEquations(mutableListOf())
        while (sysEq.solutions.size <= 0 || sysEq.equations.any { !it.leftEqualsRight() }) {
            if (generator is EquationsGenerator)
                sysEq = generator.generateLinearEquationWithNaturalSolution()
            else if (generator is System2EqGenerator)
                sysEq = generator.generateSystem2DiophantineEquations()
            Log.i("generate", sysEq.toString())
            Log.i("generate", sysEq.solutions.toString())
        }
        return sysEq
    }

    fun onTouch(clickedView: View, event: MotionEvent, scalesView: ScalesView) : Boolean{
        clickedView.onTouchEvent(event)
//        if (clickedView is TaskMainMenuView && clickedView.restoreOriginalEquation){
//            restoreOriginalEquation(clickedView, scalesView)
//            return true
//        }

        if (clickedView is TaskMainMenuView && clickedView.previousEquation
            && !scalesView.isRotating){
            previousEquation(clickedView, scalesView)
            return true
        }

        if (clickedView is TaskMainMenuView && clickedView.leaveTask){
            leaveTask(clickedView)
            return true
        }
        return true
    }


    fun previousEquation(clickedView: TaskMainMenuView, scalesView: ScalesView){
        clickedView.previousEquation = false
//        clickedView.restoreOriginalEquation = false
        scalesView.previousEquation()
    }

//    fun restoreOriginalEquation(clickedView: TaskMainMenuView, scalesView: ScalesView){
//        clickedView.restoreOriginalEquation = false
//        clickedView.previousEquation = false
//        scalesView.restoreOriginalEquation()
//    }

    fun leaveTask(clickedView: TaskMainMenuView) {
        clickedView.previousEquation = false
        val navController = Navigation.findNavController(clickedView)
        if (navController.currentDestination?.id == R.id.buildScaleFromEqFragment) {
            switchBuildToMainMenuFragment(clickedView)
        }
        else if (navController.currentDestination?.id == R.id.solveEquationFragment) {
            switchSolveToMainMenuFragment(clickedView)
        }
    }


    private fun switchBuildToMainMenuFragment(view : View) {
        val navController = Navigation.findNavController(view)
        if (navController.currentDestination?.id == R.id.buildScaleFromEqFragment) {
            val action = BuildScaleFromEqFragmentDirections
                .actionBuildScaleFromEqFragmentToMainMenu()
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun switchSolveToMainMenuFragment(view : TaskMainMenuView) {
        val navController = Navigation.findNavController(view)
        if (navController.currentDestination?.id == R.id.solveEquationFragment) {
            val action = SolveEquationFragmentDirections
                .actionSolveEquationFragmentToMainMenu()
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun getSharedPreferences(name : String) =
        mainActivity.applicationContext.getSharedPreferences(
            name, Context.MODE_PRIVATE
        )
}
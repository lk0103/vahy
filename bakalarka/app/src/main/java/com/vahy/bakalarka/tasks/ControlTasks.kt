package com.vahy.bakalarka.tasks

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.vahy.bakalarka.MainActivity
import com.vahy.bakalarka.R
import com.vahy.bakalarka.equation.SystemOfEquations
import com.vahy.bakalarka.ui.main.*
import com.vahy.vahy.ScalesView

class ControlTasks {
    lateinit var mainActivity: MainActivity

    fun generateSystemEq(): SystemOfEquations {
        val switchBetweenTasks = SwitchingBetweenTasks()
        switchBetweenTasks.mainActivity = mainActivity

        if (mainActivity.getCreatedEquation() != null){
            return mainActivity.getCreatedEquation()!!
        }

        var level = switchBetweenTasks.getLevelInt()
        level = if(level < 1) 1 else level

        val prefsTaskInLevel = getSharedPreferences(prefsNameTaskInLevel)
        val taskInLevel = prefsTaskInLevel.getInt(prefsNameTaskInLevel, 0)

        val levelInfo = switchBetweenTasks.getLevel(level)
        val generator = levelInfo.getTaskType(taskInLevel)
        Log.i("levels", " generate  level: " + level + " taskInLevel" + taskInLevel)

        var sysEq = SystemOfEquations(mutableListOf())
        while (sysEq.solutions.size <= 0 || sysEq.equations.any { !it.leftEqualsRight() }) {
            if (generator is EquationsGenerator)
                sysEq = generator.generateEquationWithNaturalSolution()
            else if (generator is System2EqGenerator)
                sysEq = generator.generateSystem2DiophantineEquations()
        }
        return sysEq
    }

    fun onTouchMainMenu(clickedView: View, event: MotionEvent, scalesView: ScalesView) : Boolean{
        clickedView.onTouchEvent(event)
        if (clickedView !is TaskMenuView)
            return true

        mainActivity.deleteCreatedEquation()

        if (clickedView.previousEquation && !scalesView.isRotating){
            previousEquation(clickedView, scalesView)
            return true
        }

        if (clickedView.leaveTask){
            leaveTask(clickedView)
            return true
        }

        if (clickedView.goBackToChooseType){
            goBackToChooseTypeCreateTask(clickedView)
            return true
        }

        if (clickedView.createTask)
            switchToCreateTask(clickedView)

        return true
    }


    fun previousEquation(clickedView: TaskMenuView, scalesView: ScalesView){
        clickedView.previousEquation = false
        scalesView.previousEquation()
    }


    fun leaveTask(clickedView: TaskMenuView) {
        clickedView.leaveTask = false
        val navController = Navigation.findNavController(clickedView)
        if (navController.currentDestination?.id == R.id.buildScaleFromEqFragment) {
            switchBuildToMainMenuFragment(clickedView)
        }
        else if (navController.currentDestination?.id == R.id.solveEquationFragment) {
            switchSolveToMainMenuFragment(clickedView)
        }
        else if (navController.currentDestination?.id == R.id.createTaskFragment) {
            switchCreateTaskToMainMenuFragment(clickedView)
        }
        else if (navController.currentDestination?.id == R.id.chooseTypeCreateTaskFragment) {
            switchChooseCreateTaskToMainMenuFragment(clickedView)
        }
    }

    private fun goBackToChooseTypeCreateTask(clickedView: TaskMenuView) {
        clickedView.goBackToChooseType = false
        val navController = Navigation.findNavController(clickedView)
        if (navController.currentDestination?.id == R.id.createTaskFragment) {
            switchCreateTaskToChooseTypeCreateTask(clickedView)
        }
    }

    private fun switchToCreateTask(clickedView: TaskMenuView) {
        clickedView.createTask = false
        val navController = Navigation.findNavController(clickedView)
        if (navController.currentDestination?.id == R.id.buildScaleFromEqFragment) {
            switchBuildToChooseTypeCreateTaskFragment(clickedView)
        }
        else if (navController.currentDestination?.id == R.id.solveEquationFragment) {
            switchSolveToCreateTaskFragment(clickedView)
        }
    }


    private fun switchBuildToMainMenuFragment(view : View) {
        val navController = Navigation.findNavController(view)
        if (navController.currentDestination?.id == R.id.buildScaleFromEqFragment) {
            val action = BuildScaleFragmentDirections
                .actionBuildScaleFromEqFragmentToMainMenu()
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun switchSolveToMainMenuFragment(view : TaskMenuView) {
        val navController = Navigation.findNavController(view)
        if (navController.currentDestination?.id == R.id.solveEquationFragment) {
            val action = SolveEquationFragmentDirections
                .actionSolveEquationFragmentToMainMenu()
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun switchCreateTaskToMainMenuFragment(view: View) {
        var navController : NavController? = null
        try {
            navController = Navigation.findNavController(view)
        }catch (e : java.lang.Exception){
            return
        }

        if (navController.currentDestination?.id == R.id.createTaskFragment) {
            val action = CreateTaskFragmentDirections
                .actionCreateTaskFragmentToMainMenuFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }


    private fun switchChooseCreateTaskToMainMenuFragment(view: View) {
        var navController : NavController? = null
        try {
            navController = Navigation.findNavController(view)
        }catch (e : java.lang.Exception){
            return
        }

        if (navController.currentDestination?.id == R.id.chooseTypeCreateTaskFragment) {
            val action = ChooseTypeCreateTaskFragmentDirections
                .actionChooseTypeCreateTaskFragmentToMainMenuFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }


    private fun switchSolveToCreateTaskFragment(view: View) {
        var navController : NavController? = null
        try {
            navController = Navigation.findNavController(view)
        }catch (e : java.lang.Exception){
            return
        }

        if (navController.currentDestination?.id == R.id.solveEquationFragment) {
            val action = SolveEquationFragmentDirections
                .actionSolveEquationFragmentToCreateTaskFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun switchBuildToChooseTypeCreateTaskFragment(view: View) {
        var navController : NavController? = null
        try {
            navController = Navigation.findNavController(view)
        }catch (e : java.lang.Exception){
            return
        }

        if (navController.currentDestination?.id == R.id.buildScaleFromEqFragment) {
            val action = BuildScaleFragmentDirections
                .actionBuildScaleFromEqFragmentToChooseTypeCreateTaskFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun switchCreateTaskToChooseTypeCreateTask(view: View) {
        var navController : NavController? = null
        try {
            navController = Navigation.findNavController(view)
        }catch (e : java.lang.Exception){
            return
        }

        if (navController.currentDestination?.id == R.id.createTaskFragment) {
            val action = CreateTaskFragmentDirections
                .actionCreateTaskFragmentToChooseTypeCreateTaskFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun getSharedPreferences(name : String) =
        mainActivity.applicationContext.getSharedPreferences(
            name, Context.MODE_PRIVATE
        )
}
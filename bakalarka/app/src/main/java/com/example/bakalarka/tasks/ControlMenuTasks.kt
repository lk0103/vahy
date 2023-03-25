package com.example.bakalarka.tasks

import android.view.MotionEvent
import android.view.View
import androidx.navigation.Navigation
import com.example.bakalarka.MainActivity
import com.example.bakalarka.R
import com.example.bakalarka.ui.main.BuildScaleFromEqFragmentDirections
import com.example.bakalarka.ui.main.SolveEquationFragmentDirections
import com.example.bakalarka.ui.main.TaskMainMenuView
import com.example.vahy.ScalesView

class ControlMenuTasks {
    lateinit var mainActivity: MainActivity

    fun onTouch(clickedView: View, event: MotionEvent, scalesView: ScalesView) : Boolean{
        clickedView.onTouchEvent(event)
//        if (clickedView is TaskMainMenuView && clickedView.restoreOriginalEquation){
//            restoreOriginalEquation(clickedView, scalesView)
//            return true
//        }

        if (clickedView is TaskMainMenuView && clickedView.previousEquation){
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
}
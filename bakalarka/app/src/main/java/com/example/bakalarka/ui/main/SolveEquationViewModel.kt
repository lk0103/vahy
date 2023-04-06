package com.example.bakalarka.ui.main

import android.os.CountDownTimer
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.bakalarka.MainActivity
import com.example.bakalarka.R
import com.example.bakalarka.tasks.*
import com.example.vahy.ScalesView
import kotlinx.android.synthetic.main.fragment_build_scale_from_eq.*


class SolveEquationViewModel : ViewModel() {

    lateinit var mainActivity: MainActivity
    var switchBetweenTasks = SwitchingBetweenTasks()
    var controlMenuTasks = ControlTasks()

    @JvmName("setMainActivity1")
    fun setMainActivity(mainactivity : MainActivity){
        mainActivity = mainactivity
        switchBetweenTasks.mainActivity = mainactivity
        controlMenuTasks.mainActivity = mainactivity
    }


    fun generateNewEq(clickedView: View, event: MotionEvent, scalesView : ScalesView) : Boolean{
        if (checkSolution(clickedView, event, scalesView)) {
            waitForSuccessAnimation(scalesView, clickedView)
        }
        return true
    }

    private fun waitForSuccessAnimation(scalesView: ScalesView, clickedView: View) {
        Log.i("levels", "generate new eq solve")
        val (level, continueOnTask) = switchBetweenTasks.storeTargetLevelAndTask()

        object : CountDownTimer(2000, 20) {
            override fun onTick(p0: Long) {
                if (!scalesView.screenTouchDisabled) {
                    this.cancel()
                    onFinish()
                }
            }

            override fun onFinish() {
                switchToChosenLevel(level, continueOnTask, clickedView)
            }
        }.start()
    }

    fun generateEquation(scalesView: ScalesView, taskSolveEquationView : TaskSolveEquationView) {
        //generovanie rovnice
        val sysEq = controlMenuTasks.generateSystemEq()

        if (sysEq.equations.size == 2)
            scalesView.setSystem2EqTask(true)

        scalesView.hasPackage = sysEq.containsBracket()
        scalesView.setHasBalloon(sysEq.toString().contains("-"))

        scalesView.setEquation(sysEq, 0)
        scalesView.setTouchabilityOfOpenPackage(false)

        val variables = scalesView.getScaleVariables()
        Log.i("right", "variables: " + (scalesView.getScaleVariables()))
        taskSolveEquationView.setMapSolutions(variables)

        switchBetweenTasks.showNewLevelUnlockedMessage(scalesView)
    }


    fun checkSolution(clickedView: View, event: MotionEvent, scalesView: ScalesView) : Boolean{
        if (clickedView is TaskSolveEquationView && !scalesView.screenTouchDisabled &&
            clickedView.screenTouchDisabled)
            clickedView.cancelMessage()

        clickedView.onTouchEvent(event)
        if (clickedView !is TaskSolveEquationView || !clickedView.checkSolution){
            return false
        }

        clickedView.checkSolution = false
        val rightAnswer = scalesView.getSolutions() == clickedView.getUserSolutions()
        scalesView.failSuccessShow(rightAnswer)
        if (clickedView is TaskSolveEquationView) {
            clickedView.failSuccessShow()
        }
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
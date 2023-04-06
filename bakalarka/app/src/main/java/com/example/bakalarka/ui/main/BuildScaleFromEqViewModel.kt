package com.example.bakalarka.ui.main

import android.os.CountDownTimer
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
    var controlMenuTasks = ControlTasks()

    @JvmName("setMainActivity1")
    fun setMainActivity(mainactivity : MainActivity){
        mainActivity = mainactivity
        switchBetweenTasks.mainActivity = mainactivity
        controlMenuTasks.mainActivity = mainactivity
    }

    fun changeEquation(clickedView: View, event: MotionEvent, buildScaleView: BuildScaleFromEqView) : Boolean{
        clickedView.onTouchEvent(event)

        if (clickedView !is ScalesView || !clickedView.isSystemOf2Eq){
            return false
        }

        buildScaleView.setIndexMarked(clickedView.getIndexEquation())
        return true
    }

    fun generateNewEq(clickedView: View, event: MotionEvent, scalesView : ScalesView) : Boolean{
        if (checkSolution(clickedView, event, scalesView)) {
            waitForSuccessAnimation(scalesView, clickedView)
        }
        return true
    }

    private fun waitForSuccessAnimation(scalesView: ScalesView, clickedView: View) {
        Log.i("levels", "generate new eq build")
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


    fun generateEquation(scalesView: ScalesView, buildScaleView : BuildScaleFromEqView) {
        val sysEq = controlMenuTasks.generateSystemEq()

        var emptySysOfEquations = SystemOfEquations(listOf(
            Equation(Addition(mutableListOf()), Addition(mutableListOf()))
        ))

        if (sysEq.equations.size == 2) {
            scalesView.setSystem2EqTask(true)
            emptySysOfEquations = SystemOfEquations(listOf(
                Equation(Addition(mutableListOf()), Addition(mutableListOf())),
                Equation(Addition(mutableListOf()), Addition(mutableListOf()))))
        }

        scalesView.setHasBalloon(sysEq.toString().contains("-"))
        scalesView.setBuildEquationTask(true, sysEq)

        scalesView.setEquation(
            emptySysOfEquations, 0)

        Log.i("generate", "vygenerovana rovnica: " + sysEq.equations.toString())
        buildScaleView.setEquations(sysEq.equations)

        switchBetweenTasks.showNewLevelUnlockedMessage(scalesView)
    }

    private fun checkSolution(clickedView: View, event: MotionEvent, scalesView: ScalesView) : Boolean{
        if (clickedView is BuildScaleFromEqView && !scalesView.screenTouchDisabled &&
                clickedView.screenTouchDisabled)
            clickedView.cancelMessage()

        clickedView.onTouchEvent(event)

        if (clickedView !is BuildScaleFromEqView ||
            (!clickedView.checkSolution && clickedView.indexTouchedEquation == -1
                    && scalesView.isSystemOf2Eq)){
            return false
        }
        if (switchEquations(clickedView, scalesView)) return false

        return check(clickedView, event, scalesView)
    }

    private fun check(clickedView: BuildScaleFromEqView, event: MotionEvent,
                      scalesView: ScalesView): Boolean {
        if (!clickedView.checkSolution)
            return false

        clickedView.checkSolution = false
        val rightAnswer = CheckBuildTaskSolution().checkSolution(clickedView, event, scalesView)
        scalesView.failSuccessShow(rightAnswer)

        if (clickedView is BuildScaleFromEqView) {
            clickedView.failSuccessShow()
        }
        return rightAnswer
    }

    private fun switchEquations(clickedView: BuildScaleFromEqView, scalesView: ScalesView): Boolean {
        if (clickedView.indexTouchedEquation > -1 && scalesView.isSystemOf2Eq) {
            scalesView.switchToEqWithIndex(clickedView.indexTouchedEquation)
            clickedView.setIndexMarked(clickedView.indexTouchedEquation)
            clickedView.indexTouchedEquation = -1
            return true
        }
        return false
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
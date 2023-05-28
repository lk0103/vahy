package com.example.bakalarka.ui.main

import android.os.CountDownTimer
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.bakalarka.MainActivity
import com.example.bakalarka.R
import com.example.bakalarka.tasks.*
import com.example.vahy.ScalesView


class SolveEquationViewModel : ViewModel() {

    lateinit var mainActivity: MainActivity
    lateinit var scalesView: ScalesView
    lateinit var solveEquationMenuView: SolveEquationMenuView
    lateinit var taskMenuView: TaskMenuView
    var switchBetweenTasks = SwitchingBetweenTasks()
    var controlMenuTasks = ControlTasks()

    @JvmName("setMainActivity1")
    fun setMainActivity(mainactivity : MainActivity, ScalesView : ScalesView,
                        SolveEquationMenuView: SolveEquationMenuView,
                        TaskMenuView : TaskMenuView){
        mainActivity = mainactivity
        switchBetweenTasks.mainActivity = mainactivity
        controlMenuTasks.mainActivity = mainactivity
        scalesView = ScalesView
        solveEquationMenuView = SolveEquationMenuView
        taskMenuView = TaskMenuView
    }


    fun generateNewEq(clickedView: View, event: MotionEvent) : Boolean{
        if (checkSolution(clickedView, event)) {
            waitForSuccessAnimation(clickedView)
        }
        return true
    }

    private fun waitForSuccessAnimation(clickedView: View) {
        val (level, continueOnTask) = switchBetweenTasks.storeTargetLevelAndTask()

        object : CountDownTimer(1280, 20) {
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

    fun generateEquation() {

        switchBetweenTasks.setTaskMainMenu(taskMenuView)

        val sysEq = controlMenuTasks.generateSystemEq()

        scalesView.setHasPackage(sysEq.containsBracket())
        scalesView.setHasBalloon(sysEq.toString().contains("-"))

        scalesView.setSystem2EqTask(sysEq.equations.size == 2)

        scalesView.setEquation(sysEq, 0, mainActivity.getVariableToScreenObjects())
        Log.i("generate", "vygenerovana rovnica: " + sysEq.equations.toString())
        Log.i("generate", "solutions: " + sysEq.solutions.toString())

        val variables = scalesView.getScaleVariables()
        solveEquationMenuView.setMapSolutions(variables)

        switchBetweenTasks.showNewLevelUnlockedMessage(scalesView)
        scalesView.unlockedLevel()
    }


    fun checkSolution(clickedView: View, event: MotionEvent) : Boolean{
        if (clickedView is SolveEquationMenuView && !scalesView.screenTouchDisabled &&
            clickedView.screenTouchDisabled)
            clickedView.cancelMessage()

        clickedView.onTouchEvent(event)
        if (clickedView !is SolveEquationMenuView || !clickedView.checkSolution){
            return false
        }

        clickedView.checkSolution = false
        val rightAnswer = scalesView.getSolutions() == clickedView.getUserSolutions()
        scalesView.failSuccessShow(rightAnswer)
        if (clickedView is SolveEquationMenuView) {
            clickedView.failSuccessShow()
        }
        return rightAnswer
    }

    fun onTouch(clickedView: View, event: MotionEvent) : Boolean{
        return controlMenuTasks.onTouchMainMenu(clickedView, event, scalesView)
    }

    private fun switchToChosenLevel(level: Int, continueOnTask: Int,
                                    view: View) {
        val taskType1 = switchBetweenTasks.getLevel(level).tasks[continueOnTask].first

        if (taskType1 == buildTypeTask) {
            switchSolveToBuildEqFragment(view)
        }

        if (taskType1 == solveTaskType) {
            switchSolveToSolveEqFragment()
        }
    }

    private fun switchSolveToBuildEqFragment(view: View) {
        var navController : NavController? = null
        try {
            navController = Navigation.findNavController(view)
        }catch (e : java.lang.Exception){
            return
        }

        if (navController.currentDestination?.id == R.id.solveEquationFragment) {
            val action = SolveEquationFragmentDirections
                .actionSolveEquationFragmentToBuildScaleFromEqFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun switchSolveToSolveEqFragment() {
        mainActivity.deleteCreatedEquation()
        generateEquation()
    }

}
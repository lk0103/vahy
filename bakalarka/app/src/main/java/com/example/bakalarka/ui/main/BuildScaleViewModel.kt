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
import com.example.bakalarka.equation.Equation
import com.example.bakalarka.equation.SystemOfEquations
import com.example.bakalarka.tasks.*
import com.example.vahy.ScalesView
import com.example.vahy.equation.Addition

class BuildScaleViewModel : ViewModel() {
    lateinit var mainActivity: MainActivity
    lateinit var scalesView: ScalesView
    lateinit var buildScaleMenuView: BuildScaleMenuView
    lateinit var taskMenuView: TaskMenuView
    var switchBetweenTasks = SwitchingBetweenTasks()
    var controlMenuTasks = ControlTasks()



    @JvmName("setMainActivity1")
    fun setMainActivity(mainactivity : MainActivity, ScalesView : ScalesView,
                        BuildScaleMenuView: BuildScaleMenuView,
                        TaskMenuView : TaskMenuView){
        mainActivity = mainactivity
        switchBetweenTasks.mainActivity = mainactivity
        controlMenuTasks.mainActivity = mainactivity
        scalesView = ScalesView
        buildScaleMenuView = BuildScaleMenuView
        taskMenuView = TaskMenuView
    }

    fun changeEquation(clickedView: View, event: MotionEvent) : Boolean{
        clickedView.onTouchEvent(event)

        if (clickedView !is ScalesView || !clickedView.isSystemOf2Eq){
            return true
        }

        buildScaleMenuView.setIndexMarked(clickedView.getIndexEquation())
        return true
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
                    scalesView.cancelShownResultMessage()
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

        scalesView.defaultScale()
        switchBetweenTasks.setTaskMainMenu(taskMenuView)

        val sysEq = controlMenuTasks.generateSystemEq()

        var emptySysOfEquations = SystemOfEquations(listOf(
            Equation(Addition(mutableListOf()), Addition(mutableListOf()))
        ))

        scalesView.setSystem2EqTask(sysEq.equations.size == 2)
        if (sysEq.equations.size == 2) {
            emptySysOfEquations = SystemOfEquations(listOf(
                Equation(Addition(mutableListOf()), Addition(mutableListOf())),
                Equation(Addition(mutableListOf()), Addition(mutableListOf()))))
        }

        scalesView.setHasBalloon(sysEq.toString().contains("-"))
        scalesView.setBuildEquationTask(true, sysEq, mainActivity.getVariableToScreenObjects())

        scalesView.setEquation(
            emptySysOfEquations, 0)

        Log.i("generate", "vygenerovana rovnica: " + sysEq.equations.toString())
        Log.i("generate", "solutions: " + sysEq.solutions.toString())
        buildScaleMenuView.setEquations(sysEq.equations)

        switchBetweenTasks.showNewLevelUnlockedMessage(scalesView)
        scalesView.unlockedLevel()
    }

    private fun checkSolution(clickedView: View, event: MotionEvent) : Boolean{
        if (clickedView is BuildScaleMenuView && !scalesView.screenTouchDisabled &&
                clickedView.screenTouchDisabled)
            clickedView.cancelMessage()

        clickedView.onTouchEvent(event)

        if (clickedView !is BuildScaleMenuView ||
            (!clickedView.checkSolution && clickedView.indexTouchedEquation == -1
                    && scalesView.isSystemOf2Eq)){
            return false
        }
        if (switchEquations(clickedView)) return false

        return check(clickedView, event)
    }

    private fun check(clickedView: BuildScaleMenuView, event: MotionEvent): Boolean {
        if (!clickedView.checkSolution)
            return false

        clickedView.checkSolution = false
        val rightAnswer = CheckBuildTaskSolution().checkSolution(clickedView, event, scalesView)
        scalesView.failSuccessShow(rightAnswer)

        if (clickedView is BuildScaleMenuView) {
            clickedView.failSuccessShow()
        }
        return rightAnswer
    }

    private fun switchEquations(clickedView: BuildScaleMenuView): Boolean {
        if (clickedView.indexTouchedEquation > -1 && scalesView.isSystemOf2Eq) {
            scalesView.switchToEqWithIndex(clickedView.indexTouchedEquation)
            clickedView.setIndexMarked(clickedView.indexTouchedEquation)
            clickedView.indexTouchedEquation = -1
            return true
        }
        return false
    }

    fun onTouch(clickedView: View, event: MotionEvent) : Boolean{
        return controlMenuTasks.onTouchMainMenu(clickedView, event, scalesView)
    }

    private fun switchToChosenLevel(level: Int, continueOnTask: Int,
                                    view: View) {
        val taskType1 = switchBetweenTasks.getLevel(level).tasks[continueOnTask].first

        if (taskType1 == buildTypeTask) {
            switchBuildToBuildEqFragment()
        }

        if (taskType1 == solveTaskType) {
            switchBuildToSolveEqFragment(view)
        }
    }


    private fun switchBuildToBuildEqFragment() {
        mainActivity.deleteCreatedEquation()
        generateEquation()
        buildScaleMenuView.changeSizeScreenObjects()
        buildScaleMenuView.invalidate()
    }

    private fun switchBuildToSolveEqFragment(view: View) {
        var navController : NavController? = null
        try {
            navController = Navigation.findNavController(view)
        }catch (e : java.lang.Exception){
            return
        }

        if (navController.currentDestination?.id == R.id.buildScaleFromEqFragment) {
            val action = BuildScaleFragmentDirections
                .actionBuildScaleFromEqFragmentToSolveEquationFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }
}
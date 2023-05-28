package com.example.bakalarka.ui.main

import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.bakalarka.MainActivity
import com.example.bakalarka.R
import com.example.bakalarka.equation.Bracket
import com.example.bakalarka.equation.Equation
import com.example.bakalarka.equation.SystemOfEquations
import com.example.bakalarka.tasks.ControlTasks
import com.example.bakalarka.tasks.SwitchingBetweenTasks
import com.example.vahy.ScalesView
import com.example.vahy.equation.Addition
import com.example.vahy.equation.Constant
import com.example.vahy.equation.Multiplication
import com.example.vahy.equation.Variable

class CreateTaskViewModel : ViewModel() {
    private lateinit var mainActivity: MainActivity
    private lateinit var scalesView: ScalesView
    private lateinit var taskMenuView: TaskMenuView
    private lateinit var createTaskMenuView: CreateTaskMenuView

    var switchBetweenTasks = SwitchingBetweenTasks()
    var controlMenuTasks = ControlTasks()

    private var numberOfStoredEquations = -1
    private var level = -1

    private val setUpEquationLevel1 = SystemOfEquations(listOf(
        Equation(
            Addition(mutableListOf(Variable("x"))),
            Addition(mutableListOf(Constant(9)))
        )
    ))

    private val setUpEquationLevel2 = SystemOfEquations(listOf(
        Equation(
            Addition(mutableListOf(Multiplication(Variable("x"), Constant(2)),
                Constant(-3))),
            Addition(mutableListOf(Constant(3),
                Bracket(Addition(mutableListOf(Variable("x"), Constant(4)))))
            )
        )
    ))

    private val setUpEquationLevel4 = SystemOfEquations(listOf(
        Equation(
            Addition(mutableListOf(Variable("x"), Variable("y"))),
            Addition(mutableListOf(Constant(6)))
        ),
        Equation(
            Addition(mutableListOf(
                Multiplication(Variable("x"), Constant(2)), Constant(4))
            ),
            Addition(mutableListOf(Multiplication(Variable("y"), Constant(2))))
        )
    ))

    private val setUpEquations = mutableListOf(setUpEquationLevel1, setUpEquationLevel2,
        setUpEquationLevel2, setUpEquationLevel4)

    private val emptySysOfEquations = SystemOfEquations(listOf(
        Equation(Addition(mutableListOf()), Addition(mutableListOf()))
    ))

    private val emptySysOf2Equations = SystemOfEquations(listOf(
        Equation(Addition(mutableListOf()), Addition(mutableListOf())),
        Equation(Addition(mutableListOf()), Addition(mutableListOf()))
    ))


    @JvmName("setMainActivity1")
    fun setMainActivity(mainactivity : MainActivity, ScalesView : ScalesView,
                        TaskMenuView : TaskMenuView,
                        CreateTaskMenu : CreateTaskMenuView){
        mainActivity = mainactivity
        switchBetweenTasks.mainActivity = mainactivity
        controlMenuTasks.mainActivity = mainactivity
        scalesView = ScalesView
        taskMenuView = TaskMenuView
        createTaskMenuView = CreateTaskMenu
    }

    fun generateEquation() {
        scalesView.defaultScale()

        level = switchBetweenTasks.getLevelInt()

        if (level == 3 && mainActivity.getTypeCreateTask() >= 2) {
            setUpEquations[level - 1] = setUpEquationLevel4
        }

        val sysEq = setUpEquations[level - 1]
        sysEq.solve()

        val emptySysOfEq = if (sysEq.equations.size == 2) emptySysOf2Equations
                                else emptySysOfEquations

        scalesView.setSystem2EqTask(sysEq.equations.size == 2)
        scalesView.setHasBalloon(sysEq.toString().contains("-"))
        scalesView.setBuildEquationTask(true, sysEq)
        scalesView.solutionBuild = mutableMapOf()

        scalesView.setEquation(emptySysOfEq, 0)
        scalesView.isCreateTask = true

        if (level == 3){
            taskMenuView.setMainMenuInCreateTaskForBuild()
            createTaskMenuView.setCreateTaskForBuild(emptySysOfEq.equations)
        }

        changeIndicatorOfRightEquation()
    }


    fun onTouchMainMenu(event: MotionEvent) : Boolean{
        controlMenuTasks.onTouchMainMenu(taskMenuView, event, scalesView)
        changeIndicatorOfRightEquation()
        return true
    }

    fun onTouchCreateTaskMenu(event: MotionEvent) : Boolean{
        createTaskMenuView.onTouchEvent(event)

        if (!createTaskMenuView.continueToSolve || scalesView.screenTouchDisabled)
            return true

        if (!scalesView.hasSolution()){
            scalesView.earthquakeAnimation()
            return true
        }

        mainActivity.setCreatedEquation(scalesView.getSystemOfEquations(),
                                        scalesView.getScreenVarToStringObj())

        if (level == 3){
            switchCreateTaskToBuildFragment(createTaskMenuView)
        }else {
            switchCreateTaskToSolveFragment(createTaskMenuView)
        }

        switchEquations()
        return true
    }

    fun onTouchScaleView(event: MotionEvent) : Boolean{
        scalesView.onTouchEvent(event)
        changeIndicatorOfRightEquation()

        createTaskMenuView.setIndexMarked(scalesView.getIndexEquation())
        return true
    }

    private fun switchEquations(): Boolean {
        if (createTaskMenuView.indexTouchedEquation > -1 && scalesView.isSystemOf2Eq) {
            scalesView.switchToEqWithIndex(createTaskMenuView.indexTouchedEquation)
            createTaskMenuView.setIndexMarked(createTaskMenuView.indexTouchedEquation)
            createTaskMenuView.indexTouchedEquation = -1
            return true
        }
        return false
    }

    private fun changeIndicatorOfRightEquation() {
        val newSizeStoredEq = scalesView.sizeStoredPreviousEquations()
        if (newSizeStoredEq != numberOfStoredEquations) {
            numberOfStoredEquations = newSizeStoredEq

            if (level == 3)
                createTaskMenuView.changeEquationBoxes(scalesView.getEquations())
        }
    }


    private fun switchCreateTaskToSolveFragment(view: View) {
        var navController : NavController? = null
        try {
            navController = Navigation.findNavController(view)
        }catch (e : java.lang.Exception){
            return
        }

        if (navController.currentDestination?.id == R.id.createTaskFragment) {
            val action = CreateTaskFragmentDirections
                .actionCreateTaskFragmentToSolveEquationFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun switchCreateTaskToBuildFragment(view: View) {
        var navController : NavController? = null
        try {
            navController = Navigation.findNavController(view)
        }catch (e : java.lang.Exception){
            return
        }

        if (navController.currentDestination?.id == R.id.createTaskFragment) {
            val action = CreateTaskFragmentDirections
                .actionCreateTaskFragmentToBuildScaleFromEqFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }
}
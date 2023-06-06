package com.vahy.bakalarka.ui.main

import android.content.Context
import android.content.SharedPreferences
import android.view.MotionEvent
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.vahy.bakalarka.MainActivity
import com.vahy.bakalarka.R
import com.vahy.bakalarka.objects.menu.RestartIcon
import com.vahy.bakalarka.tasks.*


class MainMenuViewModel : ViewModel() {
    lateinit var mainActivity: MainActivity
    lateinit var context : Context
    lateinit var prefsLevel : SharedPreferences
    lateinit var prefsTaskInLevel: SharedPreferences
    lateinit var prefsLastUnlockedLevel: SharedPreferences
    lateinit var backgroundView: BackgroundView
    var lastUnlockedLevel = 1


    fun initialize(mainMenuView: MainMenuView, bg : BackgroundView){
        context = mainActivity.applicationContext
        prefsLevel = context.getSharedPreferences(prefsNameLevel, Context.MODE_PRIVATE)
        prefsTaskInLevel =  getSharedPreferences(prefsNameTaskInLevel)

        prefsLastUnlockedLevel =  getSharedPreferences(prefsNameLastUnlocked)
        lastUnlockedLevel = prefsLastUnlockedLevel.getInt(prefsNameLastUnlocked, 1)
        mainMenuView.changeLockedLevels(lastUnlockedLevel, getListFinishedLevels())
        backgroundView = bg
    }

    fun switchBackground(){
        val switch = SwitchingBetweenTasks()
        switch.mainActivity = mainActivity
        switch.changeBackground(backgroundView, lastUnlockedLevel)
    }


    fun getListFinishedLevels() : MutableList<Int>{
        val finished = mutableListOf<Int>()
        (1 .. NUM_LEVELS).forEach { level ->
            val prefsContinueOnTaskInChosenLevel = getSharedPreferences(prefsNameTaskInLevel + level)
            val continueOnTask = prefsContinueOnTaskInChosenLevel.getInt(prefsNameTaskInLevel + level, 0)
            if (getLevel(level).isFinished(continueOnTask))
                finished.add(level)
        }
        return finished
    }

    fun onTouch(mainMenu : MainMenuView, motionEvent : MotionEvent) : Boolean{
        val createTaskLevel = mainMenu.clickCreateTaskIcon(motionEvent)
        if (createTaskLevel > 0 && createTaskLevel <= lastUnlockedLevel
            && createTaskLevel <= NUM_LEVELS){
            val continueOnTask = storeTargetLevelAndTask(createTaskLevel)
            switchToChosenCreateTaskLevel(createTaskLevel, continueOnTask, mainMenu)
            return true
        }

        val level = mainMenu.clickLevel(motionEvent)
        if (level > lastUnlockedLevel || level <= 0 || level > NUM_LEVELS) {
            clickedRestart(mainMenu, motionEvent)
            return true
        }

        val continueOnTask = storeTargetLevelAndTask(level)

        switchToChosenLevel(level, continueOnTask, mainMenu)

        return true
    }

    private fun clickedRestart(mainMenu: MainMenuView, motionEvent: MotionEvent) {
        val icon = mainMenu.clickedIcon(motionEvent)
        if (icon is RestartIcon) {
            restartLevels()
            mainMenu.changeLockedLevels(lastUnlockedLevel, getListFinishedLevels())
            mainMenu.invalidate()
        }
    }


    private fun restartLevels(){
        (1..4).forEach { level ->
            editSharedPreferences(getSharedPreferences(prefsNameTaskInLevel + level),
                prefsNameTaskInLevel + level, 0)
        }

        editSharedPreferences(prefsLastUnlockedLevel, prefsNameLastUnlocked, 1)
        lastUnlockedLevel = 1

        editSharedPreferences(getSharedPreferences(prefsNameNewUnlocked),
            prefsNameNewUnlocked, -1)
        switchBackground()
    }

    private fun storeTargetLevelAndTask(level: Int): Int {
        val prefsContinueOnTaskInChosenLevel = getSharedPreferences(prefsNameTaskInLevel + level)
        var continueOnTask = prefsContinueOnTaskInChosenLevel.getInt(prefsNameTaskInLevel + level, 0)

        val numTasksInLevel = getLevel(level).getNumberTasks()
        if (continueOnTask >= numTasksInLevel)
            continueOnTask = numTasksInLevel - 1


        editSharedPreferences(prefsLevel, prefsNameLevel, level)
        editSharedPreferences(prefsTaskInLevel, prefsNameTaskInLevel, continueOnTask)
        return continueOnTask
    }

    fun getLevel(level: Int) : Level {
        when(level){
            1 -> return Level1()
            2 -> return Level2()
            3 -> return Level3()
            4 -> return Level4()
        }
        return Level1()
    }


    private fun switchToChosenLevel(level: Int, continueOnTask: Int,
                                    mainMenu: MainMenuView) {
        val taskType1 = getLevel(level).getTaskGenerator(continueOnTask)


        if (taskType1 == buildTypeTask) {
            switchToBuildEqFragment(mainMenu)
        }

        if (taskType1 == solveTaskType) {
            switchToSolveEqFragment(mainMenu)
        }
    }

    private fun switchToChosenCreateTaskLevel(level: Int, continueOnTask: Int,
                                    mainMenu: MainMenuView) {
        val taskType1 = getLevel(level).getTaskGenerator(continueOnTask)


        if (taskType1 == buildTypeTask) {
            switchToChooseTypeCreateTaskFragment(mainMenu)
        }

        if (taskType1 == solveTaskType) {
            switchToCreateTaskFragment(mainMenu)
        }
    }


    private fun switchToBuildEqFragment(mainMenu: MainMenuView) {
        var navController : NavController? = null
        try {
            navController = Navigation.findNavController(mainMenu)
        }catch (e : java.lang.Exception){
            return
        }

        if (navController.currentDestination?.id == R.id.mainMenuFragment) {
            val action = MainMenuFragmentDirections
                .actionMainMenuToBuildScaleFromEqFragment()
            Navigation.findNavController(mainMenu).navigate(action)
        }

    }

    private fun switchToSolveEqFragment(mainMenu: MainMenuView) {
        var navController : NavController? = null
        try {
            navController = Navigation.findNavController(mainMenu)
        }catch (e : java.lang.Exception){
            return
        }

        if (navController.currentDestination?.id == R.id.mainMenuFragment) {
            val action = MainMenuFragmentDirections
                .actionMainMenuToSolveEquationFragment()
            Navigation.findNavController(mainMenu).navigate(action)
        }
    }

    private fun switchToCreateTaskFragment(mainMenu: MainMenuView) {
        var navController : NavController? = null
        try {
            navController = Navigation.findNavController(mainMenu)
        }catch (e : java.lang.Exception){
            return
        }

        if (navController.currentDestination?.id == R.id.mainMenuFragment) {
            val action = MainMenuFragmentDirections
                .actionMainMenuFragmentToCreateTaskFragment()
            Navigation.findNavController(mainMenu).navigate(action)
        }
    }

    private fun switchToChooseTypeCreateTaskFragment(mainMenu: MainMenuView) {
        var navController : NavController? = null
        try {
            navController = Navigation.findNavController(mainMenu)
        }catch (e : java.lang.Exception){
            return
        }

        if (navController.currentDestination?.id == R.id.mainMenuFragment) {
            val action = MainMenuFragmentDirections
                .actionMainMenuFragmentToChooseTypeCreateTaskFragment()
            Navigation.findNavController(mainMenu).navigate(action)
        }
    }


    private fun getSharedPreferences(name : String) =
        mainActivity.applicationContext.getSharedPreferences(
            name, Context.MODE_PRIVATE
        )

    private fun editSharedPreferences(prefs : SharedPreferences, name : String, value : Int){
        val editor = prefs.edit()
        editor.putInt(name, value)
        editor.apply()
    }

}
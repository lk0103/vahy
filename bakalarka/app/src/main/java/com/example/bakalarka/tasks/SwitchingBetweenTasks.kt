package com.example.bakalarka.tasks

import android.content.Context
import com.example.bakalarka.MainActivity
import com.example.bakalarka.R
import com.example.bakalarka.ui.main.BackgroundView
import com.example.bakalarka.ui.main.TaskMainMenuView
import kotlinx.android.synthetic.main.fragment_solve_equation.*
import kotlin.random.Random


class SwitchingBetweenTasks {
    lateinit var mainActivity: MainActivity

    fun changeBackground(backgroundView : BackgroundView) {
        val level = getLevelInt()
        val pattern = when (level) {
            1 -> R.drawable.orange_pattern
            2 -> R.drawable.violet_pattern
            3 -> R.drawable.blue_pattern
            4 -> R.drawable.green_pattern
            else -> R.drawable.blue_pattern
        }
        val bgColor = when (level) {
            1 -> R.color.level1_background
            2 -> R.color.level2_background
            3 -> R.color.level3_background
            4 -> R.color.level4_background
            else -> R.color.main_menu_background
        }
        backgroundView.changeBackground(pattern, bgColor)
    }

    fun setTaskMainMenu(taskMainMenuView: TaskMainMenuView){
        val prefsLevel =  mainActivity.applicationContext.getSharedPreferences(
            "level", Context.MODE_PRIVATE)
        val level = prefsLevel?.getInt("level", 1) ?: 1

        val prefsContinueOnTaskInChosenLevel = mainActivity.applicationContext.getSharedPreferences(
            "taskInLevel" + level, Context.MODE_PRIVATE
        )
        val continueOnTask = prefsContinueOnTaskInChosenLevel.getInt("taskInLevel" + level, 0)

        val levelInfo = getLevel(level)
        val numTasks = levelInfo.tasks.size
        taskMainMenuView.setProgressBarPar(numTasks, continueOnTask)
    }

    fun getLevel(level: Int) : Level{
        when(level){
            1 -> return Level1()
            2 -> return Level2()
        }
        return Level1()
    }

    fun getLevelInt(): Int {
        val prefsLevel = mainActivity.applicationContext.getSharedPreferences(
            "level", Context.MODE_PRIVATE
        )
        return prefsLevel.getInt("level", 1)
    }


    fun storeTargetLevelAndTask(): Pair<Int, Int> {
        val prefsLevel =  mainActivity.applicationContext.getSharedPreferences(
            "level", Context.MODE_PRIVATE)
        val level = prefsLevel.getInt("level", 1)

        val prefsTaskInLevel =  mainActivity.applicationContext.getSharedPreferences(
            "taskInLevel", Context.MODE_PRIVATE)
        var taskInLevel = prefsTaskInLevel.getInt("taskInLevel", 0)
        val prefsContinueOnTaskInChosenLevel = mainActivity.applicationContext.getSharedPreferences(
            "taskInLevel" + level, Context.MODE_PRIVATE
        )
        var continueOnTask = prefsContinueOnTaskInChosenLevel.getInt("taskInLevel" + level, 0)

        taskInLevel++
        unlockNewLevelIfComplete(level, continueOnTask)

        if (continueOnTask < getLevel(level).tasks.size - 1) {
            continueOnTask = taskInLevel
        }else if (continueOnTask == getLevel(level).tasks.size - 1){
            continueOnTask++
        }
        taskInLevel = generateNewTaskAfterCompletionOfLevel(level, continueOnTask)

        var editor = prefsTaskInLevel.edit()
        editor.putInt("taskInLevel", taskInLevel)
        editor.apply()

        editor = prefsContinueOnTaskInChosenLevel.edit()
        editor.putInt("taskInLevel" + level, continueOnTask)
        editor.apply()

        editor = prefsLevel.edit()
        editor.putInt("level", level)
        editor.apply()

        return Pair(level, taskInLevel)
    }

    private fun generateNewTaskAfterCompletionOfLevel(level: Int, taskInLevel: Int): Int {
        var taskInLevel1 = taskInLevel
        val numTasksInLevel = getLevel(level).tasks.size
        if (taskInLevel1 >= numTasksInLevel) {
            taskInLevel1 = Random.nextInt(numTasksInLevel - 4, numTasksInLevel)
        }
        return taskInLevel1
    }


    private fun unlockNewLevelIfComplete(level: Int, taskInLevel: Int) {
        val numTasksInLevel = getLevel(level).tasks.size
        if (taskInLevel == numTasksInLevel - 1) {
            val prefsLastUnlockedLevel = mainActivity.applicationContext.getSharedPreferences(
                "lastUnlockedLevel", Context.MODE_PRIVATE
            )
            var lastUnlockedLevel = prefsLastUnlockedLevel.getInt("lastUnlockedLevel", 1)
            if (lastUnlockedLevel < NUM_LEVELS) {
                lastUnlockedLevel++
                val editor = prefsLastUnlockedLevel.edit()
                editor.putInt("lastUnlockedLevel", lastUnlockedLevel)
                editor.apply()
            }
        }
    }
}
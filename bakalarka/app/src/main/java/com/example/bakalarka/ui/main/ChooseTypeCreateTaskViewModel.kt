package com.example.bakalarka.ui.main

import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.bakalarka.MainActivity
import com.example.bakalarka.R
import com.example.bakalarka.tasks.ControlTasks
import com.example.bakalarka.tasks.SwitchingBetweenTasks

class ChooseTypeCreateTaskViewModel : ViewModel() {
    private lateinit var mainActivity: MainActivity
    private lateinit var taskMenuView: TaskMenuView
    private lateinit var chooseTypeCreateTaskView: ChooseTypeCreateTaskView

    var switchBetweenTasks = SwitchingBetweenTasks()
    var controlMenuTasks = ControlTasks()

    @JvmName("setMainActivity1")
    fun setMainActivity(mainactivity : MainActivity,
                        TaskMenuView : TaskMenuView,
                        ChooseTypeCreateTaskView: ChooseTypeCreateTaskView){
        mainActivity = mainactivity
        switchBetweenTasks.mainActivity = mainactivity
        controlMenuTasks.mainActivity = mainactivity
        taskMenuView = TaskMenuView
        chooseTypeCreateTaskView = ChooseTypeCreateTaskView
    }

    fun onTouchMainMenu(event: MotionEvent) : Boolean{
        taskMenuView.onTouchEvent(event)
        if (taskMenuView.leaveTask){
            controlMenuTasks.leaveTask(taskMenuView)
        }
        return true
    }

    fun onTouchChooseTypeCreateTask(event: MotionEvent) : Boolean{
        chooseTypeCreateTaskView.onTouchEvent(event)

        if (chooseTypeCreateTaskView.continueToCreateType != - 1){
            mainActivity.setTypeCreateTask(chooseTypeCreateTaskView.continueToCreateType)
            chooseTypeCreateTaskView.continueToCreateType = -1
            switchChooseCreateTaskToCreateTaskFragment(chooseTypeCreateTaskView)
        }
        return true
    }


    private fun switchChooseCreateTaskToCreateTaskFragment(view: View) {
        var navController : NavController? = null
        try {
            navController = Navigation.findNavController(view)
        }catch (e : java.lang.Exception){
            return
        }

        if (navController.currentDestination?.id == R.id.chooseTypeCreateTaskFragment) {
            val action = ChooseTypeCreateTaskFragmentDirections
                .actionChooseTypeCreateTaskFragmentToCreateTaskFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }
}
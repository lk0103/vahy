package com.example.bakalarka.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bakalarka.MainActivity
import com.example.bakalarka.R
import com.example.bakalarka.BR.*
import com.example.bakalarka.databinding.FragmentSolveEquationBinding
import com.example.bakalarka.tasks.*
import com.example.vahy.equation.Variable
import kotlinx.android.synthetic.main.fragment_main_menu.*
import kotlinx.android.synthetic.main.fragment_solve_equation.*
import kotlin.random.Random


class SolveEquationFragment : Fragment() {

    private lateinit var binding: FragmentSolveEquationBinding
    private lateinit var viewModel: SolveEquationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_solve_equation, container, false)
        binding.setLifecycleOwner (this)
        return binding.root
    }

    lateinit var mainactivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainactivity = context as MainActivity
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SolveEquationViewModel::class.java)
        binding.setVariable(myViewModel, viewModel)
        viewModel.setMainActivity(mainactivity)
        viewModel.switchBetweenTasks.changeBackground(backgroundViewSolve)

        setCanvasParameters()
        viewModel.switchBetweenTasks.setTaskMainMenu(TaskMainMenuView)
        viewModel.generateEquation(ScalesView, TaskSolveEquationView)


        TaskMainMenuView.setOnTouchListener { view, motionEvent ->
            viewModel.onTouch(view, motionEvent, ScalesView)
        }

        TaskSolveEquationView.setOnTouchListener { view, motionEvent ->
            viewModel.generateNewEq(view, motionEvent, ScalesView)
        }

    }



    private fun setCanvasParameters() {
        val displayMetrics = DisplayMetrics()
        mainactivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        val marginTop = height / 7
        var param = ScalesView.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(8, marginTop, 8, 8)
        ScalesView.layoutParams = param

        val heightOfMenu = height / 7
        val widthOfMenu = width * 3 / 8 - 8 * 2
        param = TaskMainMenuView.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(8, 8, width - widthOfMenu, height - heightOfMenu)
        TaskMainMenuView.layoutParams = param
        TaskMainMenuView.layoutParams.width = widthOfMenu
        TaskMainMenuView.layoutParams.height = heightOfMenu

        val widthOfSolution = width - widthOfMenu - 8 * 2
        param = TaskSolveEquationView.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(100, 8, 0, height - heightOfMenu)
        TaskSolveEquationView.layoutParams = param
        TaskSolveEquationView.layoutParams.width = widthOfSolution
        TaskSolveEquationView.layoutParams.height = heightOfMenu
    }

}

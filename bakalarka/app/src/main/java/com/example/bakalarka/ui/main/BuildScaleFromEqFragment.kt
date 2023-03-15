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
import com.example.bakalarka.databinding.FragmentBuildScaleFromEqBinding
import kotlinx.android.synthetic.main.fragment_build_scale_from_eq.*
import kotlinx.android.synthetic.main.fragment_build_scale_from_eq.ScalesView
import kotlinx.android.synthetic.main.fragment_build_scale_from_eq.TaskMainMenuView
import kotlinx.android.synthetic.main.fragment_solve_equation.*


class BuildScaleFromEqFragment : Fragment() {

    private lateinit var binding: FragmentBuildScaleFromEqBinding
    private lateinit var viewModel: BuildScaleFromEqViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_build_scale_from_eq, container, false)
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
        viewModel = ViewModelProvider(this).get(BuildScaleFromEqViewModel::class.java)
        binding.setVariable(myViewModel, viewModel)
        viewModel.mainActivity = mainactivity
        viewModel.switchBetweenTasks.mainActivity = mainactivity
        viewModel.switchBetweenTasks.changeBackground(backgroundViewBuild)

        setCanvasParameters()
        viewModel.switchBetweenTasks.setTaskMainMenu(TaskMainMenuView)
        viewModel.generateEquation(ScalesView, BuildScaleView)

        TaskMainMenuView.setOnTouchListener { view, motionEvent ->
            viewModel.onTouch(view, motionEvent, ScalesView)
        }

        BuildScaleView.setOnTouchListener { view, motionEvent ->
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
        param = BuildScaleView.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(100, 8, 0, height - heightOfMenu)
        BuildScaleView.layoutParams = param
        BuildScaleView.layoutParams.width = widthOfSolution
        BuildScaleView.layoutParams.height = heightOfMenu
    }

}
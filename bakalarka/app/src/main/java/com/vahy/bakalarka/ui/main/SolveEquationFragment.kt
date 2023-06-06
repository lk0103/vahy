package com.vahy.bakalarka.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vahy.bakalarka.MainActivity
import com.vahy.bakalarka.BR.*
import com.vahy.bakalarka.databinding.FragmentSolveEquationBinding
import com.vahy.bakalarka.tasks.*
import com.vahy.vahy.ScalesView


class SolveEquationFragment : Fragment() {

    private var _binding: FragmentSolveEquationBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SolveEquationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSolveEquationBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
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

        viewModel.setMainActivity(mainactivity, binding.ScalesView, binding.TaskSolveEquationView,
            binding.TaskMainMenuView)
        viewModel.switchBetweenTasks.changeBackground(binding.backgroundViewSolve)

        setCanvasParameters()
        viewModel.generateEquation()

        binding.TaskMainMenuView.setOnTouchListener { view, motionEvent ->
            viewModel.onTouch(view, motionEvent)
        }

        binding.TaskSolveEquationView.setOnTouchListener { view, motionEvent ->
            viewModel.generateNewEq(view, motionEvent)
        }
    }



    private fun setCanvasParameters() {
        val displayMetrics = DisplayMetrics()
        mainactivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        val marginTop = height / 7
        var param = binding.ScalesView.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(8, marginTop, 8, 8)
        binding.ScalesView.layoutParams = param

        val heightOfMenu = height / 7
        val widthOfMenu = width * 10 / 24 - 8 * 2
        param = binding.TaskMainMenuView.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0, 8, width - widthOfMenu, height - heightOfMenu)
        binding.TaskMainMenuView.layoutParams = param
        binding.TaskMainMenuView.layoutParams.width = widthOfMenu
        binding.TaskMainMenuView.layoutParams.height = heightOfMenu

        val widthOfSolution = width - widthOfMenu
        param = binding.TaskSolveEquationView.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0, 8, 0, height - heightOfMenu)
        binding.TaskSolveEquationView.layoutParams = param
        binding.TaskSolveEquationView.layoutParams.width = widthOfSolution
        binding.TaskSolveEquationView.layoutParams.height = heightOfMenu
    }

    override fun onPause() {
        binding.ScalesView.cancelShownResultMessage()
        binding.ScalesView.cancelShownNewLevelMessage()
        super.onPause()
    }

}

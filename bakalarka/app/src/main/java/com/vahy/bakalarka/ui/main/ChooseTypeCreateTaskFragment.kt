package com.vahy.bakalarka.ui.main

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vahy.bakalarka.BR
import com.vahy.bakalarka.MainActivity
import com.vahy.bakalarka.databinding.FragmentChooseTypeCreateTaskBinding

class ChooseTypeCreateTaskFragment : Fragment() {

    private var _binding: FragmentChooseTypeCreateTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ChooseTypeCreateTaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseTypeCreateTaskBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    lateinit var mainactivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainactivity = context as MainActivity
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChooseTypeCreateTaskViewModel::class.java)
        binding.setVariable(BR.myViewModel, viewModel)

        viewModel.setMainActivity(mainactivity, binding.TaskMainMenuView,
            binding.ChooseTypeCreateTaskView)

        setCanvasParameters()
        binding.TaskMainMenuView.setMainMenuInChooseCreateTask()

        viewModel.switchBetweenTasks.changeBackground(binding.backgroundViewCreate)


        binding.TaskMainMenuView.setOnTouchListener { view, motionEvent ->
            viewModel.onTouchMainMenu(motionEvent)
        }

        binding.ChooseTypeCreateTaskView.setOnTouchListener { view, motionEvent ->
            viewModel.onTouchChooseTypeCreateTask(motionEvent)
        }
    }


    private fun setCanvasParameters() {
        val displayMetrics = DisplayMetrics()
        mainactivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        val marginTop = height / 7
        var param = binding.ChooseTypeCreateTaskView.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(8, marginTop, 8, 8)
        binding.ChooseTypeCreateTaskView.layoutParams = param

        val heightOfMenu = height / 7
        val widthOfMenu = width - 8 * 2
        param = binding.TaskMainMenuView.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0, 8, width - widthOfMenu, height - heightOfMenu)
        binding.TaskMainMenuView.layoutParams = param
        binding.TaskMainMenuView.layoutParams.width = widthOfMenu
        binding.TaskMainMenuView.layoutParams.height = heightOfMenu
        binding.TaskMainMenuView.isInCreateTask = true
    }

}
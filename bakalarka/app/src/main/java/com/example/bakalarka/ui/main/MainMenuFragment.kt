package com.example.bakalarka.ui.main


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bakalarka.MainActivity
import com.example.bakalarka.R
import com.example.bakalarka.BR.*
import com.example.bakalarka.databinding.FragmentMainMenuBinding
import kotlinx.android.synthetic.main.fragment_main_menu.*
import kotlinx.android.synthetic.main.fragment_solve_equation.*


class MainMenuFragment : Fragment() {

    private lateinit var binding: FragmentMainMenuBinding
    private lateinit var viewModel: MainMenuViewModel

    override fun onStart() {
        super.onStart()
        viewModel.mainActivity = mainactivity
        viewModel.initialize(MainMenuView)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_menu, container, false)
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
        viewModel = ViewModelProvider(this).get(MainMenuViewModel::class.java)
        binding.setVariable(myViewModel, viewModel)
        backgroundView.changeBackground(R.drawable.blue_pattern, R.color.main_menu_background)

        MainMenuView.setOnTouchListener { view, motionEvent ->
            viewModel.onTouch(MainMenuView, motionEvent)
        }

    }



}
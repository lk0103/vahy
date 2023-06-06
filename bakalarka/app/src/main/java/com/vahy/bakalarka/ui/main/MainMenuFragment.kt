package com.vahy.bakalarka.ui.main


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vahy.bakalarka.MainActivity
import com.vahy.bakalarka.BR.*
import com.vahy.bakalarka.databinding.FragmentMainMenuBinding
import com.vahy.bakalarka.tasks.SwitchingBetweenTasks


class MainMenuFragment : Fragment() {
    private var _binding: FragmentMainMenuBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainMenuViewModel


    override fun onStart() {
        super.onStart()
        viewModel.mainActivity = mainactivity
        viewModel.initialize(binding.MainMenuView, binding.backgroundView)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainMenuBinding.inflate(inflater, container, false)
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
        viewModel = ViewModelProvider(this).get(MainMenuViewModel::class.java)
        binding.setVariable(myViewModel, viewModel)

        val lastUnlockedLevel = mainactivity.applicationContext.getSharedPreferences(
                                prefsNameLastUnlocked, Context.MODE_PRIVATE)
                                .getInt(prefsNameLastUnlocked, 1)
        val switch = SwitchingBetweenTasks()
        switch.mainActivity = mainactivity
        switch.changeBackground(binding.backgroundView, lastUnlockedLevel)

        binding.MainMenuView.setOnTouchListener { view, motionEvent ->
            viewModel.onTouch(binding.MainMenuView, motionEvent)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.MainMenuView.changeSizeCreateIcons()
    }

}
package com.example.bakalarka.ui.main

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.bakalarka.R
import com.example.bakalarka.BR.*
import com.example.bakalarka.MainActivity
import com.example.bakalarka.databinding.FragmentScalesBinding
import kotlinx.android.synthetic.main.fragment_scales.*


class ScalesFragment : Fragment() {

    private lateinit var binding: FragmentScalesBinding
    private lateinit var viewModel: ScalesViewModel

    override fun onStart() {
        super.onStart()
//        arguments?.let {
//            var args = ScalesFragmentArgs.fromBundle(it)
//            viewModel.countingStrategy =
//                viewModel.strategies.indexOf(args.strategy)
//            Log.d("preferences", "first on start ${viewModel.countingStrategy} ${args.strategy}")
//        }
//        viewModel.mainActivity = mainactivity
//        viewModel.initialize()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scales, container, false)
        binding.setLifecycleOwner (this)
        return binding.root
    }

    lateinit var mainactivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainactivity = context as MainActivity
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ScalesViewModel::class.java)
        //binding.setVariable(myViewModel, viewModel)

    }

}
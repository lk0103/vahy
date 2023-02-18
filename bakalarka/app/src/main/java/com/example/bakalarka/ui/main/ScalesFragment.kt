package com.example.bakalarka.ui.main

import android.content.Context
import android.media.audiofx.DynamicsProcessing.Eq
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.bakalarka.R
import com.example.bakalarka.BR.*
import com.example.bakalarka.MainActivity
import com.example.bakalarka.databinding.FragmentScalesBinding
import com.example.bakalarka.equation.Bracket
import com.example.bakalarka.equation.Equation
import com.example.bakalarka.equation.SystemOfEquations
import com.example.bakalarka.objects.*
import com.example.vahy.equation.Addition
import com.example.vahy.equation.Constant
import com.example.vahy.equation.Multiplication
import com.example.vahy.equation.Variable
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

        val left1 = Addition(mutableListOf(
            Variable("x")
        ))
        val right1 = Addition(mutableListOf(
            Constant(3.0),
            Constant(2.0)
        ))
        val sysEq = SystemOfEquations(listOf(Equation(left1, right1)))
        ScalesView.setEquation(sysEq, 0)

        //binding.setVariable(myViewModel, viewModel)
        //ScalesView.setVisibilityObjectToChooseFrom(false)
//        ScalesView.setObjectsToChooseFrom(mutableListOf(Ball(mainactivity.applicationContext, 1),
//            Cube(mainactivity.applicationContext, 1),
//            Cylinder(mainactivity.applicationContext, 1),
//            Ballon(mainactivity.applicationContext, -1),
//            Package(mainactivity.applicationContext),
//            Weight(mainactivity.applicationContext, 1)
//
//        ))
    }

}
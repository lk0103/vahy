package com.example.bakalarka.ui.main

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bakalarka.MainActivity
import com.example.bakalarka.R
import com.example.bakalarka.BR.*
import com.example.bakalarka.databinding.FragmentScalesBinding
import com.example.bakalarka.equation.Bracket
import com.example.bakalarka.equation.Equation
import com.example.bakalarka.equation.SystemOfEquations
import com.example.bakalarka.objects.Ball
import com.example.bakalarka.objects.Cylinder
import com.example.bakalarka.objects.ScaleVariable
import com.example.bakalarka.tasks.EquationsGenerator
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
        binding.setVariable(myViewModel, viewModel)

        setCanvasParameters()


        val mapSolutions = mapOf<ScaleVariable, Int>(Ball(mainactivity, 2) to 3,
                        Cylinder(mainactivity, 2) to 4)
        TaskSolveEquationView.setMapSolutions(mapSolutions)

        generateEquation()


        //binding.setVariable(myViewModel, viewModel)
//        ScalesView.setVisibilityObjectToChooseFrom(false)
//        ScalesView.setObjectsToChooseFrom(mutableListOf(Ball(mainactivity.applicationContext, 1),
//            Cube(mainactivity.applicationContext, 1),
//            Cylinder(mainactivity.applicationContext, 1),
//            Ballon(mainactivity.applicationContext, -1),
//            Package(mainactivity.applicationContext),
//            Weight(mainactivity.applicationContext, 1)
//
//        ))
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
        val widthOfMenu = width / 2 - 8 * 2
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

    private fun generateEquation() {
        ////default rovnica
        val left = Addition(
            mutableListOf(
                Multiplication(
                    Bracket(
                        Addition(
                            mutableListOf(
                                Multiplication(Variable("x"), Constant(2)),
                                Constant(2)
                            )
                        )
                    ), Constant(2)
                )
            )
        )
        val right = Addition(
            mutableListOf(
                Constant(9),
                Constant(4),
                Constant(3)
            )
        )
        var eq = Equation(left, right)
        var sysEq = SystemOfEquations(listOf(eq))
        sysEq.solve()

        //generovanie rovnice
        val generator = EquationsGenerator()
        generator.rangeNumVarLeft = Pair(1, 4)
        generator.rangeNumVarRight = Pair(0, 2)
        generator.rangeSumConsLeft = Pair(8, 12)
        generator.rangeSumConsRight = Pair(10, 20)
        generator.rangeNumConsLeft = Pair(3, 4)
        generator.rangeNumConsRight = Pair(3, 4)

        generator.rangeNumBracketLeft = Pair(0, 0)
        generator.rangeNumBracketRight = Pair(0, 0)
        generator.rangeNumVarBracket = Pair(0, 2)
        generator.rangeNumConsBracket = Pair(1, 2)
        generator.rangeSumConsBracket = Pair(5, 15)

        generator.rangeNumNegativeConsRight = Pair(0, 0)
        generator.rangeNumNegativeConsLeft = Pair(0, 0)

        for (i in (0 until 1)) {
            sysEq = generator.generateLinearEquationWithNaturalSolution()
            Log.i("generate", sysEq.toString())
            sysEq.solve()
            Log.i("generate", sysEq.solutions.toString())
        }

        ScalesView.setEquation(sysEq, 0)
    }

}
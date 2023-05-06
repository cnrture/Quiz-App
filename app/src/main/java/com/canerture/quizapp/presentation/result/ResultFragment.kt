package com.canerture.quizapp.presentation.result

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.canerture.quizapp.R
import com.canerture.quizapp.common.extension.collect
import com.canerture.quizapp.common.viewBinding
import com.canerture.quizapp.databinding.FragmentResultBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultFragment : Fragment(R.layout.fragment_result) {

    private val binding by viewBinding(FragmentResultBinding::bind)

    private val quizViewModel: ResultViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectState()
        collectEffect()

        binding.btnContinue.setOnClickListener {
            quizViewModel.setEvent(ResultEvent.ContinueClicked)
        }
    }

    private fun collectState() = quizViewModel.state.collect(viewLifecycleOwner) { state ->
        with(binding) {
            state.result?.let {
                tvScore.text = "$it / 10"
                progressBarResult.setProgress(it.toFloat() * 10)

                if (state.lowerThanFifty) setResultColors(R.drawable.ic_incorrect, R.color.pink)
                else setResultColors(R.drawable.ic_correct, R.color.green)
            }
        }
    }

    private fun collectEffect() = quizViewModel.effect.collect(viewLifecycleOwner) { effect ->
        when (effect) {
            ResultUIEffect.GoHome -> findNavController().navigate(R.id.resultToHome)
        }
    }

    private fun setResultColors(drawable: Int, color: Int) {
        binding.imgResult.setImageResource(drawable)
        binding.progressBarResult.setFillColor(requireContext().getColor(color))
    }
}
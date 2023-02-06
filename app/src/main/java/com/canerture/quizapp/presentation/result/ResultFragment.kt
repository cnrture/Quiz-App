package com.canerture.quizapp.presentation.result

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.canerture.quizapp.R
import com.canerture.quizapp.common.extension.collect
import com.canerture.quizapp.databinding.FragmentResultBinding
import com.canerture.quizapp.delegation.viewBinding
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
            when (state) {
                ResultUIState.Loading -> Unit
                is ResultUIState.Data -> {
                    tvScore.text = "%${String.format("%.1f", state.result)}"
                    progressBarResult.setProgress(state.result)

                    if (state.lowerThanFifty) {
                        imgResult.setImageResource(R.drawable.ic_incorrect)
                        progressBarResult.setFillColor(requireContext().getColor(R.color.pink))
                    } else {
                        imgResult.setImageResource(R.drawable.ic_correct)
                        progressBarResult.setFillColor(requireContext().getColor(R.color.green))
                    }
                }
            }
        }
    }

    private fun collectEffect() = quizViewModel.effect.collect(viewLifecycleOwner) { effect ->
        when (effect) {
            ResultUIEffect.GoHome -> findNavController().navigate(R.id.resultToHome)
        }
    }
}
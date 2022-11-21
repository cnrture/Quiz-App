package com.canerture.quizapp.presentation.quiz

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.canerture.quizapp.R
import com.canerture.quizapp.common.showFullPagePopup
import com.canerture.quizapp.common.showPopup
import com.canerture.quizapp.common.viewBinding
import com.canerture.quizapp.databinding.FragmentQuizBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizFragment : Fragment(R.layout.fragment_quiz) {

    private val binding by viewBinding(FragmentQuizBinding::bind)

    private val quizViewModel: QuizViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgClose.setOnClickListener {
            quizViewModel.setEvent(QuizEvent.CloseClicked)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            quizViewModel.state.collect {
                binding.progressBar.isVisible = it.loadingState

                it.data?.let { questionList ->

                    val answers = mutableListOf(
                        questionList[0].incorrectAnswerOne,
                        questionList[0].incorrectAnswerTwo,
                        questionList[0].incorrectAnswerThree,
                        questionList[0].correctAnswer
                    )
                    answers.shuffle()

                    with(binding) {
                        tvQuestion.text = questionList[0].question
                        btnAnswerOne.text = answers[0]
                        btnAnswerTwo.text = answers[1]
                        btnAnswerThree.text = answers[2]
                        btnAnswerFour.text = answers[3]
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            quizViewModel.effect.collect { effect ->
                when (effect) {
                    QuizUIEffect.GoBack -> {
                        findNavController().navigate(R.id.quizToHome)
                    }
                    is QuizUIEffect.ShowError -> requireContext().showPopup(
                        iconId = R.drawable.ic_error,
                        title = effect.message,
                        dismissListener = {
                        }
                    )
                    is QuizUIEffect.ShowFullScreenError -> {
                        requireContext().showFullPagePopup(
                            iconId = R.drawable.ic_error,
                            title = effect.message,
                            dismissListener = {
                            }
                        )
                    }
                }
            }
        }
    }
}
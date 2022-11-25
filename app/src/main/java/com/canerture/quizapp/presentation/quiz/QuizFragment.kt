package com.canerture.quizapp.presentation.quiz

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
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
import com.canerture.quizapp.domain.model.question.QuestionUI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizFragment : Fragment(R.layout.fragment_quiz) {

    private val binding by viewBinding(FragmentQuizBinding::bind)

    private val quizViewModel: QuizViewModel by viewModels()

    private var second = 60

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            with(quizViewModel) {

                imgClose.setOnClickListener {
                    setEvent(QuizEvent.CloseClicked)
                }

                viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                    state.collect {
                        progressBar.isVisible = it.loadingState

                        it.data?.let { question ->
                            setData(question, it.questionIndex, it.questionCount)
                        }
                    }
                }

                viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                    effect.collect { effect ->
                        when (effect) {
                            QuizUIEffect.GoBack -> {
                                findNavController().navigate(R.id.quizToCategory)
                            }
                            is QuizUIEffect.ShowError -> requireContext().showPopup(
                                iconId = R.drawable.ic_error,
                                title = effect.message,
                                dismissListener = {
                                    setEvent(QuizEvent.CloseClicked)
                                }
                            )
                            is QuizUIEffect.ShowFullScreenError -> {
                                requireContext().showFullPagePopup(
                                    iconId = R.drawable.ic_error,
                                    title = effect.message,
                                    dismissListener = {
                                        setEvent(QuizEvent.CloseClicked)
                                    }
                                )
                            }
                            QuizUIEffect.ResetUI -> {
                                timer.cancel()
                                second = 60
                                tvTime.text = second.toString()
                                progressBarCountdown.progress = second
                                btnAnswerOne.setBackgroundColor(requireContext().getColor(R.color.white))
                                btnAnswerOne.isEnabled = true
                                btnAnswerTwo.setBackgroundColor(requireContext().getColor(R.color.white))
                                btnAnswerTwo.isEnabled = true
                                btnAnswerThree.setBackgroundColor(requireContext().getColor(R.color.white))
                                btnAnswerThree.isEnabled = true
                                btnAnswerFour.setBackgroundColor(requireContext().getColor(R.color.white))
                                btnAnswerFour.isEnabled = true
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setData(question: QuestionUI, index: Int, count: Int) {

        binding.tvQuestionCount.text = "Question $index of $count"

        val answers = mutableListOf(
            question.incorrectAnswerOne,
            question.incorrectAnswerTwo,
            question.incorrectAnswerThree,
            question.correctAnswer
        )
        answers.shuffle()

        with(binding) {
            tvQuestion.text = question.text
            btnAnswerOne.text = answers[0]
            btnAnswerTwo.text = answers[1]
            btnAnswerThree.text = answers[2]
            btnAnswerFour.text = answers[3]

            checkAnswer(
                btnAnswerOne,
                btnAnswerTwo,
                btnAnswerThree,
                btnAnswerFour,
                correctAnswer = question.correctAnswer
            )
        }

        timer.start()
    }

    private fun checkAnswer(vararg buttonList: Button, correctAnswer: String) {
        buttonList.forEach { button ->
            button.setOnClickListener {
                if (button.text.toString() == correctAnswer) {
                    button.setBackgroundColor(requireContext().getColor(R.color.green))
                    button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_correct, 0)
                    quizViewModel.setEvent(QuizEvent.AnswerClicked(true))
                } else {
                    button.setBackgroundColor(requireContext().getColor(R.color.red))
                    button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_incorrect, 0)
                    quizViewModel.setEvent(QuizEvent.AnswerClicked(false))
                }
                buttonList.forEach { it.isEnabled = false }
            }
        }
    }

    private val timer = object : CountDownTimer(60000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            second--
            binding.tvTime.text = second.toString()
            binding.progressBarCountdown.progress = second
        }

        override fun onFinish() {
            quizViewModel.setEvent(QuizEvent.AnswerClicked(false))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel()
    }
}
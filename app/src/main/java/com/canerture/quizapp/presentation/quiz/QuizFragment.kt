package com.canerture.quizapp.presentation.quiz

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.canerture.quizapp.R
import com.canerture.quizapp.common.extension.collect
import com.canerture.quizapp.common.extension.showFullPagePopup
import com.canerture.quizapp.common.extension.showPopup
import com.canerture.quizapp.databinding.FragmentQuizBinding
import com.canerture.quizapp.delegation.viewBinding
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

                state.collect(viewLifecycleOwner) { state ->
                    progressBar.isVisible = state.loadingState

                    state.data?.let { question ->
                        setData(question, state.questionIndex, state.questionCount)
                    }
                }

                effect.collect(viewLifecycleOwner) { effect ->
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
                            clearState(
                                btnAnswerOne,
                                btnAnswerTwo,
                                btnAnswerThree,
                                btnAnswerFour
                            )
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
                    button.setState(R.color.green, R.drawable.ic_correct, true)
                } else {
                    button.setState(R.color.red, R.drawable.ic_incorrect, false)
                }
                buttonList.forEach { it.isEnabled = false }
            }
        }
    }

    private fun Button.setState(color: Int, drawable: Int, isCorrect: Boolean) {
        setBackgroundColor(requireContext().getColor(color))
        setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
        quizViewModel.setEvent(QuizEvent.AnswerClicked(isCorrect))
    }

    private fun clearState(vararg buttonList: Button) {
        buttonList.forEach { button ->
            button.setBackgroundColor(requireContext().getColor(R.color.white))
            button.isEnabled = true
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
package com.canerture.quizapp.presentation.quiz

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.canerture.quizapp.R
import com.canerture.quizapp.common.extension.collect
import com.canerture.quizapp.common.extension.gone
import com.canerture.quizapp.common.extension.handler
import com.canerture.quizapp.common.extension.showErrorPopup
import com.canerture.quizapp.common.extension.visible
import com.canerture.quizapp.common.viewBinding
import com.canerture.quizapp.databinding.FragmentQuizBinding
import com.canerture.quizapp.domain.model.question.QuestionUI
import com.canerture.quizapp.presentation.base.customview.QuizAppAnswerButton
import com.canerture.quizapp.presentation.base.customview.QuizAppAnswerButton.CorrectType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizFragment : Fragment(R.layout.fragment_quiz) {

    private val binding by viewBinding(FragmentQuizBinding::bind)

    private val quizViewModel: QuizViewModel by viewModels()

    private var second = SECOND

    private lateinit var selectedButton: QuizAppAnswerButton
    private var buttonList = listOf<QuizAppAnswerButton>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            collectState()
            collectEffect()

            buttonList = listOf(btnAnswerOne, btnAnswerTwo, btnAnswerThree, btnAnswerFour)

            imgClose.setOnClickListener { quizViewModel.setEvent(QuizEvent.CloseClicked) }
        }
    }

    private fun collectState() = quizViewModel.state.collect(viewLifecycleOwner) { state ->
        when (state) {
            QuizUIState.Loading -> binding.progressBar.visible()
            is QuizUIState.Data -> {
                binding.progressBar.gone()
                setData(state.question, state.questionIndex, state.questionCount)
            }
        }
    }

    private fun collectEffect() = quizViewModel.effect.collect(viewLifecycleOwner) { effect ->
        when (effect) {
            QuizUIEffect.GoBack -> findNavController().navigate(R.id.quizToCategory)

            is QuizUIEffect.ShowError -> {
                binding.progressBar.gone()
                requireContext().showErrorPopup(
                    R.drawable.ic_error,
                    effect.message
                ) {
                    quizViewModel.setEvent(QuizEvent.CloseClicked)
                }
            }

            QuizUIEffect.CorrectAnswer -> {
                selectedButton.setCorrectState(CorrectType.Correct)
                handler(DELAY) { quizViewModel.setEvent(QuizEvent.NextQuestion) }
            }

            is QuizUIEffect.IncorrectAnswer -> {
                selectedButton.setCorrectState(CorrectType.Incorrect)
                buttonList.forEach {
                    if (it.getText() == effect.correctAnswer) {
                        it.setCorrectState(CorrectType.Correct)
                    }
                }
                handler(DELAY) { quizViewModel.setEvent(QuizEvent.NextQuestion) }
            }

            QuizUIEffect.NextQuestion -> {
                timer.cancel()
                second = SECOND
                binding.tvTime.text = second.toString()
                binding.progressBarCountdown.setProgress(second.toFloat())
                clearState()
            }

            is QuizUIEffect.GoToResult -> {
                val action = QuizFragmentDirections.quizToResult(effect.correctAnswers)
                findNavController().navigate(action)
            }
        }
    }

    private fun setData(question: QuestionUI, index: Int, count: Int) = with(binding) {

        tvQuestionCount.text = "Question $index of $count"
        tvQuestion.text = question.text

        buttonList.mapIndexed { index, button ->
            button.setAnswer(question.allAnswers[index])
        }

        checkAnswer()

        timer.start()
    }

    private fun checkAnswer() {
        buttonList.forEach { button ->
            button.setOnClickListener {
                selectedButton = button
                quizViewModel.setEvent(QuizEvent.Answered(button.getText()))
            }
        }
    }

    private fun clearState() {
        buttonList.forEach { button ->
            button.clearAnswer()
        }
    }

    private val timer = object : CountDownTimer(MILLISLNFUTURE, INTERVAL) {
        override fun onTick(millisUntilFinished: Long) {
            second--
            binding.tvTime.text = second.toString()
            binding.progressBarCountdown.setProgress(second.toFloat())
        }

        override fun onFinish() {
            quizViewModel.setEvent(QuizEvent.TimeIsUp)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel()
    }

    companion object {
        const val MILLISLNFUTURE = 60000L
        const val INTERVAL = 1000L
        const val SECOND = 60
        const val DELAY = 1000L
    }
}
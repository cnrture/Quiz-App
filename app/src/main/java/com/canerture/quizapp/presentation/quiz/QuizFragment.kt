package com.canerture.quizapp.presentation.quiz

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.canerture.quizapp.R
import com.canerture.quizapp.common.extension.collect
import com.canerture.quizapp.common.extension.handler
import com.canerture.quizapp.common.extension.showFullPagePopup
import com.canerture.quizapp.common.extension.showPopup
import com.canerture.quizapp.databinding.FragmentQuizBinding
import com.canerture.quizapp.delegation.viewBinding
import com.canerture.quizapp.domain.model.question.QuestionUI
import com.canerture.quizapp.presentation.base.customview.QuizAppAnswerButton
import com.canerture.quizapp.presentation.base.customview.QuizAppAnswerButton.CorrectType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizFragment : Fragment(R.layout.fragment_quiz) {

    private val binding by viewBinding(FragmentQuizBinding::bind)

    private val quizViewModel: QuizViewModel by viewModels()

    private var second = 60

    private lateinit var selectedButton: QuizAppAnswerButton
    private var buttonList = listOf<QuizAppAnswerButton>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            with(quizViewModel) {

                buttonList = listOf(btnAnswerOne, btnAnswerTwo, btnAnswerThree, btnAnswerFour)

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
                        is QuizUIEffect.ShowError ->
                            requireContext().showPopup(
                                R.drawable.ic_error,
                                effect.message
                            ) {
                                setEvent(QuizEvent.CloseClicked)
                            }
                        is QuizUIEffect.ShowFullScreenError -> {
                            requireContext().showFullPagePopup(
                                R.drawable.ic_error,
                                effect.message
                            ) {
                                setEvent(QuizEvent.CloseClicked)
                            }
                        }
                        QuizUIEffect.CorrectAnswer -> {
                            selectedButton.setCorrectState(CorrectType.Correct)
                            handler(1000) { setEvent(QuizEvent.NextQuestion) }
                        }
                        is QuizUIEffect.IncorrectAnswer -> {
                            selectedButton.setCorrectState(CorrectType.Incorrect)
                            buttonList.forEach {
                                if (it.getText() == effect.correctAnswer) {
                                    it.setCorrectState(CorrectType.Correct)
                                }
                            }
                            handler(1000) { setEvent(QuizEvent.NextQuestion) }
                        }
                        QuizUIEffect.NextQuestion -> {
                            timer.cancel()
                            second = 60
                            tvTime.text = second.toString()
                            progressBarCountdown.setProgress(second.toFloat())
                            clearState()
                        }
                        is QuizUIEffect.GoToResult -> {
                            val action = QuizFragmentDirections.quizToResult(effect.correctAnswers)
                            findNavController().navigate(action)
                        }
                    }
                }
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

    private val timer = object : CountDownTimer(60000, 1000) {
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
}
package com.canerture.quizapp.presentation.base.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.canerture.quizapp.R
import com.canerture.quizapp.databinding.AnswerCustomButtonBinding

class QuizAppAnswerButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0,
) : ConstraintLayout(context, attributeSet, defStyle) {

    private val binding =
        AnswerCustomButtonBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        binding.root.id = View.generateViewId()

        context.obtainStyledAttributes(attributeSet, R.styleable.QuizAppAnswerButton).apply {
            try {
                binding.tvAnswer.text =
                    getString(R.styleable.QuizAppAnswerButton_qaab_answerText)
                binding.tvChoice.text =
                    getString(R.styleable.QuizAppAnswerButton_qaab_choice)

                val index = getInt(R.styleable.QuizAppAnswerButton_qaab_isCorrect, 0)
                setCorrectState(CorrectType.values()[index])
            } finally {
                recycle()
            }
        }
    }

    fun setAnswer(answer: String?) {
        binding.tvAnswer.text = answer
    }

    fun setCorrectState(correctType: CorrectType) {
        when (correctType) {
            CorrectType.Default -> buttonState(
                R.drawable.ic_unselected,
                android.R.color.darker_gray,
                R.color.white,
                false
            )
            CorrectType.Correct -> buttonState(
                R.drawable.ic_correct,
                R.color.green,
                R.color.light_green,
                false
            )
            CorrectType.Incorrect -> buttonState(
                R.drawable.ic_incorrect,
                R.color.red,
                R.color.light_red,
                false
            )
        }
    }

    fun clearAnswer() =
        buttonState(R.drawable.ic_unselected, android.R.color.darker_gray, R.color.white, true)

    fun getText() = binding.tvAnswer.text.toString()

    private fun buttonState(drawable: Int, strokeColor: Int, bgColor: Int, isEnabled: Boolean) =
        with(binding) {
            imgIcon.setImageResource(drawable)
            root.isEnabled = isEnabled
            cvQuestion.strokeColor = context.getColor(strokeColor)
            cvQuestion.setBackgroundColor(context.getColor(bgColor))
        }

    enum class CorrectType {
        Default, Correct, Incorrect
    }
}
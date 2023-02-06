package com.canerture.quizapp.presentation.quiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.quizapp.R
import com.canerture.quizapp.common.extension.collect
import com.canerture.quizapp.delegation.viewmodel.VMDelegation
import com.canerture.quizapp.delegation.viewmodel.VMDelegationImpl
import com.canerture.quizapp.domain.model.question.QuestionUI
import com.canerture.quizapp.domain.usecase.GetQuestionsByCategoryUseCase
import com.canerture.quizapp.domain.usecase.GetSessionTokenUseCase
import com.canerture.quizapp.domain.usecase.GetTokenFromDataStoreUseCase
import com.canerture.quizapp.domain.usecase.SaveTokenUseCase
import com.canerture.quizapp.infrastructure.provider.StringResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val getTokenFromDataStoreUseCase: GetTokenFromDataStoreUseCase,
    private val getSessionTokenUseCase: GetSessionTokenUseCase,
    private val getQuestionsByCategoryUseCase: GetQuestionsByCategoryUseCase,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val stringResourceProvider: StringResourceProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel(),
    VMDelegation<QuizUIEffect, QuizEvent, QuizUIState> by VMDelegationImpl(QuizUIState.Loading) {

    private var questionIndex = 0
    private var questionList = listOf<QuestionUI>()

    var category: Int? = null
    var type: String? = null

    private var correctAnswers = 0

    init {

        initViewModel(this)

        collectEvent()

        category = savedStateHandle.get<Int>(CATEGORY)
        type = savedStateHandle.get<String>(TYPE)

        if (category != null && type != null) getTokenFromDataStore(category!!, type!!)
        else setEffect(QuizUIEffect.ShowError("Something went wrong!"))
    }

    private fun collectEvent() = event.collect(viewModelScope) { event ->
        when (event) {
            QuizEvent.CloseClicked -> {
                setEffect(QuizUIEffect.GoBack)
            }

            is QuizEvent.Answered -> {
                if (event.answer == questionList[questionIndex].correctAnswer) {
                    correctAnswers++
                    setEffect(QuizUIEffect.CorrectAnswer)
                } else {
                    setEffect(QuizUIEffect.IncorrectAnswer(questionList[questionIndex].correctAnswer))
                }
            }

            QuizEvent.NextQuestion -> {
                if (questionIndex == QUESTION_INDEX_NINE) setEffect(
                    QuizUIEffect.GoToResult(
                        correctAnswers
                    )
                )
                else nextQuestion()
            }

            QuizEvent.TimeIsUp -> {
                if (questionIndex == QUESTION_INDEX_NINE) setEffect(
                    QuizUIEffect.GoToResult(
                        correctAnswers
                    )
                )
                else nextQuestion()
            }
        }
    }

    private fun nextQuestion() {
        setEffect(QuizUIEffect.NextQuestion)
        questionIndex++
        val question = questionList[questionIndex]
        question.allAnswers.shuffle()
        setState(QuizUIState.Data(question, questionIndex + 1, questionList.size))
    }

    private fun getTokenFromDataStore(category: Int, type: String) {
        getTokenFromDataStoreUseCase.invoke().onEach { state ->
            when (state) {
                is GetTokenFromDataStoreUseCase.GetTokenFromDataStoreState.Success ->
                    getQuestionsByCategory(category, type, state.token)

                GetTokenFromDataStoreUseCase.GetTokenFromDataStoreState.EmptyToken -> getSessionToken()
            }
        }.launchIn(viewModelScope)
    }

    private fun getQuestionsByCategory(category: Int, type: String, token: String) {
        getQuestionsByCategoryUseCase.invoke(category, type, token).onEach { state ->
            when (state) {
                is GetQuestionsByCategoryUseCase.GetQuestionsByCategoryState.Success -> {
                    questionList = state.questionList
                    setState(
                        QuizUIState.Data(
                            questionList[questionIndex],
                            questionIndex + 1,
                            questionList.size
                        )
                    )
                }

                GetQuestionsByCategoryUseCase.GetQuestionsByCategoryState.EmptyToken -> getSessionToken()

                GetQuestionsByCategoryUseCase.GetQuestionsByCategoryState.EmptyList -> setEffect(
                    QuizUIEffect.ShowError(stringResourceProvider.getString(R.string.empty_question_list))
                )

                is GetQuestionsByCategoryUseCase.GetQuestionsByCategoryState.Error -> setEffect(
                    QuizUIEffect.ShowError(state.errorMessage)
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun getSessionToken() {
        getSessionTokenUseCase.invoke().onEach {
            when (it) {
                is GetSessionTokenUseCase.GetSessionTokenState.Success -> saveToken(it.token)

                is GetSessionTokenUseCase.GetSessionTokenState.Error -> setEffect(
                    QuizUIEffect.ShowError(
                        it.errorMessage
                    )
                )

                GetSessionTokenUseCase.GetSessionTokenState.EmptyToken -> setEffect(
                    QuizUIEffect.ShowError(
                        stringResourceProvider.getString(R.string.something_went_wrong)
                    )
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun saveToken(token: String) {
        saveTokenUseCase.invoke(token).onEach {
            when (it) {
                SaveTokenUseCase.SaveTokenState.Success -> getTokenFromDataStore(category!!, type!!)
                SaveTokenUseCase.SaveTokenState.Error -> setEffect(
                    QuizUIEffect.ShowError(
                        stringResourceProvider.getString(R.string.something_went_wrong)
                    )
                )
            }
        }.launchIn(viewModelScope)
    }

    companion object {
        private const val QUESTION_INDEX_NINE = 9
        private const val CATEGORY = "category"
        private const val TYPE = "type"
    }
}
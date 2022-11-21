package com.canerture.quizapp.domain.usecase

import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.domain.model.question.QuestionUI
import com.canerture.quizapp.domain.repository.QuestionRepository
import com.canerture.quizapp.mapper.toQuestionUI
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class GetQuestionsByCategoryUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    operator fun invoke(
        category: Int,
        difficulty: String,
        type: String
    ): Flow<Resource<List<QuestionUI>>> = callbackFlow {
        questionRepository.getQuestionsByCategory(category, difficulty, type).collect { result ->
            when (result) {
                is Resource.Success -> {

                    val questions = mutableListOf<QuestionUI>()

                    result.data.questions?.forEach {
                        if (it.question != null) {
                            it.incorrectAnswers?.forEach { incorrectAnswer ->
                                if (incorrectAnswer != null) {
                                    questions.add(it.toQuestionUI())
                                }
                            }
                        }
                    } ?: kotlin.run {
                        trySend(Resource.Error("Empty question list!"))
                    }

                    trySend(Resource.Success(questions))
                }
                is Resource.Error -> {
                    trySend(Resource.Error(result.message))
                }
            }
        }

        awaitClose { channel.close() }
    }
}
package com.canerture.quizapp.domain.usecase

import com.canerture.quizapp.domain.repository.QuestionRepository
import javax.inject.Inject

class SaveTokenUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    suspend operator fun invoke(token: String) = questionRepository.saveToken(token)
}
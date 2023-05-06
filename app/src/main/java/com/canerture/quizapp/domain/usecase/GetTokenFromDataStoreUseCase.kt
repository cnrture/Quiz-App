package com.canerture.quizapp.domain.usecase

import com.canerture.quizapp.domain.repository.QuestionRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetTokenFromDataStoreUseCase @Inject constructor(
    private val questionRepository: QuestionRepository,
) {
    operator fun invoke(): Flow<GetTokenFromDataStoreState> = flow {
        questionRepository.getTokenFromDataStore().collect {
            it?.let {
                emit(GetTokenFromDataStoreState.Success(it))
            } ?: kotlin.run {
                emit(GetTokenFromDataStoreState.EmptyToken)
            }
        }
    }

    sealed class GetTokenFromDataStoreState {
        data class Success(val token: String) : GetTokenFromDataStoreState()
        object EmptyToken : GetTokenFromDataStoreState()
    }
}
package com.canerture.quizapp.domain.usecase

import com.canerture.quizapp.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class GetTokenFromDataStoreUseCase @Inject constructor(
    private val questionRepository: QuestionRepository,
) {
    operator fun invoke(): Flow<GetTokenFromDataStoreState> = callbackFlow {
        questionRepository.getTokenFromDataStore().collect {
            it?.let {
                trySend(GetTokenFromDataStoreState.Success(it))
            } ?: kotlin.run {
                trySend(GetTokenFromDataStoreState.NullToken)
            }
        }
    }

    sealed class GetTokenFromDataStoreState {
        class Success(val token: String) : GetTokenFromDataStoreState()
        object NullToken : GetTokenFromDataStoreState()
    }
}
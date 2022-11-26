package com.canerture.quizapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.common.extension.collect
import com.canerture.quizapp.delegation.viewmodel.VMDelegation
import com.canerture.quizapp.delegation.viewmodel.VMDelegationImpl
import com.canerture.quizapp.domain.usecase.GetSessionTokenUseCase
import com.canerture.quizapp.domain.usecase.GetTokenFromDataStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTokenFromDataStoreUseCase: GetTokenFromDataStoreUseCase,
    private val getSessionTokenUseCase: GetSessionTokenUseCase
) : ViewModel(),
    VMDelegation<HomeUIEffect, HomeEvent, HomeUIState> by VMDelegationImpl(HomeUIState(loadingState = true)) {

    init {

        initViewModel(this)

        getTokenFromDataStore()

        event.collect(viewModelScope) {
            when (it) {
                HomeEvent.PlayClicked -> {
                    setEffect(HomeUIEffect.GoToCategoryScreen)
                }
            }
        }
    }

    private fun getTokenFromDataStore() {
        getTokenFromDataStoreUseCase.invoke().onEach { state ->
            when (state) {
                is GetTokenFromDataStoreUseCase.GetTokenFromDataStoreState.Success -> {
                    setState(HomeUIState(loadingState = false))
                }
                GetTokenFromDataStoreUseCase.GetTokenFromDataStoreState.NullToken -> {
                    getSessionToken()
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getSessionToken() {
        getSessionTokenUseCase.invoke().onEach {
            when (it) {
                is Resource.Success -> {
                    setState(HomeUIState(loadingState = false))
                }
                is Resource.Error -> {
                    setState(HomeUIState(loadingState = false))
                    setEffect(HomeUIEffect.ShowFullScreenError(it.message))
                }
            }
        }.launchIn(viewModelScope)
    }
}
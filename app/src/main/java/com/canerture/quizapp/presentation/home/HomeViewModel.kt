package com.canerture.quizapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.quizapp.R
import com.canerture.quizapp.common.extension.collect
import com.canerture.quizapp.domain.usecase.GetSessionTokenUseCase
import com.canerture.quizapp.domain.usecase.GetTokenFromDataStoreUseCase
import com.canerture.quizapp.domain.usecase.SaveTokenUseCase
import com.canerture.quizapp.infrastructure.provider.StringResourceProvider
import com.canerture.quizapp.presentation.base.viewmodel.VMDelegation
import com.canerture.quizapp.presentation.base.viewmodel.VMDelegationImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTokenFromDataStoreUseCase: GetTokenFromDataStoreUseCase,
    private val getSessionTokenUseCase: GetSessionTokenUseCase,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val stringResourceProvider: StringResourceProvider
) : ViewModel(),
    VMDelegation<HomeUIEffect, HomeEvent, HomeUIState> by VMDelegationImpl(HomeUIState.Loading) {

    init {

        initViewModel(this)

        collectEvent()

        getTokenFromDataStore()
    }

    private fun collectEvent() = event.collect(viewModelScope) {
        when (it) {
            HomeEvent.PlayClicked -> {
                setEffect(HomeUIEffect.GoToCategoryScreen)
            }
        }
    }

    private fun getTokenFromDataStore() {
        getTokenFromDataStoreUseCase.invoke().onEach { state ->
            when (state) {
                is GetTokenFromDataStoreUseCase.GetTokenFromDataStoreState.Success -> {
                    setState(HomeUIState.TokenSuccess)
                }

                GetTokenFromDataStoreUseCase.GetTokenFromDataStoreState.EmptyToken -> {
                    getSessionToken()
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getSessionToken() {
        getSessionTokenUseCase.invoke().onEach {
            when (it) {
                is GetSessionTokenUseCase.GetSessionTokenState.Success -> {
                    saveToken(it.token)
                }

                is GetSessionTokenUseCase.GetSessionTokenState.Error -> {
                    setEffect(HomeUIEffect.ShowError(it.errorMessage))
                }

                GetSessionTokenUseCase.GetSessionTokenState.EmptyToken -> {
                    setEffect(HomeUIEffect.ShowError(stringResourceProvider.getString(R.string.something_went_wrong)))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun saveToken(token: String) = viewModelScope.launch {
        saveTokenUseCase.invoke(token)
    }
}
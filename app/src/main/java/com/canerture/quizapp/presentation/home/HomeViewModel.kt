package com.canerture.quizapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.common.SessionManager
import com.canerture.quizapp.delegation.ViewModelDelegation
import com.canerture.quizapp.delegation.ViewModelDelegationImpl
import com.canerture.quizapp.domain.usecase.GetSessionTokenUseCase
import com.canerture.quizapp.domain.usecase.GetTokenFromDataStoreUseCase
import com.canerture.quizapp.domain.usecase.SaveTokenUseCase
import com.canerture.quizapp.presentation.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getSessionTokenUseCase: GetSessionTokenUseCase,
    private val getTokenFromDataStoreUseCase: GetTokenFromDataStoreUseCase,
    private val saveTokenUseCase: SaveTokenUseCase
) : ViewModel(),
    ViewModelDelegation<HomeUIEffect, HomeEvent, Unit> by ViewModelDelegationImpl() {

    init {

        initViewModel(this)

        getSessionTokenFromDataStore()

        viewModelScope.launch {
            event.collect {
                when (it) {
                    HomeEvent.PlayClicked -> {
                        setEffect(HomeUIEffect.GoToCategoryScreen)
                    }
                    HomeEvent.SendTokenRequest -> {
                        getSessionToken()
                    }
                }
            }
        }
    }

    private fun getSessionTokenFromDataStore() {
        getTokenFromDataStoreUseCase.invoke().onEach {
            when (it) {
                is Resource.Success -> {
                    setState(UIState(loadingState = false))
                    SessionManager.sessionToken = it.data
                }
                is Resource.Error -> getSessionToken()
            }
        }.launchIn(viewModelScope)
    }

    private fun getSessionToken() {
        setState(UIState(loadingState = true))
        getSessionTokenUseCase.invoke().onEach {
            when (it) {
                is Resource.Success -> {
                    setState(UIState(loadingState = false))
                    saveTokenUseCase.invoke(it.data)
                }
                is Resource.Error -> {
                    setState(UIState(loadingState = false))
                    setEffect(HomeUIEffect.ShowFullScreenError("Session Token Not Found!"))
                }
            }
        }.launchIn(viewModelScope)
    }
}
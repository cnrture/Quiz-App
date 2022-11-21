package com.canerture.quizapp.presentation.home

sealed class HomeEvent {
    object PlayClicked : HomeEvent()
    object SendTokenRequest : HomeEvent()
}
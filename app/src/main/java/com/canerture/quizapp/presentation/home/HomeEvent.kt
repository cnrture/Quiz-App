package com.canerture.quizapp.presentation.home

sealed class HomeEvent {
    object SendTokenRequest : HomeEvent()
}
package com.canerture.quizapp.presentation.home

import com.canerture.quizapp.presentation.base.Event

sealed class HomeEvent : Event {
    object PlayClicked : HomeEvent()
}
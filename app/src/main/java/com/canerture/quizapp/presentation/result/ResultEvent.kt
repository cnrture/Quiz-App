package com.canerture.quizapp.presentation.result

import com.canerture.quizapp.presentation.base.viewmodel.Event

sealed class ResultEvent : Event {
    object ContinueClicked : ResultEvent()
}
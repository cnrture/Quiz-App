package com.canerture.quizapp.common.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun <T> Flow<T>.collect(lifecycleOwner: LifecycleOwner, function: (T) -> Unit) {
    lifecycleOwner.lifecycleScope.launchWhenResumed {
        collect {
            function(it)
        }
    }
}

fun <T> Flow<T>.collect(coroutineScope: CoroutineScope, function: (T) -> Unit) {
    coroutineScope.launch {
        collect {
            function(it)
        }
    }
}
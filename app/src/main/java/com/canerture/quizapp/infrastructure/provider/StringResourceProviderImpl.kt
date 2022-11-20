package com.canerture.quizapp.infrastructure.provider

import android.content.Context
import androidx.annotation.StringRes
import com.canerture.quizapp.domain.provider.StringResourceProvider

class StringResourceProviderImpl(private val context: Context) : StringResourceProvider {

    override fun getString(@StringRes stringResId: Int): String {
        return context.getString(stringResId)
    }
}
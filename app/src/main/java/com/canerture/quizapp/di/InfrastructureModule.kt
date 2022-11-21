package com.canerture.quizapp.di

import android.app.Application
import com.canerture.quizapp.infrastructure.provider.StringResourceProvider
import com.canerture.quizapp.infrastructure.provider.StringResourceProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InfrastructureModule {

    @Provides
    @Singleton
    fun provideStringResourceProvider(application: Application): StringResourceProvider {
        return StringResourceProviderImpl(application)
    }
}
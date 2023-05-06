package com.canerture.quizapp.di

import com.canerture.quizapp.data.repository.QuestionsRepositoryImpl
import com.canerture.quizapp.data.source.local.DataStoreDataSource
import com.canerture.quizapp.data.source.remote.QuestionDataSource
import com.canerture.quizapp.domain.repository.QuestionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideQuestionsRepository(
        questionDataSource: QuestionDataSource,
        dataStoreDataSource: DataStoreDataSource
    ): QuestionRepository =
        QuestionsRepositoryImpl(questionDataSource, dataStoreDataSource)
}
package com.canerture.quizapp.di

import android.content.Context
import com.canerture.quizapp.data.source.local.DataStoreDataSourceImpl
import com.canerture.quizapp.data.source.remote.QuestionDataSourceImpl
import com.canerture.quizapp.data.source.remote.QuestionService
import com.canerture.quizapp.data.source.local.DataStoreDataSource
import com.canerture.quizapp.data.source.remote.QuestionDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideRemoteDataSource(questionService: QuestionService): QuestionDataSource =
        QuestionDataSourceImpl(questionService)

    @Provides
    @Singleton
    fun provideDataStoreDataSource(@ApplicationContext context: Context): DataStoreDataSource =
        DataStoreDataSourceImpl(context)
}
package com.canerture.quizapp.data.source.remote

import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.data.model.question.Result
import com.canerture.quizapp.domain.source.remote.QuestionDataSource
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class QuestionDataSourceImpl @Inject constructor(
    private val questionService: QuestionService
) : QuestionDataSource {

    private var compositeDisposable = CompositeDisposable()

    override fun getCategories() = callbackFlow {
        trySend(Resource.Loading)
        compositeDisposable.add(
            questionService.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    trySend(Resource.Success(it))
                }, {
                    trySend(Resource.Error(it.message.orEmpty()))
                })
        )

        awaitClose { channel.close() }
    }

    override fun getQuestionsByCategory(
        category: String,
        difficulty: String,
        type: String
    ): Flow<Resource<Result>> = callbackFlow {
        trySend(Resource.Loading)
        compositeDisposable.add(
            questionService.getQuestionsByCategory(
                category = category,
                difficulty = difficulty,
                type = type
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    trySend(Resource.Success(it))
                }, {
                    trySend(Resource.Error(it.message.orEmpty()))
                })
        )

        awaitClose { channel.close() }
    }

    override fun getSessionToken() = callbackFlow {
        trySend(Resource.Loading)
        compositeDisposable.add(
            questionService.retrieveSessionToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    trySend(Resource.Success(it))
                }, {
                    trySend(Resource.Error(it.message.orEmpty()))
                })
        )

        awaitClose { channel.close() }
    }
}
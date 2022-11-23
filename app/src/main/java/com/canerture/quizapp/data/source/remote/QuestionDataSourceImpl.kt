package com.canerture.quizapp.data.source.remote

import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.data.model.question.Result
import com.canerture.quizapp.domain.source.remote.QuestionDataSource
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class QuestionDataSourceImpl @Inject constructor(
    private val questionService: QuestionService
) : QuestionDataSource {

    private var compositeDisposable = CompositeDisposable()

    override fun getQuestionsByCategory(
        category: Int,
        type: String,
        token: String
    ): Flow<Resource<Result>> = callbackFlow {
        compositeDisposable.add(
            questionService.getQuestionsByCategory(
                category = category,
                type = type,
                token = token
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

    override fun resetSessionToken(token: String) = callbackFlow {
        compositeDisposable.add(
            questionService.resetSessionToken(token = token)
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
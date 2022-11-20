package com.canerture.quizapp.data.source.remote

import com.canerture.quizapp.data.model.category.Category
import com.canerture.quizapp.data.model.token.Token
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface QuestionService {

    @GET("api_token.php?command=request")
    fun retrieveSessionToken(): Observable<Token>

    @GET("api_category.php")
    fun getCategories(): Observable<Category>
}
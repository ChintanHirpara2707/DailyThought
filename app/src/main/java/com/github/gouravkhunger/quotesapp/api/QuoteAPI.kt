package com.github.gouravkhunger.quotesapp.api

import com.github.gouravkhunger.quotesapp.models.Quote
import retrofit2.Response
import retrofit2.http.GET

interface QuoteAPI {

    // full url-> https://zenquotes.io/api/random
    @GET("random")
    suspend fun getRandomQuote(): Response<List<Quote>>

    @GET("today")
    suspend fun getQuoteOfTheDay(): Response<List<Quote>>
}

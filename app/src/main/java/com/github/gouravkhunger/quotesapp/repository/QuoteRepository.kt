package com.github.gouravkhunger.quotesapp.repository

import com.github.gouravkhunger.quotesapp.api.QuoteAPI
import com.github.gouravkhunger.quotesapp.db.QuoteDao
import com.github.gouravkhunger.quotesapp.models.Quote
import com.github.gouravkhunger.quotesapp.store.Preference
import com.github.gouravkhunger.quotesapp.store.PreferenceStore
import javax.inject.Inject

class QuoteRepository @Inject constructor(
    private val dao: QuoteDao,
    private val api: QuoteAPI,
    private val preferenceStore: PreferenceStore
) {

    suspend fun getRandomQuote() = api.getRandomQuote()

    suspend fun getQuoteOfTheDay() = api.getQuoteOfTheDay()

    suspend fun upsert(quote: Quote) = dao.upsert(quote)

    suspend fun deleteQuote(quote: Quote) =
        dao.deleteSavedQuote(quote)

    fun getSavedQuotes() = dao.getSavedQuotes()

    suspend fun <T> getSetting(preference: Preference<T>) = preferenceStore.getPreference(preference)

    suspend fun <T> saveSetting(preference: Preference<T>, value: T) =
        preferenceStore.putPreference(preference, value)
}

package com.github.gouravkhunger.quotesapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.github.gouravkhunger.quotesapp.models.Quote

@Dao
interface QuoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(quote: Quote): Long

    @Query("SELECT * FROM quotes")
    fun getSavedQuotes(): LiveData<List<Quote>>

    @Delete
    suspend fun deleteSavedQuote(quote: Quote)
}

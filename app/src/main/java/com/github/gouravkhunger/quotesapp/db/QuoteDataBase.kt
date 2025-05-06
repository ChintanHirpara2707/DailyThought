package com.github.gouravkhunger.quotesapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.gouravkhunger.quotesapp.models.Quote
@Database(
    entities = [Quote::class],
    version = 1
)
abstract class QuoteDataBase : RoomDatabase() {

    abstract fun getQuoteDao(): QuoteDao
}

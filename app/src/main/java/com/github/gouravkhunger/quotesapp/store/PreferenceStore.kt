package com.github.gouravkhunger.quotesapp.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private const val PREFERENCES_NAME = "settings"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

data class Preference<T>(
    val key: Preferences.Key<T>,
    val default: T
) {
    companion object {
        val ASK_NOTIF_PERM = Preference(booleanPreferencesKey("ask_notif_perm"), true)
        val CHECK_FOR_UPDATES = Preference(booleanPreferencesKey("check_for_updates"), false)
    }
}

interface IPreferenceStore {
    suspend fun <T> putPreference(preference: Preference<T>, value: T)
    suspend fun <T> getPreference(preference: Preference<T>): T
}

class PreferenceStore @Inject constructor(
    private val context: Context
): IPreferenceStore {
    override suspend fun <T> putPreference(preference: Preference<T>, value: T) {
        context.dataStore.edit { prefs ->
            prefs[preference.key] = value
        }
    }

    override suspend fun <T> getPreference(preference: Preference<T>): T {
        val prefs = context.dataStore.data.first()
        return (prefs[preference.key] ?: preference.default)
    }
}

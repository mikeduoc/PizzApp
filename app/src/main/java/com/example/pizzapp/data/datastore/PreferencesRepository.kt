package com.example.pizzapp.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesRepository(private val context: Context) {

    private val TEMA_OSCURO = booleanPreferencesKey("tema_oscuro")
    private val DESCANSO_VISUAL = booleanPreferencesKey("descanso_visual")
    private val ES_ADMIN = booleanPreferencesKey("es_admin")

    val temaOscuroFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[TEMA_OSCURO] ?: false
        }

    suspend fun setTemaOscuro(activado: Boolean) {
        context.dataStore.edit {
            it[TEMA_OSCURO] = activado
        }
    }

    val descansoVisualFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[DESCANSO_VISUAL] ?: false
        }

    suspend fun setDescansoVisual(activado: Boolean) {
        context.dataStore.edit {
            it[DESCANSO_VISUAL] = activado
        }
    }

    val esAdminFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[ES_ADMIN] ?: false
        }

    suspend fun setEsAdmin(esAdmin: Boolean) {
        context.dataStore.edit {
            it[ES_ADMIN] = esAdmin
        }
    }
}
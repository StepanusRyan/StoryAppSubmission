package com.stepanusryan.storyapp.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.stepanusryan.storyapp.model.LoginResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Preference private constructor(private val datastore:DataStore<Preferences>) {
    fun getUser(): Flow<LoginResult>{
        return datastore.data.map {
            LoginResult(
                it[USER_KEY] ?:"",
                it[NAME_KEY] ?:"",
                it[TOKEN_KEY] ?:"",
                it[STATE_KEY] ?: false
            )
        }
    }
    suspend fun saveUser(loginResult: LoginResult){
        datastore.edit {
            it[USER_KEY] = loginResult.userId
            it[NAME_KEY] = loginResult.name
            it[TOKEN_KEY] ?:loginResult.token
            it[STATE_KEY] ?: loginResult.isLogin
        }
    }
    suspend fun login() {
        datastore.edit {
            it[STATE_KEY] = true
        }
    }

    suspend fun logout() {
        datastore.edit {
            it[USER_KEY] = ""
            it[NAME_KEY] = ""
            it[TOKEN_KEY] = ""
            it[STATE_KEY] = false
        }
    }
    companion object {
        @Volatile
        private var INSTANCE: Preference? = null

        private val USER_KEY = stringPreferencesKey("userId")
        private val NAME_KEY = stringPreferencesKey("name")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>): Preference {
            return INSTANCE ?: synchronized(this) {
                val instance = Preference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
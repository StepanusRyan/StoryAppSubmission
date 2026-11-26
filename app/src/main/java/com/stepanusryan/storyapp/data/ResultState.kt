package com.stepanusryan.storyapp.data

import android.util.Log
import com.google.gson.Gson
import com.stepanusryan.storyapp.api.response.GeneralResponse
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException

sealed interface ResultState<out T> {
    data object Loading : ResultState<Nothing>
    data class Success<out T>(val data: T) : ResultState<T>
    data class Error(val message: String?) : ResultState<Nothing>
}

suspend fun <T> resultOf(block: suspend () -> T): ResultState<T> = try {
    ResultState.Success(block())
} catch (e: CancellationException) {
    throw e
} catch (e: HttpException) {
    ResultState.Error(e.serverMessage())
} catch (e: Exception) {
    Log.e("ResultState", "Request failed", e)
    ResultState.Error(null)
}

private fun HttpException.serverMessage(): String? = try {
    response()?.errorBody()?.string()?.let { body ->
        Gson().fromJson(body, GeneralResponse::class.java)?.message
    }
} catch (e: Exception) {
    Log.e("ResultHttp","$e")
    null
}

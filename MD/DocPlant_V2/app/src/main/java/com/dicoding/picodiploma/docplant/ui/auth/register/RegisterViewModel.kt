package com.dicoding.picodiploma.docplant.ui.auth.register

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.docplant.data.remote.api.ApiConfig
import com.dicoding.picodiploma.docplant.data.remote.response.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RegisterViewModel : ViewModel() {
    suspend fun userRegister(
        name: String,
        email: String,
        password: String
    ): Flow<Result<RegisterResponse>> = flow {
        try {
            val response = ApiConfig.getApiService().register(name, email, password)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
}
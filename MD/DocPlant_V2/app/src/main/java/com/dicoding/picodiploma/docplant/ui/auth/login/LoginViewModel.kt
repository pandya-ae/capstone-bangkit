package com.dicoding.picodiploma.docplant.ui.auth.login

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.docplant.data.remote.api.ApiConfig
import com.dicoding.picodiploma.docplant.data.remote.response.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class LoginViewModel : ViewModel() {
    suspend fun userLogin(email: String, password: String): Flow<Result<LoginResponse>> = flow {
        try {
            val response = ApiConfig.getApiService().login(email, password)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
}
package com.gws.networking.repository

import com.gws.networking.api.Api
import dagger.Reusable
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

@Reusable
class UssdRepositoryImpl @Inject constructor(
    private val api: Api
) : UssdRepository {
    override fun login(login: String, password: String): Flow<Boolean> {
        return flow {
//            api.login(login, password)
            emit(true)
        }.catch {
            emit(false)
        }.flowOn(Dispatchers.IO)
    }
}

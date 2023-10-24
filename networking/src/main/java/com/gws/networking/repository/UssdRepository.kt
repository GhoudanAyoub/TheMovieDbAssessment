package com.gws.networking.repository

import kotlinx.coroutines.flow.Flow

interface UssdRepository {
    fun login(login: String, password: String): Flow<Boolean>
}

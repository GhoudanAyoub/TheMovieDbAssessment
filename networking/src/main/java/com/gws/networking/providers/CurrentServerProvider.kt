package com.gws.networking.providers

import com.gws.networking.model.ServerEntity

interface CurrentServerProvider {
    suspend fun currentServer(): ServerEntity?
    suspend fun saveServer(serverEntity: ServerEntity)
}

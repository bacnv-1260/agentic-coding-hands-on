package com.example.saa.data.repository

import io.github.jan.supabase.AccessTokenProvider
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.SupabaseSerializer
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.network.KtorSupabaseHttpClient
import io.github.jan.supabase.plugins.PluginManager
import kotlin.reflect.KType

/**
 * Minimal stub of [SupabaseClient] used only to satisfy the constructor of open data source
 * classes in unit tests. The subclasses override all actual data source methods so none of these
 * members are ever accessed at runtime.
 */
@OptIn(SupabaseInternal::class)
internal object StubSupabaseClient : SupabaseClient {
    override val supabaseUrl: String = "stub.supabase.co"
    override val supabaseHttpUrl: String = "https://stub.supabase.co"
    override val supabaseKey: String = "stub-anon-key"
    override val useHTTPS: Boolean = true
    override val pluginManager: PluginManager = PluginManager(emptyMap())
    override val httpClient: KtorSupabaseHttpClient
        get() = throw UnsupportedOperationException("StubSupabaseClient.httpClient must not be called in unit tests")
    override val defaultSerializer: SupabaseSerializer = object : SupabaseSerializer {
        override fun <T : Any> encode(type: KType, value: T): String = throw UnsupportedOperationException()
        override fun <T : Any> decode(type: KType, value: String): T = throw UnsupportedOperationException()
    }
    override val accessToken: AccessTokenProvider? = null
    override suspend fun close() = Unit
}


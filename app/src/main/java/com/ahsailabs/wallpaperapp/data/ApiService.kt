package com.ahsailabs.wallpaperapp.data

import com.ahsailabs.wallpaperapp.BuildConfig
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*

/**
 * Created by ahmad s on 01/10/21.
 */
object ApiService {
    private fun getAuthString(): String {
        return "563492ad6f9170000100000185a20af81e8d4d418ebbef5d60d197c0"
    }

    val client: HttpClient by lazy {
        HttpClient(CIO) {
            expectSuccess = true
            install(JsonFeature) {
                serializer = GsonSerializer {
                    setPrettyPrinting()
                    disableHtmlEscaping()
                }
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 15_000
                connectTimeoutMillis = 15_000
                socketTimeoutMillis = 15_000
            }
            if(BuildConfig.DEBUG) {
                install(Logging) {
                    logger = Logger.SIMPLE
                    level = LogLevel.ALL
                }
            }
            defaultRequest {
                header("Authorization", getAuthString())
                header("Content-Type", "application/json")
            }
        }
    }

    private const val BASE_URL: String = "https://api.pexels.com/v1/"
    const val searchUrl: String = BASE_URL+"search"
}
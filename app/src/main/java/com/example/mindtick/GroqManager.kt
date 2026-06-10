package com.example.mindtick

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GroqManager {

    private const val API_KEY = ""
    private val retrofit =

        Retrofit.Builder()
            .baseUrl("https://api.groq.com/")
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->

                        val request: Request =

                            chain.request()
                                .newBuilder()
                                .addHeader(
                                    "Authorization",
                                    "Bearer $API_KEY"
                                )
                                .build()

                        chain.proceed(request)
                    }
                    .build()
            )
            .build()

    private val api =
        retrofit.create(
            GroqApiService::class.java
        )

    suspend fun getFeedback(
        prompt: String
    ): String {

        return try {

            val request =

                GroqRequest(
                    model =
                        "llama-3.3-70b-versatile",

                    messages =
                        listOf(
                            Message(
                                "user",
                                prompt
                            )
                        )
                )

            val response =
                api.getChatCompletion(
                    request
                )

            response
                .choices[0]
                .message
                .content

        } catch (e: Exception) {

            "AI 응답 실패 : ${e.message}"
        }
    }
}
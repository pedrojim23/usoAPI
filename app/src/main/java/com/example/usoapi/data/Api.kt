package com.example.usoapi.data

import android.content.Context
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class Api(context: Context) {
    private val builder: Retrofit.Builder = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())

    private val retrofit: Retrofit = builder.build()

    interface RemoteService {
        @GET("pokemon")
        fun loadPokemon(@Query("limit") limit: Int): Call<PokemonResponse>
    }

    fun build(): RemoteService {
        return retrofit.create(RemoteService::class.java)
    }
}

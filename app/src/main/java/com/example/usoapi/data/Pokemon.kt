package com.example.usoapi.data

import com.google.gson.annotations.SerializedName

data class PokemonResponse(
    @SerializedName("count")
    val count:Int,
    @SerializedName("next")
    val next:String,
    @SerializedName("previous")
    val previous:String,
    @SerializedName("results")
    val results:List<Pokemon>
)

data class Pokemon(val name:String, val url:String)
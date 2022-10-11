package com.example.myapplication.interfaces

import com.example.myapplication.models.GameModel
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Headers("x-api-key: MZFZdQWQim4bQAOc2tkGs4mbCwblRm9S4Jawq15e")
    @GET("games")
    fun getGames(@QueryMap filter: HashMap<String, String>): Call<MutableList<GameModel>>

    @Headers("x-api-key: MZFZdQWQim4bQAOc2tkGs4mbCwblRm9S4Jawq15e")
    @GET("games/{id}")
    fun getGamesById(@Path("id") id:String, @QueryMap filter: HashMap<String, String>): Call<MutableList<GameModel>>

    @Headers("x-api-key: MZFZdQWQim4bQAOc2tkGs4mbCwblRm9S4Jawq15e")
    @GET("games")
    fun getGamesBySearch(@Query("search") searchText: String, @QueryMap filter: HashMap<String, String>): Call<MutableList<GameModel>>

}
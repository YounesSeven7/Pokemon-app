package com.example.pokemonapp.fearure_pokemon.data.dataSource


import com.example.pokemonapp.fearure_pokemon.domain.model.pokemon.Pokemon
import com.example.pokemonapp.fearure_pokemon.domain.model.pokemon_list.PokemonList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApi {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): com.example.pokemonapp.fearure_pokemon.domain.model.pokemon_list.PokemonList

    @GET("pokemon/{name}")
    suspend fun getPokemonInfo(
        @Path("name") name: String
    ): com.example.pokemonapp.fearure_pokemon.domain.model.pokemon.Pokemon
}
package com.example.pokemonapp.fearure_pokemon.data.repository

import com.example.pokemonapp.fearure_pokemon.data.dataSource.PokeApi
import com.example.pokemonapp.fearure_pokemon.domain.model.pokemon.Pokemon
import com.example.pokemonapp.fearure_pokemon.domain.model.pokemon_list.PokemonList
import com.example.pokemonapp.fearure_pokemon.domain.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject


class PokeRepository constructor(
    private val api: PokeApi
) {
    suspend fun getPokemonList(limit: Int, offset:Int): Resource<PokemonList> {
        val response = try {
            api.getPokemonList(limit, offset)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred.")
        }
        return Resource.Success(response)
    }

    suspend fun getPokemonInfo(name: String): Resource<Pokemon> {
        val response = try {
            api.getPokemonInfo(name)
        } catch (e: Exception) {
            return Resource.Error("There is no pokemon have this name.")
        }
        return Resource.Success(response)
    }
}
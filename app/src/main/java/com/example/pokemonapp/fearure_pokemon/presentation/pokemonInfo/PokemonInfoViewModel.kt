package com.example.pokemonapp.fearure_pokemon.presentation.pokemonInfo

import androidx.lifecycle.ViewModel
import com.example.pokemonapp.fearure_pokemon.data.repository.PokeRepository
import com.example.pokemonapp.fearure_pokemon.domain.model.pokemon.Pokemon
import com.example.pokemonapp.fearure_pokemon.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonInfoViewModel @Inject constructor(
    private val repository: PokeRepository
): ViewModel() {

    suspend fun getPokemonInfo(pokemonName: String) :Resource<Pokemon> {
        return repository.getPokemonInfo(name = pokemonName)
    }

}

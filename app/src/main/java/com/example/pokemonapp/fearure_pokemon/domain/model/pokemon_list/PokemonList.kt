package com.example.pokemonapp.fearure_pokemon.domain.model.pokemon_list

data class PokemonList(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<com.example.pokemonapp.fearure_pokemon.domain.model.pokemon_list.Result>
)
package com.example.pokemonapp.fearure_pokemon.domain.model.pokemon

data class Ability(
    val ability: com.example.pokemonapp.fearure_pokemon.domain.model.pokemon.AbilityX,
    val isHidden: Boolean,
    val slot: Int
)
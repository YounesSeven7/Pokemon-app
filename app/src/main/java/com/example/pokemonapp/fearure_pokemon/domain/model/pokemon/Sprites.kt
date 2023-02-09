package com.example.pokemonapp.fearure_pokemon.domain.model.pokemon

data class Sprites(
    val backDefault: String,
    val backFemale: Any,
    val backShiny: String,
    val backShinyFemale: Any,
    val frontDefault: String,
    val frontFemale: Any,
    val frontShiny: String,
    val frontShinyFemale: Any,
    val other: com.example.pokemonapp.fearure_pokemon.domain.model.pokemon.Other,
    val versions: com.example.pokemonapp.fearure_pokemon.domain.model.pokemon.Versions
)
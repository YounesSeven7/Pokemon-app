package com.example.pokemonapp.fearure_pokemon.domain.model.pokemon

data class VersionGroupDetail(
    val levelLearnedAt: Int,
    val moveLearnMethod: com.example.pokemonapp.fearure_pokemon.domain.model.pokemon.MoveLearnMethod,
    val versionGroup: com.example.pokemonapp.fearure_pokemon.domain.model.pokemon.VersionGroup
)
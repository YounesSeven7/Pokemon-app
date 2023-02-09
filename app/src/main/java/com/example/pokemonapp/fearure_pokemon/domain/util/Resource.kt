package com.example.pokemonapp.fearure_pokemon.domain.util

sealed class Resource<T> (val data: T? = null, val message: String? = null) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(message: String): Resource<T>(message = message)
    class Logging<T>(): Resource<T>()
}

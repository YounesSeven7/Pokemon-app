package com.example.pokemonapp.fearure_pokemon.presentation.pokemon_list

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.pokemonapp.fearure_pokemon.domain.util.Constants.PAGE_SIZE
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.pokemonapp.fearure_pokemon.domain.model.PokemonItemEntry
import com.example.pokemonapp.fearure_pokemon.data.repository.PokeRepository
import com.example.pokemonapp.fearure_pokemon.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokeRepository
): ViewModel() {

    private var curPage = 0

    var pokemonList = mutableStateOf<List<PokemonItemEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private var cachedPokemonList = listOf<PokemonItemEntry>()
    private var isSearchStarting = true
    var isSearching = mutableStateOf(false)

    init {
        loadPokemonPaginated()
    }

    fun searchPokemonList(query: String) {
        val listToSearch = if(isSearchStarting) {
            Log.d("younes", "younes1")
            pokemonList.value
        } else {
            cachedPokemonList
        }
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                pokemonList.value = cachedPokemonList
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }
            val result = listToSearch.filter {
                it.pokemonName.contains(query.trim(), ignoreCase = true) ||
                        it.number.toString() == query.trim()
            }
            if (isSearchStarting) {
                cachedPokemonList = pokemonList.value
                isSearchStarting = false
            }
            pokemonList.value = result
            isSearching.value = true
        }
    }

    fun loadPokemonPaginated() {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getPokemonList(PAGE_SIZE, curPage * PAGE_SIZE)
            when(result) {
                is Resource.Success -> {
                    endReached.value = curPage * PAGE_SIZE >= result.data!!.count
                    pokemonList.value += getPokemonList(result)
                    curPage++
                    loadError.value = ""
                    isLoading.value = false
                }
                is Resource.Error -> {
                    Log.d("younes", result.message!!)
                    loadError.value = result.message!!
                    isLoading.value = false
                }
                is Resource.Logging -> {

                }
            }
        }
    }

    fun getPokemonList(result: Resource.Success<com.example.pokemonapp.fearure_pokemon.domain.model.pokemon_list.PokemonList>): List<PokemonItemEntry> {
        return result.data!!.results.mapIndexed { index, item ->
            val number = if (item.url.endsWith("/")) {
                item.url.dropLast(1).takeLastWhile { it.isDigit() }
            } else {
                item.url.takeLastWhile { it.isDigit() }
            }
            val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
            PokemonItemEntry(
                item.name.replaceFirstChar { it.titlecase(Locale.ROOT) },
                url,
                number.toInt()
            )
        }
    }


    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bmp).generate {
            it?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}
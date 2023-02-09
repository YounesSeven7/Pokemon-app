package com.example.pokemonapp.fearure_pokemon.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.toLowerCase
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokemonapp.fearure_pokemon.presentation.pokemonInfo.PokemonInfoScreen
import com.example.pokemonapp.fearure_pokemon.presentation.pokemon_list.PokemonListScreen

import com.example.pokemonapp.ui.theme.PokemonAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    public companion object {
        const val POKEMON_LIST_SCREEN = "POKEMON_LIST"
        const val POKEMON_INFO_SCREEN = "POKEMON_INFO"

        private const val POKEMON_NAME_PRAM = "POKEMON_NAME"
        private const val DOMINANT_COLOR_PRAM = "DOMINANT_COLOR"
        private const val POKEMON_NUMBER_PRAM = "POKEMON_NUMBER"

    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokemonAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = POKEMON_LIST_SCREEN) {
                    composable(route = POKEMON_LIST_SCREEN) {
                        PokemonAppTheme {
                            PokemonListScreen(navController = navController)
                        }
                    }
                    composable(
                        route = "$POKEMON_INFO_SCREEN" +
                                "/{$POKEMON_NAME_PRAM}" +
                                "/{$DOMINANT_COLOR_PRAM}" +
                                "/{$POKEMON_NUMBER_PRAM}",
                        arguments = listOf(
                            navArgument(POKEMON_NAME_PRAM) { type = NavType.StringType},
                            navArgument(DOMINANT_COLOR_PRAM) { type = NavType.IntType },
                            navArgument(POKEMON_NUMBER_PRAM) { type = NavType.IntType }
                        )
                    ) {
                        val pokemonName = remember { it.arguments?.getString(POKEMON_NAME_PRAM) ?: "" }.lowercase()
                        val dominantColor = remember {
                          val color = it.arguments?.getInt(DOMINANT_COLOR_PRAM)
                          color?.let { Color(it) }  ?: Color.White
                        }
                        val pokemonNumber = remember {
                            it.arguments?.getInt(POKEMON_NUMBER_PRAM) ?: 1
                        }

                        PokemonInfoScreen(
                            pokemonName = pokemonName,
                            dominantColor = dominantColor,
                            navController = navController,
                            number = pokemonNumber
                        )
                    }
                }
            }
        }
    }
}

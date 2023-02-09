package com.example.pokemonapp.di

import com.example.pokemonapp.fearure_pokemon.data.dataSource.PokeApi
import com.example.pokemonapp.fearure_pokemon.data.repository.PokeRepository
import com.example.pokemonapp.fearure_pokemon.domain.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun providePokemonRepository(
        api: PokeApi
    ) = PokeRepository(api)

    @Singleton
    @Provides
    fun providePokeApi(): PokeApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PokeApi::class.java)

    }
}
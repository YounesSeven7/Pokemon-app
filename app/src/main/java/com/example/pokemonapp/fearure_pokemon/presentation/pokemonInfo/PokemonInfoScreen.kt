package com.example.pokemonapp.fearure_pokemon.presentation.pokemonInfo

import android.app.Activity
import android.os.Build
import android.util.Log
import android.view.View
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LineWeight
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pokemonapp.R
import com.example.pokemonapp.fearure_pokemon.domain.model.pokemon.Pokemon
import com.example.pokemonapp.fearure_pokemon.domain.model.pokemon.Type
import com.example.pokemonapp.fearure_pokemon.domain.util.Resource
import com.example.pokemonapp.fearure_pokemon.presentation.util.parseStatToAbbr
import com.example.pokemonapp.fearure_pokemon.presentation.util.parseStatToColor
import com.example.pokemonapp.fearure_pokemon.presentation.util.parseTypeToColor
import com.example.pokemonapp.ui.theme.DarkColorScheme
import com.example.pokemonapp.ui.theme.LightColorScheme
import com.example.pokemonapp.ui.theme.Typography
import java.util.Locale
import kotlin.math.log
import kotlin.math.round


@Composable
fun PokemonInfoScreen(
    pokemonName: String,
    dominantColor: Color,
    number: Int,
    navController: NavController,
    topPadding: Dp = 20.dp,
    pokemonImageSize: Dp = 200.dp,
    viewModel: PokemonInfoViewModel = hiltViewModel()
) {
    val pokemonInfo = produceState<Resource<Pokemon>>(initialValue = Resource.Logging()) {
        value = viewModel.getPokemonInfo(pokemonName)
    }.value

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Black.toArgb()
            window.navigationBarColor = dominantColor.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dominantColor)
            .padding(bottom = 16.dp)
    ) {
        PokemonInfoTopSection(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black,
                            Color.Transparent
                        )
                    )
                )
        )
        PokemonInfoStateWrapper(
            pokemonInfo = pokemonInfo,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding + pokemonImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .shadow(10.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(colorScheme.surface)
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            loadingModifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
                .padding(
                    top = topPadding + pokemonImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )

        )
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            if (pokemonInfo is Resource.Success) {
                pokemonInfo.data?.let {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${it.id}.png")
                            .crossfade(true)
                            .build(),
                        contentDescription = pokemonInfo.data.name,
                        modifier = Modifier
                            .size(pokemonImageSize)
                            .offset(y = topPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PokemonInfoTopSection(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(36.dp)
                .offset(16.dp, 16.dp)
                .clickable {
                    navController.popBackStack()
                }
        )
    }
}

@Composable
fun PokemonInfoStateWrapper(
    pokemonInfo: Resource<Pokemon>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier
) {
    when(pokemonInfo) {
        is Resource.Success -> {
            PokemonInfoSection(
                pokemonInfo = pokemonInfo.data!!,
                modifier = modifier
                    .offset(y = (-20).dp)
                )
        }
        is Resource.Error -> {
            Text(
                text = pokemonInfo.message!!,
                color = Color.Red,
                modifier = modifier
            )
        }
        is Resource.Logging -> {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = loadingModifier
            )

        }
    }
}

@Composable
fun PokemonInfoSection(
    pokemonInfo: Pokemon,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .offset(y = 100.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "#${pokemonInfo.id} ${pokemonInfo.name.replaceFirstChar { it.titlecase(Locale.ROOT) }}",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            color = colorScheme.onSurface
        )
        PokemonTypeSection(types = pokemonInfo.types)
        PokemonInfoDataSection(pokemonWeight = pokemonInfo.weight, pokemonHeight = pokemonInfo.height)
        PokemonBaseState(pokemonInfo = pokemonInfo)
    }
}

@Composable
fun PokemonTypeSection(types: List<Type>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
    ) {
        for (type in types) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .clip(CircleShape)
                    .background(parseTypeToColor(type))
                    .height(35.dp)
            ) {
                Text(
                    text = type.type.name.replaceFirstChar { it.titlecase() },
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun PokemonInfoDataSection(
    pokemonWeight: Int,
    pokemonHeight: Int,
    sectionHeight: Dp = 80.dp
) {
    val pokemonWeightInKg = remember {
        pokemonWeight / 10f
    }
    val pokemonHeightInMeters = remember {
        pokemonHeight /10f
    }
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        PokemonInfoDataItem(
            dataValue = pokemonWeightInKg, 
            dataUnit = "kg", 
            dataIcon = painterResource(id = R.drawable.ic_weight),
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier
            .size(1.dp, sectionHeight)
            .background(Color.LightGray)
        )
        PokemonInfoDataItem(
            dataValue = pokemonHeightInMeters,
            dataUnit = "m",
            dataIcon = painterResource(id = R.drawable.ic_height),
            modifier = Modifier.weight(1f))
    }
}

@Composable
fun PokemonInfoDataItem(
    dataValue: Float,
    dataUnit: String,
    dataIcon: Painter,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Icon(painter = dataIcon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "$dataValue$dataUnit",
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}


@Composable
fun PokemonBaseState(
    pokemonInfo: Pokemon,
    animDelayPerPerItem: Int = 100
) {
    val maxBaseStat = remember {
        pokemonInfo.stats.maxOf { it.baseStat }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "BaseState:",
            fontSize = 20.sp,
            color = colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(4.dp))
        // there is a problem in value stat.baseStat the value came equal 0
        // maybe there are some changes in the api ,however you can test the animation by
        // give statValue parameter initial value or use another api .

        for (indic in pokemonInfo.stats.indices) {
            val stat = pokemonInfo.stats[indic]
            PokemonStat(
                stateName = parseStatToAbbr(stat),
                statValue = stat.baseStat,
                statMaxValue = maxBaseStat,
                stateColor = parseStatToColor(stat),
                animationDelay = 2 * animDelayPerPerItem
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun PokemonStat(
    stateName: String,
    statValue: Int,
    statMaxValue: Int,
    stateColor: Color,
    height: Dp = 28.dp,
    animationDuration: Int = 1000,
    animationDelay: Int = 100
) {



    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val curPercent = animateFloatAsState(
        targetValue = if (animationPlayed) { statValue / statMaxValue.toFloat() }
        else 0.0f,
        animationSpec = tween(
            animationDuration,
            animationDelay
        )

    )
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(CircleShape)
            .background(
                if (isSystemInDarkTheme()) {
                    Color(0xFF505050)
                } else {
                    Color.LightGray
                }
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(curPercent.value)
                .clip(CircleShape)
                .background(stateColor)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = stateName,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${(curPercent.value * statMaxValue).toInt()}",
                fontWeight = FontWeight.Bold
            )
        }
    }



}
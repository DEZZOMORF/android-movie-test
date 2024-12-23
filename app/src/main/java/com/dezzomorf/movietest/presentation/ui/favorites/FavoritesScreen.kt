package com.dezzomorf.movietest.presentation.ui.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dezzomorf.movietest.R
import com.dezzomorf.movietest.domain.model.Movie
import com.dezzomorf.movietest.presentation.components.MovieDateItem
import com.dezzomorf.movietest.presentation.components.MovieItem
import com.dezzomorf.movietest.presentation.ui.theme.MovieTestTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    FavoritesScreen(
        modifier = modifier,
        uiState = uiState,
        onToggleFavorite = viewModel::onToggleFavorite
    )
}

@Composable
private fun FavoritesScreen(
    modifier: Modifier = Modifier,
    uiState: FavoritesUiState,
    onToggleFavorite: (movieId: Long, currentIsFavorite: Boolean) -> Unit,
) {
    if (uiState.isEmpty) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = stringResource(R.string.no_favorites_yet))
        }
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            uiState.favoritesByMonth.forEach { (monthLabel, movies) ->
                item {
                    MovieDateItem(
                        date = monthLabel
                    )
                }
                items(movies) { movie ->
                    MovieItem(
                        movie = movie,
                        onToggleFavorite = {
                            onToggleFavorite(movie.id, movie.isFavorite)
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Preview
@Composable
private fun FavoritesScreenPreview() {
    MovieTestTheme {
        FavoritesScreen(
            uiState = FavoritesUiState(
                favoritesByMonth = mapOf(
                    "Feb 2024" to listOf(Movie.mock, Movie.mock),
                    "Apr 2024" to listOf(Movie.mock, Movie.mock),
                )
            ),
            onToggleFavorite = { _, _ -> }
        )
    }
}

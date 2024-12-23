package com.dezzomorf.movietest.presentation.ui.all

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dezzomorf.movietest.R
import com.dezzomorf.movietest.domain.model.Movie
import com.dezzomorf.movietest.presentation.components.MovieDateItem
import com.dezzomorf.movietest.presentation.components.MovieItem
import com.dezzomorf.movietest.presentation.ui.theme.MovieTestTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun AllMoviesScreen(
    modifier: Modifier = Modifier,
    viewModel: AllMoviesViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    AllMoviesScreen(
        modifier = modifier,
        uiState = uiState,
        onRefresh = viewModel::refreshMovies,
        onToggleFavorite = viewModel::onToggleFavorite,
        loadMore = viewModel::loadMore
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AllMoviesScreen(
    modifier: Modifier = Modifier,
    uiState: AllMoviesUiState,
    onRefresh: () -> Unit,
    onToggleFavorite: (movieId: Long, currentIsFavorite: Boolean) -> Unit,
    loadMore: () -> Unit,
) {
    val pullRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        modifier = modifier.fillMaxSize(),
        state = pullRefreshState,
        isRefreshing = uiState.isRefreshing,
        onRefresh = onRefresh,
    ) {
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.wrapContentSize())
                }
            }

            uiState.isError -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = stringResource(R.string.error_occurred))
                }
            }

            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    uiState.moviesByMonth.forEach { (monthLabel, movies) ->
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
                    item {
                        if (uiState.isLoadingMore) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        } else {
                            LaunchedEffect(Unit) {
                                loadMore()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun AllMoviesScreenPreview() {
    MovieTestTheme {
        AllMoviesScreen(
            uiState = AllMoviesUiState(
                moviesByMonth = mapOf(
                    "Feb 2024" to listOf(Movie.mock, Movie.mock),
                    "Apr 2024" to listOf(Movie.mock, Movie.mock),
                )
            ),
            onRefresh = {},
            loadMore = {},
            onToggleFavorite = { _, _ -> }
        )
    }
}

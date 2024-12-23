package com.dezzomorf.movietest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dezzomorf.movietest.domain.model.Movie
import com.dezzomorf.movietest.domain.usecase.GetFavouriteMoviesUseCase
import com.dezzomorf.movietest.domain.usecase.ToggleFavoriteUseCase
import com.dezzomorf.movietest.presentation.ui.favorites.FavoritesUiState
import com.dezzomorf.movietest.presentation.ui.favorites.FavoritesViewModel
import com.dezzomorf.movietest.utils.ResourceManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FavoritesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getFavouriteMoviesUseCase = mockk<GetFavouriteMoviesUseCase>()
    private val toggleFavoriteUseCase = mockk<ToggleFavoriteUseCase>()
    private val resourceManager = mockk<ResourceManager>()

    private lateinit var viewModel: FavoritesViewModel

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { resourceManager.getString(any()) } returns "some string"
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `uiState is updated with movies from getFavouriteMoviesUseCase`() = runTest {
        val favoriteMovies = listOf(Movie.mock, Movie.mock)
        coEvery { getFavouriteMoviesUseCase.execute() } returns flowOf(favoriteMovies)

        viewModel = FavoritesViewModel(
            getFavouriteMoviesUseCase,
            toggleFavoriteUseCase,
            resourceManager
        )

        val uiStates = mutableListOf<FavoritesUiState>()
        val job = launch {
            viewModel.uiState.toList(uiStates)
        }

        advanceUntilIdle()

        Assert.assertTrue(uiStates.last().favoritesByMonth.isNotEmpty())
        Assert.assertFalse(uiStates.last().isEmpty)

        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onToggleFavorite invokes toggleFavoriteUseCase with correct args`() = runTest {
        coEvery { getFavouriteMoviesUseCase.execute() } returns emptyFlow()
        coEvery { toggleFavoriteUseCase.execute(any(), any()) } returns Unit
        viewModel = FavoritesViewModel(
            getFavouriteMoviesUseCase,
            toggleFavoriteUseCase,
            resourceManager
        )
        viewModel.onToggleFavorite(movieId = 123L, currentIsFav = true)
        advanceUntilIdle()


        coVerify {
            toggleFavoriteUseCase.execute(123L, false)
        }
    }
}

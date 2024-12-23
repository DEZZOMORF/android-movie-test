package com.dezzomorf.movietest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dezzomorf.movietest.domain.model.Movie
import com.dezzomorf.movietest.domain.usecase.FetchMoviesUseCase
import com.dezzomorf.movietest.domain.usecase.GetMoviesUseCase
import com.dezzomorf.movietest.domain.usecase.ToggleFavoriteUseCase
import com.dezzomorf.movietest.presentation.ui.all.AllMoviesUiState
import com.dezzomorf.movietest.presentation.ui.all.AllMoviesViewModel
import com.dezzomorf.movietest.utils.ResourceManager
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class AllMoviesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getMoviesUseCase = mockk<GetMoviesUseCase>()
    private val toggleFavoriteUseCase = mockk<ToggleFavoriteUseCase>()
    private val fetchMoviesUseCase = mockk<FetchMoviesUseCase>()
    private val resourceManager = mockk<ResourceManager>()

    private lateinit var viewModel: AllMoviesViewModel

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
    fun `init calls executeMovies and loadFirstPage, updating uiState when movies arrive (success path)`() = runTest {
        val fakeMovies = listOf(Movie.mock, Movie.mock)
        coEvery { getMoviesUseCase.execute() } returns flowOf(fakeMovies)
        coEvery { fetchMoviesUseCase.fetchMovies(1) } returns Result.success(Unit)

        viewModel = AllMoviesViewModel(
            getMoviesUseCase,
            toggleFavoriteUseCase,
            fetchMoviesUseCase,
            resourceManager
        )

        val states = mutableListOf<AllMoviesUiState>()
        val job = launch { viewModel.uiState.toList(states) }

        advanceUntilIdle()

        val finalState = states.last()
        Assert.assertFalse(finalState.isLoading)
        Assert.assertFalse(finalState.isError)
        Assert.assertTrue(finalState.moviesByMonth.isNotEmpty())
        Assert.assertEquals(1, finalState.currentPage)

        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `init calls loadFirstPage, sets isError when fetchMovies fails`() = runTest {
        coEvery { getMoviesUseCase.execute() } returns flowOf(emptyList())
        coEvery { fetchMoviesUseCase.fetchMovies(1) } returns Result.failure(Exception())

        viewModel = AllMoviesViewModel(
            getMoviesUseCase,
            toggleFavoriteUseCase,
            fetchMoviesUseCase,
            resourceManager
        )

        val states = mutableListOf<AllMoviesUiState>()
        val job = launch { viewModel.uiState.toList(states) }

        advanceUntilIdle()

        val finalState = states.last()
        Assert.assertFalse(finalState.isLoading)
        Assert.assertTrue(finalState.isError)
        Assert.assertEquals(0, finalState.currentPage)

        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `refreshMovies sets isRefreshing, calls fetchMovies(1), updates state on success`() = runTest {
        coEvery { getMoviesUseCase.execute() } returns flowOf(emptyList())
        coEvery { fetchMoviesUseCase.fetchMovies(1) } returns Result.success(Unit)

        viewModel = AllMoviesViewModel(
            getMoviesUseCase,
            toggleFavoriteUseCase,
            fetchMoviesUseCase,
            resourceManager
        )

        val states = mutableListOf<AllMoviesUiState>()
        val job = launch { viewModel.uiState.toList(states) }

        viewModel.refreshMovies()
        advanceUntilIdle()

        val finalState = states.last()
        Assert.assertFalse(finalState.isRefreshing)
        Assert.assertFalse(finalState.isError)

        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `refreshMovies updates state on failure`() = runTest {
        coEvery { getMoviesUseCase.execute() } returns flowOf(emptyList())
        coEvery { fetchMoviesUseCase.fetchMovies(1) } returns Result.failure(Exception())

        viewModel = AllMoviesViewModel(
            getMoviesUseCase,
            toggleFavoriteUseCase,
            fetchMoviesUseCase,
            resourceManager
        )

        val states = mutableListOf<AllMoviesUiState>()
        val job = launch { viewModel.uiState.toList(states) }

        viewModel.refreshMovies()
        advanceUntilIdle()

        val finalState = states.last()
        Assert.assertFalse(finalState.isRefreshing)
        Assert.assertTrue(finalState.isError)

        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadMore increments currentPage on success`() = runTest {
        coEvery { getMoviesUseCase.execute() } returns flowOf(emptyList())
        coEvery { fetchMoviesUseCase.fetchMovies(1) } returns Result.success(Unit)
        coEvery { fetchMoviesUseCase.fetchMovies(2) } returns Result.success(Unit)

        viewModel = AllMoviesViewModel(
            getMoviesUseCase,
            toggleFavoriteUseCase,
            fetchMoviesUseCase,
            resourceManager
        )

        val states = mutableListOf<AllMoviesUiState>()
        val job = launch { viewModel.uiState.toList(states) }

        advanceUntilIdle()

        Assert.assertEquals(1, states.last().currentPage)

        viewModel.loadMore()
        advanceUntilIdle()

        val finalState = states.last()
        Assert.assertEquals(2, finalState.currentPage)
        Assert.assertFalse(finalState.isLoadingMore)
        Assert.assertFalse(finalState.isError)

        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadMore sets isError if fetch fails`() = runTest {
        coEvery { getMoviesUseCase.execute() } returns flowOf(emptyList())
        coEvery { fetchMoviesUseCase.fetchMovies(1) } returns Result.success(Unit)
        coEvery { fetchMoviesUseCase.fetchMovies(2) } returns Result.failure(Exception())

        viewModel = AllMoviesViewModel(
            getMoviesUseCase,
            toggleFavoriteUseCase,
            fetchMoviesUseCase,
            resourceManager
        )

        val states = mutableListOf<AllMoviesUiState>()
        val job = launch { viewModel.uiState.toList(states) }

        advanceUntilIdle()

        viewModel.loadMore()
        advanceUntilIdle()

        val finalState = states.last()
        Assert.assertTrue(finalState.isError)
        Assert.assertFalse(finalState.isLoadingMore)
        Assert.assertEquals(1, finalState.currentPage)

        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onToggleFavorite calls toggleFavoriteUseCase with expected args`() = runTest {
        coEvery { getMoviesUseCase.execute() } returns flowOf(emptyList())
        coEvery { fetchMoviesUseCase.fetchMovies(1) } returns Result.success(Unit)
        coEvery { toggleFavoriteUseCase.execute(any(), any()) } returns Unit

        viewModel = AllMoviesViewModel(
            getMoviesUseCase,
            toggleFavoriteUseCase,
            fetchMoviesUseCase,
            resourceManager
        )

        viewModel.onToggleFavorite(movieId = 42L, currentIsFavorite = true)
        advanceUntilIdle()

        coVerify {
            toggleFavoriteUseCase.execute(42L, false)
        }
    }
}
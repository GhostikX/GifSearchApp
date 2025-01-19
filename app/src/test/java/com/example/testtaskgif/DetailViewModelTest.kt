package com.example.testtaskgif

import com.example.testtaskgif.data.repository.IGifRepository
import com.example.testtaskgif.domain.models.GifDetail
import com.example.testtaskgif.ui.viewmodel.DetailViewModel
import com.example.testtaskgif.utils.NetworkMonitor
import com.example.testtaskgif.utils.ResponseState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {

    @Mock
    private lateinit var repository: IGifRepository

    @Mock
    private lateinit var networkMonitor: NetworkMonitor

    private lateinit var detailViewModel: DetailViewModel

    @Before
    fun setUp() {
        whenever(networkMonitor.isInternetConnected).thenReturn(MutableStateFlow(true))
        detailViewModel = DetailViewModel(repository, networkMonitor)
    }

    @Test
    fun `fetchDetails emits Loading then Success`() = runTest {
        // Given
        val gifId = "gif123"
        val gifDetail = GifDetail(id = gifId, title = "Test Gif", originalUrl = "test_url", sourceUrl = "test_url", size = 1, description = "")
        whenever(repository.findGifById(gifId)).thenReturn(flowOf(ResponseState.Success(gifDetail)))

        // When
        detailViewModel.fetchDetails(gifId)

        // Assert
        val states = detailViewModel.detailState.take(2).toList()
        assert(states[0] is ResponseState.Loading)
        assert(states[1] is ResponseState.Success && (states[1] as ResponseState.Success).data == gifDetail)
    }

    @Test
    fun `fetchDetails emits Loading then Error`() = runTest {
        // Given
        val gifId = "gif123"
        val errorMessage = "Something went wrong"
        whenever(repository.findGifById(gifId)).thenReturn(flowOf(ResponseState.Error(Throwable(errorMessage))))

        // When
        detailViewModel.fetchDetails(gifId)

        // Assert
        val states = detailViewModel.detailState.take(2).toList()
        assert(states[0] is ResponseState.Loading)
        assert(states[1] is ResponseState.Error && (states[1] as ResponseState.Error).error?.message == errorMessage)
    }

    @Test
    fun `fetchDetails with empty gifId does not call repository`() = runTest {
        // Given
        val gifId = ""

        // When
        detailViewModel.fetchDetails(gifId)

        // Assert
        verify(repository, times(0)).findGifById(anyString())
        assert(detailViewModel.detailState.value is ResponseState.Error)
    }

    @Test
    fun `retry calls fetchDetails with lastGifId`() = runTest {
        // Given
        val gifId = "123"
        val gifDetail = GifDetail(
            id = gifId,
            title = "Test Gif",
            originalUrl = "test_url",
            sourceUrl = "test_url",
            size = 1,
            description = ""
        )
        whenever(repository.findGifById(gifId)).thenReturn(flowOf(ResponseState.Success(gifDetail)))

        // When
        detailViewModel.fetchDetails(gifId)
        advanceUntilIdle()
        detailViewModel.retry()
        advanceUntilIdle()

        // Assert
        verify(repository, times(2)).findGifById(gifId)
    }

}

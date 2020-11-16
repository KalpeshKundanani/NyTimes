package com.kalpeshkundanani.nytimes.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kalpeshkundanani.nytimes.BuildConfig
import com.kalpeshkundanani.nytimes.data.MockNewsRepository
import com.kalpeshkundanani.nytimes.data.enums.NewsPeriod
import com.kalpeshkundanani.nytimes.data.models.NewsResult
import com.kalpeshkundanani.nytimes.data.network.NetworkServicesProvider
import com.kalpeshkundanani.nytimes.data.network.services.MostPopularNewsService
import junit.framework.Assert.*
import org.junit.*
import org.junit.Assert.assertNotEquals
import org.junit.rules.TestRule
import org.junit.runners.MethodSorters


/**
 * Created by Kalpesh Kundanani on 16/11/20.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class NewsViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private var newsViewModel: NewsViewModel? = null
    private var newsRepository: MockNewsRepository? = null

    @Before
    fun setUp() {
        newsViewModel = NewsViewModel()

        val apiKey: String = BuildConfig.NY_TIMES_API_KEY
        val baseUrl: String = BuildConfig.NY_TIMES_BASE_URL
        val provider = NetworkServicesProvider(baseUrl)
        val service: MostPopularNewsService = provider.getMostPopularNewsService()!!

        // repository with mocked behaviour.
        newsRepository = MockNewsRepository.getInstance(service, apiKey)!!
        newsViewModel?.init(newsRepository!!)

        // this is so that live data keeps updating the value and
        // not wait for subscriber to get associated.
        newsViewModel?.newsResultsLiveData?.observeForever {}
        newsViewModel?.isLoadingLiveData?.observeForever {}
        newsViewModel?.networkErrorLiveData?.observeForever {}
        newsViewModel?.newsPeriodMutableLiveData?.observeForever {}
    }

    @After
    fun tearDown() {
        newsViewModel = null
    }

    @Test
    fun fetchNews_MockingState_Success() {
        newsRepository?.mockingState = MockNewsRepository.MockingState.Success
        newsViewModel?.fetchNews(NewsPeriod.WEEK)

        assertNotNull(newsViewModel?.newsResultsLiveData?.value)
        assertEquals(newsViewModel?.isLoadingLiveData?.value, false)
        assertEquals(newsViewModel?.networkErrorLiveData?.value, false)
    }

    @Test
    fun fetchNews_MockingState_IOException() {
        newsRepository?.mockingState = MockNewsRepository.MockingState.IOException
        newsViewModel?.fetchNews(NewsPeriod.DAY)

        assertNull(newsViewModel?.newsResultsLiveData?.value)
        assertEquals(newsViewModel?.isLoadingLiveData?.value, false)
        assertEquals(newsViewModel?.networkErrorLiveData?.value, true)
    }

    @Test
    fun fetchNews_MockingState_NullData() {
        newsRepository?.mockingState = MockNewsRepository.MockingState.NullData
        newsViewModel?.fetchNews(NewsPeriod.DAY)

        assertNull(newsViewModel?.newsResultsLiveData?.value)
        assertEquals(newsViewModel?.isLoadingLiveData?.value, false)
        assertEquals(newsViewModel?.networkErrorLiveData?.value, false)
    }

    @Test
    fun fetchNews_MockingState_SuccessEmptyData() {
        newsRepository?.mockingState = MockNewsRepository.MockingState.SuccessEmptyData
        newsViewModel?.fetchNews(NewsPeriod.DAY)

        assertNotNull(newsViewModel?.newsResultsLiveData?.value)
        assertEquals(newsViewModel?.newsResultsLiveData?.value, listOf<MutableList<NewsResult>>())
        assertEquals(newsViewModel?.isLoadingLiveData?.value, false)
        assertEquals(newsViewModel?.networkErrorLiveData?.value, false)
    }
}
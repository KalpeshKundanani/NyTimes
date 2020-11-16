package com.kalpeshkundanani.nytimes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalpeshkundanani.nytimes.data.NewsRepository
import com.kalpeshkundanani.nytimes.data.enums.NewsPeriod
import com.kalpeshkundanani.nytimes.data.models.NewsResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*

/**
 * Created by Kalpesh Kundanani on 12/11/20.
 */
class NewsViewModel : ViewModel() {
    private var newsRepository: NewsRepository? = null
    private var newsResults: MutableList<NewsResult> = ArrayList()
    private var selectedNewsPeriod: NewsPeriod = NewsPeriod.DAY
    private var networkError = false

    val newsResultsLiveData: MutableLiveData<MutableList<NewsResult>> = MutableLiveData()
    val isLoadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val networkErrorLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val newsPeriodMutableLiveData: MutableLiveData<NewsPeriod> = MutableLiveData()

    fun init(newsRepository: NewsRepository) {
        if (this.newsRepository != null) return

        this.newsRepository = newsRepository
        newsPeriodMutableLiveData.postValue(selectedNewsPeriod)
    }

    fun fetchNews(newsPeriod: NewsPeriod) {
        if (selectedNewsPeriod == newsPeriod && newsResults.isNotEmpty() || networkError) {
            newsResultsLiveData.postValue(newsResults)
            return
        }
            isLoadingLiveData.postValue(true)
            try {
                val period = newsPeriod.getPeriod()
                val response = newsRepository?.fetchNews(period)
                val newsResult = response?.body()
                if (newsResult != null) {
                    newsResults.clear()
                    newsResults.addAll(newsResult.results ?: arrayListOf())
                    selectedNewsPeriod = newsPeriod
                    newsResultsLiveData.postValue(newsResults)
                }
                networkError = false
            } catch (e: IOException) {
                networkError = true
                newsPeriodMutableLiveData.postValue(selectedNewsPeriod)
                e.printStackTrace()
            }
            networkErrorLiveData.postValue(networkError)
            isLoadingLiveData.postValue(false)

    }

    fun updateNewsPeriod(period: NewsPeriod?) {
        if (selectedNewsPeriod == period) return
        networkError = false
        newsPeriodMutableLiveData.postValue(period)
    }

    fun onQueryTextChange(query: String) {
        val finalQuery = query.toLowerCase(Locale.getDefault())
        val searchedResults = ArrayList<NewsResult>()
        val mutableList = newsResults
        for (newsResult in mutableList) {
            val lowerCaseTitle = newsResult.title?.toLowerCase(Locale.getDefault())
            val titleContainsQuery = lowerCaseTitle?.contains(finalQuery) ?: false
            val lowerCaseByLine = newsResult.byline?.toLowerCase(Locale.getDefault())
            val byLineContainsQuery = lowerCaseByLine?.contains(finalQuery) ?: false
            if (titleContainsQuery || byLineContainsQuery) searchedResults.add(newsResult)
        }
        newsResultsLiveData.postValue(searchedResults)
    }
}
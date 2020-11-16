package com.kalpeshkundanani.nytimes.ui.activity

import android.app.AlertDialog
import android.app.SearchManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.kalpeshkundanani.nytimes.BuildConfig
import com.kalpeshkundanani.nytimes.R
import com.kalpeshkundanani.nytimes.data.NewsRepository
import com.kalpeshkundanani.nytimes.data.enums.NewsPeriod
import com.kalpeshkundanani.nytimes.data.models.NewsResult
import com.kalpeshkundanani.nytimes.data.network.NetworkServicesProvider
import com.kalpeshkundanani.nytimes.data.network.services.MostPopularNewsService
import com.kalpeshkundanani.nytimes.ui.adapter.NewsRecyclerViewAdapter
import com.kalpeshkundanani.nytimes.viewmodel.BaseViewModelFactory
import com.kalpeshkundanani.nytimes.viewmodel.NewsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [NewsItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class MainActivity : AppCompatActivity() {
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var hasMasterAndDetailOnSameScreen = false
    private lateinit var progressView: CardView
    private var newsRecyclerViewAdapter: NewsRecyclerViewAdapter? = null
    private val mBaseViewModelFactory: BaseViewModelFactory = BaseViewModelFactory()
    private val newsViewModel: NewsViewModel by lazy {
        ViewModelProvider(this, mBaseViewModelFactory)
                .get(NewsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // base view of the screen.
        initBaseScreenUI()

        // backend setup.
        initViewModel()

        // init screen content UI.
        initProgressBar()
        initNewsRecyclerView()

        // set data observers.
        setDataObservers()
    }

    private fun initBaseScreenUI() {
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View?>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        toolbar.title = title
        if (findViewById<View?>(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            hasMasterAndDetailOnSameScreen = true
        }
    }

    private fun initViewModel() {
        val apiKey: String = BuildConfig.NY_TIMES_API_KEY
        val baseUrl: String = BuildConfig.NY_TIMES_BASE_URL
        val provider = NetworkServicesProvider(baseUrl)
        val service: MostPopularNewsService = provider.getMostPopularNewsService()!!
        val newsRepository: NewsRepository = NewsRepository.getInstance(service, apiKey)!!
        newsViewModel.init(newsRepository)
    }

    private fun initProgressBar() {
        progressView = findViewById(R.id.loading_layout)
        showProgressBar(false)
    }

    private fun setDataObservers() {
        newsViewModel.newsResultsLiveData.observe(this, this::handleNewsResultsUpdate)
        newsViewModel.isLoadingLiveData.observe(this, this::showProgressBar)
        newsViewModel.newsPeriodMutableLiveData.observe(this, this::handleNewsPeriodUpdate)
        newsViewModel.networkErrorLiveData.observe(this, this::handleNetworkError)
    }

    private fun handleNewsResultsUpdate(newsResults: MutableList<NewsResult>) {
        newsRecyclerViewAdapter?.setData(newsResults)
    }

    private fun handleNetworkError(hasNetworkError: Boolean?) {
        if (hasNetworkError == true) {
            AlertDialog.Builder(this)
                    .setTitle(R.string.error_title_network_error)
                    .setMessage(R.string.error_message_not_able_to_load_feeds)
                    .setNegativeButton(R.string.button_label_cancel, null)
                    .show()
        }
    }

    private fun handleNewsPeriodUpdate(period: NewsPeriod?) {
        if (period == null) return
        invalidateOptionsMenu()

        GlobalScope.launch(Dispatchers.IO) {
            newsViewModel.fetchNews(period)
        }
    }

    private fun initNewsRecyclerView() {
        newsRecyclerViewAdapter = NewsRecyclerViewAdapter(this, hasMasterAndDetailOnSameScreen)
        val recyclerView = findViewById<RecyclerView?>(R.id.item_list)
        recyclerView.adapter = newsRecyclerViewAdapter
    }

    private fun showProgressBar(isLoading: Boolean?) {
        progressView.visibility = visibility(isLoading ?: false)
    }

    private fun visibility(isVisible: Boolean): Int {
        return if (isVisible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater: MenuInflater? = menuInflater
        menuInflater?.inflate(R.menu.main, menu)
        intiSearchView(menu)
        checkSelectedNewsPeriodOption(menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun checkSelectedNewsPeriodOption(menu: Menu?) {
        val newsPeriod = newsViewModel.newsPeriodMutableLiveData.value
        if (newsPeriod != null) menu?.getItem(newsPeriod.ordinal + 1)?.isChecked = true
    }

    private fun intiSearchView(menu: Menu?) {
        val searchItem = menu?.findItem(R.id.action_search)
        if (searchItem != null) {
            val searchView = searchItem.actionView as SearchView
            val searchManager = this.getSystemService(SEARCH_SERVICE) as SearchManager
            searchView.setSearchableInfo(searchManager.getSearchableInfo(this.componentName))
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    if (query == null) return false
                    newsViewModel.onQueryTextChange(query)
                    return true
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val period = when (item.itemId) {
            R.id.menu_one_day -> NewsPeriod.DAY
            R.id.menu_seven_days -> NewsPeriod.WEEK
            R.id.menu_thirty_days -> NewsPeriod.MONTH
            else -> null
        } ?: return false

        newsViewModel.updateNewsPeriod(period)
        return true
    }
}
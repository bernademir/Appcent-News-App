package com.bernademir.newsapp.ui.news

import android.app.SearchManager
import android.content.ClipData
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bernademir.newsapp.R
import com.bernademir.newsapp.R.*
import com.bernademir.newsapp.adapter.ArticleAdapter
import com.bernademir.newsapp.api.EverythingEndpoint
import com.bernademir.newsapp.model.Article
import com.bernademir.newsapp.model.Everything
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class NewsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {


    private val ENDPOINT_URL by lazy { "https://newsapi.org/v2/" }
    private lateinit var everythingEndpoint: EverythingEndpoint
    private lateinit var newsApiConfig: String
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var articleList: ArrayList<Article>
    private lateinit var userKeyWordInput: String
    private lateinit var everythingObservable: Observable<Everything>
    private lateinit var compositeDisposable: CompositeDisposable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       val view= inflater.inflate(layout.fragment_news, container, false)

        val retrofit: Retrofit = generateRetrofitBuilder()
        everythingEndpoint = retrofit.create(EverythingEndpoint::class.java)
        newsApiConfig = resources.getString(string.api_key)

        view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh).setOnRefreshListener(
            this
        )
        view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh).setColorSchemeResources(
            color.black
        )

        articleList = ArrayList()
        articleAdapter = ArticleAdapter(articleList)

        userKeyWordInput = ""

        compositeDisposable = CompositeDisposable()

        view.findViewById<RecyclerView>(R.id.recycler_view).setHasFixedSize(
            true
        )
        view.findViewById<RecyclerView>(R.id.recycler_view).layoutManager = LinearLayoutManager(
            context
        )
        view.findViewById<RecyclerView>(R.id.recycler_view).itemAnimator = DefaultItemAnimator()
        view.findViewById<RecyclerView>(R.id.recycler_view).adapter = articleAdapter

        setHasOptionsMenu(true)

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    override fun onRefresh() {
        checkUserKeywordInput()
    }

    private fun checkUserKeywordInput() {
        if (userKeyWordInput.isEmpty()) {
            queryTopHeadlines()
        } else {
            getKeyWordQuery(userKeyWordInput)
        }
    }

    private fun generateRetrofitBuilder(): Retrofit {

        return Retrofit.Builder()
            .baseUrl(ENDPOINT_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.bottom_nav_search, menu)
            setUpSearchMenuItem(menu)
    }


    private fun setUpSearchMenuItem(menu: Menu) {
        val searchManager: SearchManager = (this.activity?.getSystemService(Context.SEARCH_SERVICE)) as SearchManager
        val searchView: SearchView = ((menu.findItem(R.id.action_search)?.actionView)) as SearchView
        val searchMenuItem: MenuItem = menu.findItem(R.id.action_search)

        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.activity?.componentName))
        searchView.queryHint = "Type a text"
        searchView.setOnQueryTextListener(onQueryTextListenerCallback())
        searchMenuItem.icon.setVisible(false, false)
    }

    private fun onQueryTextListenerCallback(): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(userInput: String): Boolean {
                return checkQueryText(userInput)
            }
            override fun onQueryTextChange(userInput: String): Boolean {
                return checkQueryText(userInput)
            }
        }
    }

    private fun checkQueryText(userInput: String): Boolean {
        if (userInput.length > 1) {
            userKeyWordInput = userInput
            getKeyWordQuery(userInput)
        } else if (userInput == "") {
            userKeyWordInput = ""
            queryTopHeadlines()
        }
        return false
    }

    private fun getKeyWordQuery(userKeywordInput: String) {
        requireView().findViewById<SwipeRefreshLayout>(R.id.swipe_refresh).isRefreshing = true
        if (userKeywordInput.isNotEmpty()) {
            everythingObservable = everythingEndpoint.getUserSearchInput(
                newsApiConfig,
                userKeywordInput,
                1
            )
            subscribeObservableOfArticle()
        } else {
            queryTopHeadlines()
        }
    }

    private fun queryTopHeadlines() {
        requireView().findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)!!.isRefreshing = true
        everythingObservable = everythingEndpoint.getTopHeadlines("tr", newsApiConfig)
        subscribeObservableOfArticle()
    }

    private fun subscribeObservableOfArticle() {
        articleList.clear()
        compositeDisposable.add(
            everythingObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap {
                    Observable.fromIterable(it.articles)
                }
                .subscribeWith(createArticleObserver())
        )
    }

    private fun createArticleObserver(): DisposableObserver<Article> {
        return object : DisposableObserver<Article>() {
            override fun onNext(article: Article) {
                if (!articleList.contains(article)) {
                    articleList.add(article)
                }
            }
            override fun onComplete() {
                showArticlesOnRecyclerView()
            }
            override fun onError(e: Throwable) {
                Log.e("createArticleObserver", "Article error: ${e.message}")
            }
        }
    }

    private fun showArticlesOnRecyclerView() {
        if (articleList.size > 0) {
            view?.findViewById<TextView>(R.id.empty_text)!!.visibility = View.GONE
            view?.findViewById<RecyclerView>(R.id.recycler_view)!!.visibility = View.GONE
            view?.findViewById<RecyclerView>(R.id.recycler_view)!!.visibility = View.VISIBLE
            articleAdapter.setArticles(articleList)
        } else {
            view?.findViewById<RecyclerView>(R.id.recycler_view)!!.visibility = View.GONE
            view?.findViewById<TextView>(R.id.empty_text)!!.visibility = View.VISIBLE
            view?.findViewById<RecyclerView>(R.id.recycler_view)!!.visibility = View.VISIBLE
            view?.findViewById<RecyclerView>(R.id.recycler_view)!!.setOnClickListener { checkUserKeywordInput() }
        }
        view?.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)!!.isRefreshing = false
    }
}
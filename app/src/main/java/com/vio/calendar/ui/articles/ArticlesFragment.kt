package com.vio.calendar.ui.articles

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import com.vio.calendar.R
import com.vio.calendar.app.CalendarApplication
import com.vio.calendar.data.article.net.FirebaseClient
import com.vio.calendar.ui.main.MainActivity
import com.vio.calendar.viewmodel.article.ArticleViewModel
import kotlinx.android.synthetic.main.fragment_articles.*
import kotlinx.android.synthetic.main.fragment_articles.view.*

class ArticlesFragment: Fragment()  {

    private lateinit var adapter: ArticleAdapter
    private lateinit var articleViewModel: ArticleViewModel

    val ADMOB_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"
    var currentNativeAd: UnifiedNativeAd? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_articles, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MainActivity", (activity as MainActivity).prefs.getString("token", "token"))
//        articleViewModel = ViewModelProviders.of(this).get(ArticleViewModel::class.java)
        view.parentShimmerLayout.visibility = View.VISIBLE

        adapter = ArticleAdapter(mutableListOf(), this, (activity as MainActivity).applicationContext, CalendarApplication.prefs!!, view)
        articlesRecyclerView.adapter = adapter
        //ViewCompat.setNestedScrollingEnabled(articlesRecyclerView, false);
        //articlesRecyclerView.isNestedScrollingEnabled = false

       // ViewCompat.setNestedScrollingEnabled(articlesRecyclerView, false);
//        code = (activity as MainActivity).code
        getArticles()
    }

//    private fun showLoading() {
//        parentShimmerLayout.visibility = View.VISIBLE
//    }

//    private fun hideLoading() {
//        parentShimmerLayout.visibility = View.GONE
//    }

    private fun getArticles() {
//        showLoading()
//        articleViewModel.getArticles(code).observe(this, Observer { articles ->
//
//            if (articles == null) {
//            } else {
//                articles.sortedWith(compareBy {it.creationDate})
//                adapter.setArticles(articles.reversed())
//                parentShimmerLayout.visibility = View.GONE
//            }
//        })

        FirebaseClient().getArticlesByLang(CalendarApplication.code).observe(this, Observer { articles ->

            if (articles == null) {
            } else {
                articles.sortedWith(compareBy {it.created_at})
                adapter.setArticles(articles)

            }
        })



    }

//    private fun refreshAd() {
//
//        val builder = AdLoader.Builder((activity as MainActivity), ADMOB_AD_UNIT_ID)
//
//        builder.forUnifiedNativeAd { unifiedNativeAd ->
//            // OnUnifiedNativeAdLoadedListener implementation.
//            val adView = layoutInflater
//                .inflate(R.layout.ad_unified, null) as UnifiedNativeAdView
//
//        }
//
//        val videoOptions = VideoOptions.Builder()
//            .build()
//
//        val adOptions = NativeAdOptions.Builder()
//            .setVideoOptions(videoOptions)
//            .build()
//
//        builder.withNativeAdOptions(adOptions)
//
//        val adLoader = builder.withAdListener(object : AdListener() {
//            override fun onAdFailedToLoad(errorCode: Int) {
//                Log.d("ArticlesFragment", "native ad failed to load with error code: $errorCode")
//            }
//        }).build()
//
//        adLoader.loadAd(AdRequest.Builder().build())
//    }

}

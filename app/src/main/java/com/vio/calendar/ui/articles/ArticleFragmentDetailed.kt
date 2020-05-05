package com.vio.calendar.ui.articles

import android.graphics.Color
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.vio.calendar.R
import com.vio.calendar.data.article.model.Article
import java.lang.Exception

class ArticleFragmentDetailed(var article: Article): Fragment()  {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_articles_detailed, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.articel_title).text = Html.fromHtml(article.title)

        Glide.with(this)
            .load(article.picture_url)
            .transform(MultiTransformation(CenterCrop(), RoundedCorners(32)))
            .into(view.findViewById(R.id.article_image))

        val llForWebView = view.findViewById<LinearLayout>(R.id.ll_for_web_view)
//        val t = TextView(context)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                t.text = Html.fromHtml(article.content, Html.FROM_HTML_MODE_COMPACT)
//                t.textSize = 16f
//            } else {
//                t.text = Html.fromHtml(article.content)
//                t.textSize = 16f
//            }
//
//        llForWebView.addView(t)
//
        try {
            val wv = WebView(context)
            wv.setBackgroundColor(Color.TRANSPARENT)

            wv.settings.loadWithOverviewMode = true
            wv.settings.useWideViewPort = false


            wv.settings.javaScriptEnabled = true

            wv.loadDataWithBaseURL(null, "<style> img { display: block; max-width: 100%; height: auto; } </style>${article.content}", "text/html", "utf-8", null)
            llForWebView.addView(wv)
        } catch (e: Exception){}



        view.findViewById<ImageView>(R.id.back_btn).setOnClickListener {
            val fragment = ArticlesFragment()
            val transaction =
                (context as FragmentActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_frame, fragment).commit()
        }


    }


}
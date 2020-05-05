package com.vio.calendar.ui.articles

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.ads.formats.MediaView
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import com.vio.calendar.R
import com.vio.calendar.app.CalendarApplication
import com.vio.calendar.data.article.ArticleRepositoryImpl
import com.vio.calendar.data.article.model.Article
import com.vio.calendar.data.article.model.LikesResponseItem
import com.vio.calendar.data.article.net.FirebaseClient
import com.vio.calendar.inflate
import com.vio.calendar.ui.main.MainActivity
import com.vio.calendar.view.fragments.PrefsFragment
import kotlinx.android.synthetic.main.fragment_articles.view.*
import kotlinx.android.synthetic.main.item_article.view.articleImage
import kotlinx.android.synthetic.main.item_article_latest_version.view.*
import kotlinx.android.synthetic.main.item_article_new.view.*
import kotlinx.android.synthetic.main.item_article_new.view.like_counter
import kotlinx.android.synthetic.main.item_article_new.view.like_image
import kotlinx.android.synthetic.main.item_article_new.view.title

class ArticleAdapter(
    private val articles: MutableList<Article>,
    private val lifecycleOwner: LifecycleOwner,
    private val context: Context,
    private val prefs: SharedPreferences,
    private val view: View
) :
    RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {


//    val repository = ArticleRepositoryImpl()

//    val transformation = MultiTransformation<Bitmap>(
//                CenterCrop(), RoundedCorners(32)
//    )

    val token = prefs.getString("token", "s")!!
    val name = prefs.getString("user_name", "")!!
    val id = prefs.getString("userid", "")!!
    val color = prefs.getString("color", "")!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_article_latest_version))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(articles[position])
            view.parentShimmerLayout.visibility = View.GONE
    }

    override fun getItemCount() = articles.size

    fun setArticles(articles: List<Article>) {
        this.articles.clear()
        this.articles.addAll(articles)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var article: Article

//        var likesCount: Int = 0
//        var isLiked: Boolean = false

        fun bind(article: Article) {
            this.article = article
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                itemView.content.text = Html.fromHtml(article.content, Html.FROM_HTML_MODE_COMPACT)
//            } else {
//                itemView.content.text = Html.fromHtml(article.content)
//            }
//            itemView.content.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            itemView.title.text = article.title


            itemView.article_btn_open.setOnClickListener {
                if (article.is_vip){
                    if (MainActivity.mRewardedVideoAd!!.isLoaded){
                        MainActivity.mRewardedVideoAd!!.show()
                        val fragment = ArticleFragmentDetailed(article)
                        val transaction =
                            (itemView.context as FragmentActivity).supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.main_frame, fragment).commit()
                    } else {
                    val fragment = ArticleFragmentDetailed(article)
                    val transaction =
                        (itemView.context as FragmentActivity).supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.main_frame, fragment).commit()
                }
                } else {
                    val fragment = ArticleFragmentDetailed(article)
                    val transaction =
                        (itemView.context as FragmentActivity).supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.main_frame, fragment).commit()
                }
        }

//            itemView.content.setOnClickListener {
////                if (itemView.content.isExpanded) {
////                    itemView.content.collapse()
////                } else {
////                    itemView.content.expand()
////                }
////            }



//            repository.getLikes(article).observe(lifecycleOwner, Observer { likesList ->
//                if (likesList!!.contains(LikesResponseItem(id))) {
//                    isLiked = true
//                    itemView.like_image.setImageResource(R.drawable.like_filled)
//                } else {
//                    isLiked = false
//                    itemView.like_image.setImageResource(R.drawable.like)
//                }
//
//            })

            if (prefs.contains(article._id!!)) {
                val likeTrimmedCounterStr: String =
                    if (article.likes.toInt() > 1000) (article.likes.toInt() / 1000).toString() + "K" else article.likes

                itemView.like_image.setImageResource(R.drawable.like_filled)
                itemView.like_counter.text = likeTrimmedCounterStr
            } else {

                val likeTrimmedCounterStr: String =
                    if (article.likes.toInt() > 1000) (article.likes.toInt() / 1000).toString() + "K" else article.likes
                itemView.like_image.setImageResource(R.drawable.like)
                itemView.like_counter.text = likeTrimmedCounterStr
            }

            itemView.like_image.setOnClickListener {

                changeLikeState(article, itemView)
            }


            /*
            itemView.sendCommentButton.setOnClickListener {

                commentsCount++
                val comment = CommentSend(itemView.commentInputField.text.toString())
                repository.sendComment(article.id!!, token, comment)

                itemView.articleCommentCount.text = commentsCount.toString()
                adapter.addComment(
                    Comment(
                        "",
                        UserData(name, color),
                        itemView.commentInputField.text.toString(),
                        Date()
                    )
                )

                itemView.commentInputField.text.clear()

            }*/

//            repository.getLikesCount(article)
//                .observe(lifecycleOwner, Observer { likeResponseCount ->
//                    Log.d("getLikesCount", "article.id ${article.id}")
//                    likesCount = likeResponseCount?.likesCount!!
//                    itemView.like_counter.text = article.likes + ""
//                })


            /*repository.getComments(article).observe(lifecycleOwner, Observer {
                    comments ->
                if (comments == null) {
                } else {
                    Log.d("ArticleAdapter", "article: ${article.id}, comments: $comments")
                    commentsCount = comments.size
                    itemView.articleCommentCount.text = comments.size.toString()
                    comments.sortedWith(compareBy {it.createdAt})
                    Log.d("ArticleAdapter", "size: ${comments.size}")
                    adapter.setComments(comments.asReversed())
                }
            })*/

            Glide.with(itemView.context)
                .load(article.picture_url)
                .transform(MultiTransformation(CenterCrop(), RoundedCorners(32)))
                .into(itemView.articleImage)


            if (article.is_vip)
            {itemView.vip_ll.visibility = View.VISIBLE
                itemView.article_btn_open.background = itemView.context.resources.getDrawable(R.drawable.rounded_btn)
                itemView.article_btn_open.setTextColor(itemView.context.resources.getColor(R.color.color_1_white))
            }
            else
            {itemView.vip_ll.visibility = View.GONE
                itemView.article_btn_open.background = itemView.context.resources.getDrawable(R.drawable.rounded_btn_empty)
                itemView.article_btn_open.setTextColor(itemView.context.resources.getColor(R.color.main_color))
            }

        }
    }

    fun changeLikeState(article: Article, itemView : View){
        if (!prefs.contains(article._id!!)) {

            FirebaseClient().changeArticleLikeValue(CalendarApplication.code, article._id!!, (article.likes.toInt()+1).toString())
            prefs.edit().putString(article._id!!, article._id!!).apply()


            val likeTrimmedCounterStr: String =
                if (article.likes.toInt() > 1000) (article.likes.toInt() / 1000).toString() + "K" else article.likes

            itemView.like_image.setImageResource(R.drawable.like_filled)
            itemView.like_counter.text = likeTrimmedCounterStr
        } else {

            FirebaseClient().changeArticleLikeValue(CalendarApplication.code, article._id!!, (article.likes.toInt()-1).toString())
            prefs.edit().remove(article._id!!).apply()

            val likeTrimmedCounterStr: String =
                if (article.likes.toInt() > 1000) (article.likes.toInt() / 1000).toString() + "K" else article.likes
            itemView.like_image.setImageResource(R.drawable.like)
            itemView.like_counter.text = likeTrimmedCounterStr
        }
    }

//    private fun populateUnifiedNativeAdView(nativeAd: UnifiedNativeAd, adView: UnifiedNativeAdView) {
//        // You must call destroy on old ads when you are done with them,
//        // otherwise you will have a memory leak.
//
//        // Set the media view. Media content will be automatically populated in the media view once
//        // adView.setNativeAd() is called.
//        adView.mediaView = adView.findViewById<MediaView>(R.id.ad_media)
//
//        // Set other ad assets.
//        adView.headlineView = adView.findViewById(R.id.ad_headline)
//        adView.bodyView = adView.findViewById(R.id.ad_body)
//        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
//        adView.iconView = adView.findViewById(R.id.ad_app_icon)
//        adView.priceView = adView.findViewById(R.id.ad_price)
//        adView.starRatingView = adView.findViewById(R.id.ad_stars)
//        adView.storeView = adView.findViewById(R.id.ad_store)
//        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)
//
//        // The headline is guaranteed to be in every UnifiedNativeAd.
//        (adView.headlineView as TextView).text = nativeAd.headline
//
//        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
//        // check before trying to display them.
//        if (nativeAd.body == null) {
//            adView.bodyView.visibility = View.INVISIBLE
//        } else {
//            adView.bodyView.visibility = View.VISIBLE
//            (adView.bodyView as TextView).text = nativeAd.body
//        }
//
//        if (nativeAd.callToAction == null) {
//            adView.callToActionView.visibility = View.INVISIBLE
//        } else {
//            adView.callToActionView.visibility = View.VISIBLE
//            (adView.callToActionView as Button).text = nativeAd.callToAction
//        }
//
//        if (nativeAd.icon == null) {
//            adView.iconView.visibility = View.GONE
//        } else {
//            (adView.iconView as ImageView).setImageDrawable(
//                nativeAd.icon.drawable)
//            adView.iconView.visibility = View.VISIBLE
//        }
//
//        if (nativeAd.price == null) {
//            adView.priceView.visibility = View.INVISIBLE
//        } else {
//            adView.priceView.visibility = View.VISIBLE
//            (adView.priceView as TextView).text = nativeAd.price
//        }
//
//        if (nativeAd.store == null) {
//            adView.storeView.visibility = View.INVISIBLE
//        } else {
//            adView.storeView.visibility = View.VISIBLE
//            (adView.storeView as TextView).text = nativeAd.store
//        }
//
//        if (nativeAd.starRating == null) {
//            adView.starRatingView.visibility = View.INVISIBLE
//        } else {
//            (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
//            adView.starRatingView.visibility = View.VISIBLE
//        }
//
//        if (nativeAd.advertiser == null) {
//            adView.advertiserView.visibility = View.INVISIBLE
//        } else {
//            (adView.advertiserView as TextView).text = nativeAd.advertiser
//            adView.advertiserView.visibility = View.VISIBLE
//        }
//
//        // This method tells the Google Mobile Ads SDK that you have finished populating your
//        // native ad view with this native ad. The SDK will populate the adView's MediaView
//        // with the media content from this native ad.
//        adView.setNativeAd(nativeAd)
//
//        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
//        // have a video asset.
//        val vc = nativeAd.videoController
//
//    }


}

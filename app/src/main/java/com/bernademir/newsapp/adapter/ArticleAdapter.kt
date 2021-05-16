package com.bernademir.newsapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

import com.bernademir.newsapp.R
import com.bernademir.newsapp.model.Article
import com.bernademir.newsapp.view.NewsDetailActivity
import com.squareup.picasso.Picasso

class ArticleAdapter(
    private var articleList: ArrayList<Article>
) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    private val placeHolderImage = "https://pbs.twimg.com/profile_images/467502291415617536/SP8_ylk9.png"

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ArticleViewHolder {
        val itemView: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_article, viewGroup, false)
        return ArticleViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    override fun onBindViewHolder(articleViewHolder: ArticleViewHolder, itemIndex: Int) {
        val article: Article = articleList[itemIndex]
        setPropertiesForArticleViewHolder(articleViewHolder, article)
        articleViewHolder.cardView.setOnClickListener {
            val intent = Intent(it.context, NewsDetailActivity::class.java)
            intent?.putExtra("data", article)
            it.context.startActivity(intent)
        }
    }

    private fun setPropertiesForArticleViewHolder(
        articleViewHolder: ArticleViewHolder,
        article: Article
    ) {
        checkForUrlToImage(article, articleViewHolder)
        articleViewHolder.title.text = article.title
        articleViewHolder.description.text = article.description
    }

    private fun checkForUrlToImage(
        article: Article?,
        articleViewHolder: ArticleViewHolder
    ) {
        if (article?.urlToImage.isNullOrEmpty()) {
            Picasso.get()
                .load(placeHolderImage)
                .centerCrop()
                .fit()
                .into(articleViewHolder.urlToImage)
        } else {
            Picasso.get()
                .load(article?.urlToImage)
                .centerCrop()
                .fit()
                .into(articleViewHolder.urlToImage)
        }
    }

    fun setArticles(articles: ArrayList<Article>) {
        articleList = articles
        notifyDataSetChanged()
    }

    inner class ArticleViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        val cardView: CardView by lazy { view.findViewById<CardView>(R.id.card_view) }
        val urlToImage: ImageView by lazy { view.findViewById<ImageView>(R.id.article_urlToImage) }
        val title: TextView by lazy { view.findViewById<TextView>(R.id.article_title) }
        val description: TextView by lazy { view.findViewById<TextView>(R.id.article_description) }
    }
}
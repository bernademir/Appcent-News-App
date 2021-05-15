package com.bernademir.newsapp.ui

import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bernademir.newsapp.R
import com.bernademir.newsapp.model.Article
import com.squareup.picasso.Picasso

class NewsDetailActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        val data = intent.extras
        val news = data?.getParcelable<Article>("data")

        findViewById<TextView>(R.id.title_text).text = news?.title.toString()

        findViewById<TextView>(R.id.author_name).text = news?.author.toString()

        findViewById<TextView>(R.id.date_text).text = news?.publishedAt.toString()

        findViewById<TextView>(R.id.content_text).text = news?.description.toString() + news?.content.toString()
        findViewById<TextView>(R.id.content_text).movementMethod = ScrollingMovementMethod()

        Picasso.get()
            .load(news?.urlToImage.toString())
            .centerCrop()
            .fit()
            .into(findViewById<ImageView>(R.id.detail_image))


        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        findViewById<Button>(R.id.source_button).setOnClickListener {
            val intent = Intent(it.context, WebViewActivity::class.java)
            intent?.putExtra("webUrl", news?.url)
            it.context.startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.news_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_favorite -> {
            onClickFav()
            true
        }
        R.id.action_share -> {
//            val iconId = resources.getIdentifier("action_share", "id", "android")
//            findViewById<SearchView>(iconId).setOnClickListener {
//                val s = (R.id.title_text).toString()
//                val shareIntent = Intent()
//                shareIntent.action = Intent.ACTION_SEND
//                shareIntent.type="text/plain"
//                shareIntent.putExtra(Intent.EXTRA_TEXT, s);
//                startActivity(Intent.createChooser(shareIntent,"Share via"))
//            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun onClickFav(){
        val intent = Intent(this@NewsDetailActivity,FavoritesFragment::class.java)
        intent.putExtra("title",R.id.title_text)
        intent.putExtra("description",R.id.content_text)
        intent.putExtra("image",R.id.detail_image)
        startActivity(intent)
    }
}
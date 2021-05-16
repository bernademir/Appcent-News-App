package com.bernademir.newsapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bernademir.newsapp.R
import com.bernademir.newsapp.adapter.ArticleAdapter
import com.bernademir.newsapp.model.Article
import com.squareup.picasso.Picasso

class FavoritesFragment : Fragment() {
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var articleList: ArrayList<Article>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Favorites"

        val favTitle = requireArguments().getString("newsTitle")
        view.findViewById<TextView>(R.id.title_text).text = favTitle

        val favContent = requireArguments().getString("newsContent")
        view.findViewById<TextView>(R.id.content_text).text = favContent

        val favImage = requireArguments().getString("newsTitle")
        Picasso.get()
            .load(favImage.toString())
            .centerCrop()
            .fit()
            .into(view.findViewById<ImageView>(R.id.detail_image))

        if (articleList.size > 0) {
            view?.findViewById<RecyclerView>(R.id.recycler_view_fav)!!.visibility = View.GONE
            view?.findViewById<RecyclerView>(R.id.recycler_view_fav)!!.visibility = View.VISIBLE
            articleAdapter.setArticles(articleList)
        } else {
            view?.findViewById<RecyclerView>(R.id.recycler_view_fav)!!.visibility = View.GONE
            view?.findViewById<RecyclerView>(R.id.recycler_view_fav)!!.visibility = View.VISIBLE
        }
        return view
    }
}
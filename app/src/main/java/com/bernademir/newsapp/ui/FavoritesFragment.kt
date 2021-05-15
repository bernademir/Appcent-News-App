package com.bernademir.newsapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bernademir.newsapp.R
import com.squareup.picasso.Picasso

class FavoritesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_favorites, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Favorites"

        val data = (activity as AppCompatActivity).intent.extras
        val title = data?.getString("title")
        val description = data?.getString("description")
        val image = data?.getString("image")

        view.findViewById<TextView>(R.id.article_title).text = title
        view.findViewById<TextView>(R.id.article_description).text = description
        Picasso.get()
            .load(image)
            .centerCrop()
            .fit()
            .into(view.findViewById<ImageView>(R.id.article_urlToImage))

        return view
    }
}
package com.dev.eraydel.news.viewholders

import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.dev.eraydel.news.R
import com.dev.eraydel.news.listeners.OnItemClickListener
import com.dev.eraydel.news.models.Article
import com.squareup.picasso.Picasso

class ArticlesViewHolder (val view: View, val listener: OnItemClickListener): RecyclerView.ViewHolder(view){

    fun render(noticia:Article){
        view.findViewById<TextView>(R.id.tvTitulo).text = noticia.title
        view.findViewById<TextView>(R.id.tvFecha).text = noticia.section

        view.findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.cnn)
        itemView.setOnClickListener{
            listener.onItemClick(noticia)
        }

    }
}
package com.example.megogo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.megogo.R
import com.example.megogo.CinemaDatabase
import com.squareup.picasso.Picasso

class MovieAdapterAdm(
    private val movies: MutableList<Movie>,
    private val onDeleteClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapterAdm.MovieViewHolder>() {

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMovieTitle: TextView = view.findViewById(R.id.tvMovieTitle)
        val tvMovieGenre: TextView = view.findViewById(R.id.tvMovieGenre)
        val tvMovieYear: TextView = view.findViewById(R.id.tvMovieYear)
        val ivPoster: ImageView = view.findViewById(R.id.ivPoster)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie_adm, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.tvMovieTitle.text = movie.title
        holder.tvMovieGenre.text = movie.genre
        holder.tvMovieYear.text = movie.year.toString()

        // Загружаем изображение постера с помощью Picasso
        Picasso.get().load(movie.poster).into(holder.ivPoster)

        holder.btnDelete.setOnClickListener { onDeleteClick(movie) }
    }

    override fun getItemCount(): Int = movies.size

    fun removeMovie(movie: Movie) {
        val position = movies.indexOf(movie)
        if (position != -1) {
            movies.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}

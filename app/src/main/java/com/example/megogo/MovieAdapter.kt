package com.example.megogo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.megogo.R
import com.squareup.picasso.Picasso

data class MovieItem(val id: Long, val title: String, val genre: String, val posterUrl: String, val year: String, val plot: String)

class MovieAdapter(private var movieList: List<MovieItem>) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.movie_title)
        val genre: TextView = view.findViewById(R.id.movie_genre)
        val poster: ImageView = view.findViewById(R.id.movie_poster)
        val yearText: TextView = view.findViewById(R.id.movie_year)
        val descText: TextView = view.findViewById(R.id.movie_desc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList[position]
        holder.title.text = movie.title
        holder.genre.text = movie.genre
        holder.yearText.text = movie.year
        holder.descText.text = movie.plot
        Picasso.get().load(movie.posterUrl).into(holder.poster)
    }

    override fun getItemCount(): Int = movieList.size

    fun updateMovies(newMovies: List<MovieItem>) {
        movieList = newMovies
        notifyDataSetChanged()
    }
}

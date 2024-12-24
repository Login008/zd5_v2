package com.example.megogo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.megogo.databinding.ActivityMovieListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieListBinding
    private lateinit var adapter: MovieAdapterAdm
    private val movies = mutableListOf<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MovieAdapterAdm(movies) { movie ->
            Toast.makeText(this@MovieListActivity, "The movie was removed from rental", Toast.LENGTH_SHORT).show()
            deleteMovieFromDatabase(movie)
        }

        binding.recyclerViewMovies.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewMovies.adapter = adapter

        loadMoviesFromDatabase()
    }

    private fun loadMoviesFromDatabase() {
        lifecycleScope.launch {
            val database = DatabaseClient.getInstance(applicationContext)
            val movieList = withContext(Dispatchers.IO) {
                database.movieDao().getAllMovies()
            }
            movies.clear()
            movies.addAll(movieList)
            adapter.notifyDataSetChanged()
        }
    }

    private fun deleteMovieFromDatabase(movie: Movie) {
        lifecycleScope.launch {
            val database = DatabaseClient.getInstance(applicationContext)
            withContext(Dispatchers.IO) {
                database.movieDao().deleteMovie(movie)
            }
            adapter.removeMovie(movie)
        }
    }

    fun goToAddition(view: View) {
        startActivity(Intent(this@MovieListActivity, MainForAdmin::class.java))
    }
}

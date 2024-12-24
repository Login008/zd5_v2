package com.example.megogo

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.megogo.databinding.ActivityMainForUserBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainForUser : AppCompatActivity() {
    private lateinit var binding: ActivityMainForUserBinding
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var genres: List<String>
    private var allMovies: List<MovieItem> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainForUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Настройка RecyclerView
        movieAdapter = MovieAdapter(listOf())
        binding.recyclerViewMovies.apply {
            layoutManager = LinearLayoutManager(this@MainForUser)
            adapter = movieAdapter
        }

        // Получение фильмов из базы данных
        loadMovies()

        // Настройка Spinner для фильтрации
        genres = listOf("All genres", "Action", "Horror", "Drama", "Comedy", "Thriller", "Mystery", "Romance", "Crime", "Sci-Fi", "Animation", "Adventure") // Пример жанров
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genres)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGenres.adapter = spinnerAdapter

        binding.spinnerGenres.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                filterMoviesByGenre(genres[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun loadMovies() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = DatabaseClient.getInstance(applicationContext)
            val moviesFromDb = db.movieDao().getAllMovies().map {
                MovieItem(it.id, it.title, it.genre, it.poster, it.year, it.description)
            }
            withContext(Dispatchers.Main) {
                allMovies = moviesFromDb
                movieAdapter.updateMovies(allMovies)
            }
        }
    }

    private fun filterMoviesByGenre(genre: String) {
        val filteredMovies = if (genre == "All genres") {
            allMovies
        } else {
            allMovies.filter { it.genre.contains(genre) }
        }
        movieAdapter.updateMovies(filteredMovies)
    }
}


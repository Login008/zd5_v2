package com.example.megogo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainForAdmin : AppCompatActivity() {

    private lateinit var searchTitle: EditText
    private lateinit var searchYear: EditText
    private lateinit var searchButton: Button
    private lateinit var db: CinemaDatabase
    private lateinit var posterIm: ImageView
    private lateinit var titleText: TextView
    private lateinit var yearText: TextView
    private lateinit var genreText: TextView
    private lateinit var plotText: TextView
    private lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_for_admin)

        db = DatabaseClient.getInstance(applicationContext)
        searchTitle = findViewById(R.id.etSearchTitle)
        searchYear = findViewById(R.id.etSearchYear)
        searchButton = findViewById(R.id.btnSearch)
        posterIm = findViewById(R.id.tvPoster)
        titleText = findViewById(R.id.tvTitle)
        yearText = findViewById(R.id.tvYear)
        genreText = findViewById(R.id.tvGenre)
        plotText = findViewById(R.id.tvPlot)


        searchButton.setOnClickListener {
                if (searchTitle.text.isNotEmpty()) {
                    val title = searchTitle.text.toString()
                    val year = searchYear.text.toString()
                    searchMovies(title, year)
                } else
                    Toast.makeText(
                        this@MainForAdmin,
                        "Fill at least a name of a movie",
                        Toast.LENGTH_SHORT)
                        .show()
        }
    }

    private fun searchMovies(title: String, year: String) {
        val url = "https://www.omdbapi.com/?apikey=8424b5c9&t=$title&y=$year"

        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            {
                    responce ->
                val obj = JSONObject(responce)

                try {
                    movie = Movie(
                        id = 0,
                        title = obj.getString("Title"),
                        year = obj.getString("Year"),
                        poster = obj.getString("Poster"),
                        description = obj.getString("Plot"),
                        genre = obj.getString("Genre")
                    )
                    loadMovie(movie)
                }
                catch (e:Exception)
                {
                    Toast.makeText(
                        this@MainForAdmin,
                        "The movie wasn't found",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            },
            {
                Toast.makeText(this, "There's some error, we're soon gonna fix it", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(stringRequest)
    }

    private fun loadMovie(movie: Movie) {
        titleText.text = movie.title
        yearText.text = movie.year
        plotText.text = movie.description
        genreText.text = movie.genre
        Picasso.get()
            .load(movie.poster)
            .placeholder(R.drawable.logo)
            .into(posterIm)
    }

    private fun addMovieToDatabase(movie: Movie) {
        var exists = false
        GlobalScope.launch {
            val movie1 = db.movieDao().getMovieByTitle(movie.title)
            exists = movie1 != null
            if (!exists) {
                db.movieDao().insertMovie(movie)
                runOnUiThread {
                    Toast.makeText(this@MainForAdmin, "The movie is added", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            else
            {
                runOnUiThread{
                    Toast.makeText(this@MainForAdmin, "The movie is already in the rental", Toast.LENGTH_SHORT)
                        .show() }
            }
        }
    }

    fun AddMovieBD(view: View) {
        try {
            addMovieToDatabase(movie)
        }
        catch (e:Exception)
        {
            Toast.makeText(this@MainForAdmin, "A movie for adding wasn't still found", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun goToRemoving(view: View) {
        startActivity(Intent(this@MainForAdmin, MovieListActivity::class.java))
    }
}
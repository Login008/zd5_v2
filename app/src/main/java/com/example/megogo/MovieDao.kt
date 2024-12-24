package com.example.megogo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovieDao {
    @Insert
    fun insertMovie(movie: Movie)

    @Delete
    fun deleteMovie(movie: Movie)

    @Query("SELECT * FROM movies")
    fun getAllMovies(): List<Movie>

    @Query("SELECT * FROM movies WHERE title LIKE :query")
    fun searchMovies(query: String): List<Movie>

    @Query("SELECT * FROM movies WHERE title = :title LIMIT 1")
    fun getMovieByTitle(title: String): Movie?
}

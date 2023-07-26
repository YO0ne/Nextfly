package com.example.nextfli

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    lateinit var recyclerViewMovies: RecyclerView
    lateinit var moviesAdapter: MoviesAdapter
    lateinit var toolbar: Toolbar
    lateinit var imageSearch: ImageView
    lateinit var editSearch: EditText

    lateinit var editMovieSearch: EditText
    lateinit var buttonSearch: Button
    lateinit var movieTitle: TextView
    lateinit var movieSummary: TextView
    lateinit var moviePoster: ImageView
    lateinit var movieReleaseDate: TextView

    lateinit var movieLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerViewMovies = findViewById(R.id.recyclerViewMovies)
        toolbar = findViewById(R.id.toolbar)
        imageSearch = toolbar.findViewById(R.id.imageSearch)
        editSearch = toolbar.findViewById(R.id.editSearch)
        setSupportActionBar(toolbar)

        editMovieSearch = findViewById(R.id.editMovieSearch)
        buttonSearch = findViewById(R.id.buttonSearch)
        movieTitle = findViewById(R.id.movieTitle)
        movieSummary = findViewById(R.id.movieSummary)
        moviePoster = findViewById(R.id.moviePoster)
        movieReleaseDate = findViewById(R.id.movieReleaseText)

        movieLayout = findViewById(R.id.movieLayout)

        //TODO: create retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val movieService = retrofit.create(MovieService::class.java)

        //TODO: call moviedb api
        val result = movieService.getMovie()

        result.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    val results = result?.get("results")?.asJsonArray
                    val firstMovie = results?.get(0)?.asJsonObject
                    val originalTitle = firstMovie?.get("original_title")?.asString
                    val overview = firstMovie?.get("overview")?.asString
                    val release = firstMovie?.get("release_date")?.asString

                    val poster = firstMovie?.get("poster_path")?.asString
                    Picasso.get().load("https://image.tmdb.org/t/p/original/$poster").into(moviePoster)


                    movieTitle.text = "$originalTitle"
                    movieSummary.text = "$overview"
                    movieReleaseDate.text = "$release"

                    movieLayout.visibility = View.VISIBLE

                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(applicationContext, "Erreur serveur", Toast.LENGTH_SHORT).show()
            }

        })



        val items = listOf(
            Movies("Oppenheimer","Pendant la Seconde Guerre mondiale, le lieutenant-général Leslie Groves Jr. nomme le physicien J. Robert Oppenheimer pour travailler sur le projet ultra-secret Manhattan. Oppenheimer et une équipe de scientifiques passent des années à développer et à concevoir la bombe atomique.","19/07/2023", R.drawable.oppenheimer, 3.7F),
            Movies("Transformers: Rise of the Beasts","Les Decepticons viennent d'arriver sur Terre en 1994, tout comme Optimus Prime, qui existe depuis un peu plus longtemps. L'archéologue Elena et le soldat Noah au Pérou découvrent les traces d'un ancien conflit de transformateurs sur Terre. À l'époque, les Maximals, les Predacons et les Terrorcons s'affrontaient, et ils revinrent à la vie un peu plus tard.","07/06/2023", R.drawable.transformers, 3F),
            Movies("Avatar 2","Jake Sully et Neytiri ont formé une famille et font tout pour rester aussi soudés que possible. Ils sont cependant contraints de quitter leur foyer et d'explorer les différentes régions encore mystérieuses de Pandora. Lorsqu'une ancienne menace refait surface, Jake va devoir mener une guerre difficile contre les humains.","14/12/2022", R.drawable.avatar, 4.5F),
            Movies("Elementaire","Dans la ville d'Element City, le feu, l'eau, la terre et l'air vivent dans la plus parfaite harmonie. C'est ici que résident Flam, une jeune femme intrépide et vive d'esprit, au caractère bien trempé, et Flack, un garçon sentimental et amusant, plutôt suiveur dans l'âme. L'amitié qu'ils se portent remet en question les croyances de Flam sur le monde dans lequel ils vivent.","21/06/2023", R.drawable.elementaire, 3.9F),
        )

        moviesAdapter = MoviesAdapter(items)
        recyclerViewMovies.apply {
            recyclerViewMovies.layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = moviesAdapter

        }


        imageSearch.setOnClickListener {
            val search = editSearch.text.toString()
            moviesAdapter.filter.filter(search)
        }

    }

}
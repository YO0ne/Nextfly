package com.example.nextfli

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.widget.ToolbarWidgetWrapper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var recyclerViewMovies: RecyclerView
    lateinit var moviesAdapter: MoviesAdapter
    lateinit var toolbar: Toolbar
    lateinit var imageSearch: ImageView
    lateinit var editSearch: EditText

    lateinit var tvResponse: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerViewMovies = findViewById(R.id.recyclerViewMovies)
        toolbar = findViewById(R.id.toolbar)
        imageSearch = toolbar.findViewById(R.id.imageSearch)
        editSearch = toolbar.findViewById(R.id.editSearch)
        setSupportActionBar(toolbar)

        tvResponse = findViewById(R.id.tvResponse)

        //TODO: create retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val movieService = retrofit.create(MovieService::class.java)

        //TODO: call moviedb api
        val result = movieService.getMovie()

        result.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    tvResponse.text = response.body()
                }

            }

            override fun onFailure(call: Call<String>, t: Throwable) {
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
package com.example.nextfli

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MoviesAdapter(var items: List<Movies>) : RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>(), Filterable {


    var moviesFilteredList: List<Movies> = ArrayList()

    init {
        moviesFilteredList = items
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var charSearch = constraint.toString()
                if(charSearch.isEmpty()) {
                    moviesFilteredList = items
                } else {
                    var resultList = ArrayList<Movies>()
                    for (movies in items) {
                        if(movies.original_title.lowercase().contains(charSearch.lowercase())
                        || movies.overview.lowercase().contains(charSearch.lowercase())){
                            resultList.add(movies)
                        }
                    }
                    moviesFilteredList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = moviesFilteredList
                return filterResults

            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                moviesFilteredList = results?.values as ArrayList<Movies>
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MoviesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val movies = moviesFilteredList[position]
        holder.bind(movies)
    }

    override fun getItemCount() = moviesFilteredList.size

    inner class MoviesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var titleMovie: TextView
        var overviewText: TextView
        var releaseDate: TextView
        var posterMovie: ImageView
        var rating : RatingBar

        init {
            titleMovie = itemView.findViewById(R.id.titleMovie)
            overviewText = itemView.findViewById(R.id.overviewText)
            releaseDate = itemView.findViewById(R.id.releaseDate)
            posterMovie = itemView.findViewById(R.id.posterMovie)
            rating = itemView.findViewById(R.id.rating)
        }
        fun bind(movies : Movies) {
            titleMovie.text = movies.original_title
            overviewText.text = movies.overview
            releaseDate.text = movies.release_date
            posterMovie.setImageResource(movies.poster_path)
            rating.rating = movies.vote_average
        }
    }


}

package com.example.usoapi

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
//import android.widget.SearchView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.usoapi.data.Api
import com.example.usoapi.data.Pokemon
import com.example.usoapi.data.PokemonResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var linearLayout: LinearLayout
    private lateinit var searchView: SearchView
    private lateinit var apiService: Api.RemoteService
    private lateinit var pokemonList: List<Pokemon>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        linearLayout = findViewById(R.id.linearLayout)
        searchView = findViewById(R.id.searchView)
        apiService = Api(this).build() // Pasa el contexto aquí

        init()
        setupSearchView()
    }


    private fun init() {
        val request = apiService.loadPokemon(151)

        request.enqueue(object : Callback<PokemonResponse> {
            override fun onResponse(call: Call<PokemonResponse>, response: Response<PokemonResponse>) {
                if (response.isSuccessful) {
                    val pokemonResponse = response.body()
                    pokemonList = pokemonResponse?.results ?: emptyList()
                    displayPokemonList(pokemonList)
                } else {
                    println("Error en la respuesta: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<PokemonResponse>, t: Throwable) {
                println("Error en la solicitud: ${t.message}")
            }
        })
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                performSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Puedes agregar lógica adicional si deseas realizar la búsqueda mientras el usuario escribe
                return true
            }
        })
    }

    private fun performSearch(query: String?) {
        if (query.isNullOrBlank()) {
            // Si la consulta está vacía, muestra la lista completa de Pokémon
            displayPokemonList(pokemonList)
        } else {
            // Filtra la lista de Pokémon por nombre y muestra los resultados
            val filteredPokemonList = pokemonList.filter { it.name.contains(query, ignoreCase = true) }
            if (filteredPokemonList.isEmpty()) {
                // Maneja el caso en el que no se encuentran resultados
                val noResultsTextView = TextView(this)
                noResultsTextView.text = "No se encontraron resultados."
                linearLayout.removeAllViews()
                linearLayout.addView(noResultsTextView)
            } else {
                displayPokemonList(filteredPokemonList)
            }
        }
    }


    private fun displayPokemonList(pokemonList: List<Pokemon>) {
        linearLayout.removeAllViews()

        if (pokemonList.isEmpty()) {
            // Maneja el caso en el que no se encuentran resultados
            val noResultsTextView = TextView(this)
            noResultsTextView.text = "No se encontraron resultados."
            linearLayout.addView(noResultsTextView)
        } else {
            // Muestra los Pokémon encontrados
            for (pokemon in pokemonList) {
                val textView = TextView(this)
                textView.text = pokemon.name
                linearLayout.addView(textView)

                val imageView = ImageView(this)
                val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${pokemon.getNumber()}.png"
                Glide.with(this).load(imageUrl).into(imageView)
                linearLayout.addView(imageView)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_clear -> {
                // Borra el texto del SearchView y muestra la lista completa de Pokémon
                searchView.setQuery("", false)
                performSearch("")
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    fun Pokemon.getNumber(): Int {
        val parts = url.split("/")
        return parts[parts.size - 2].toInt()
    }
}

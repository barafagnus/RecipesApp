package ru.vysokov.recipesapp.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import kotlinx.serialization.json.Json
import ru.vysokov.recipesapp.R
import ru.vysokov.recipesapp.databinding.ActivityMainBinding
import ru.vysokov.recipesapp.model.Category
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navOptions = navOptions {
            anim {
                enter = androidx.navigation.ui.R.anim.nav_default_enter_anim
                exit = androidx.navigation.ui.R.anim.nav_default_exit_anim
                popEnter = androidx.navigation.ui.R.anim.nav_default_pop_enter_anim
                popExit = androidx.navigation.ui.R.anim.nav_default_exit_anim
            }
            launchSingleTop = true
        }

        with(binding) {
            btnCategories.setOnClickListener {
                navController.navigate(R.id.categoriesListFragment, null, navOptions)
            }

            btnFavorites.setOnClickListener {
                navController.navigate(R.id.favoritesFragment, null, navOptions)
            }
        }

        Log.i("!!!", "Метод onCreate выполняется на потоке: ${Thread.currentThread().name}")

        val thread = Thread {
            val url = URL("https://recipes.androidsprint.ru/api/category")
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()

            Log.i("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")
            Log.i("!!!", "code: ${connection.responseCode}")
            Log.i("!!!", "msg: ${connection.responseMessage}")

            val raw = connection.inputStream.bufferedReader().readText()
            Log.i("!!!", "body: $raw")

            val categories = Json.decodeFromString<List<Category>>(raw)
            categories.forEach { Log.i("!!!", "object: ${it}") }
        }

        thread.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
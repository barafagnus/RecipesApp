package ru.vysokov.recipesapp.ui.recipes.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.vysokov.recipesapp.databinding.FragmentFavoritesBinding
import ru.vysokov.recipesapp.ui.recipes.recipeslist.RecipesListAdapter

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException()
    private lateinit var recipesListAdapter: RecipesListAdapter
    private val viewModel: FavoritesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        viewModel.loadFavorites()
    }

    private fun initRecycler() {
        recipesListAdapter = RecipesListAdapter(emptyList())
        binding.rvRecipes.adapter = recipesListAdapter

        recipesListAdapter.setOnItemClickListener(
            object : RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipeId(recipeId)
                }
            }
        )

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            if (!state.isLoaded) return@observe

            recipesListAdapter.dataSet = state.favoriteRecipes
            binding.tvEmptyFavorites.visibility =
                if (state.favoriteRecipes.isEmpty()) View.VISIBLE else View.INVISIBLE
        }

        viewModel.errorEvent.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(context, getString(errorMessage), Toast.LENGTH_LONG).show()
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val action = FavoritesFragmentDirections.actionFavoritesFragmentToRecipeFragment(recipeId)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
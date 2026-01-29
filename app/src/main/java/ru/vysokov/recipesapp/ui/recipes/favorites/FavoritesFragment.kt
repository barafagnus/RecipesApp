package ru.vysokov.recipesapp.ui.recipes.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import ru.vysokov.recipesapp.R
import ru.vysokov.recipesapp.core.RecipeConstants
import ru.vysokov.recipesapp.data.repository.STUB
import ru.vysokov.recipesapp.data.utils.FavoritesManager
import ru.vysokov.recipesapp.databinding.FragmentFavoritesBinding
import ru.vysokov.recipesapp.ui.recipes.recipe.RecipeFragment
import ru.vysokov.recipesapp.ui.recipes.recipeslist.RecipesListAdapter

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException()

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
    }

    private fun initRecycler() {
        val recipeIds = FavoritesManager.getFavorites(requireContext()).map { it.toInt() }.toSet()
        val dataset = STUB.getRecipesById(recipeIds)
        val recipesListAdapter = RecipesListAdapter(dataset)
        val recipesListRecyclerView = binding.rvRecipes
        recipesListRecyclerView.adapter = recipesListAdapter

        if (recipeIds.isEmpty()) {
            binding.tvEmptyFavorites.visibility = View.VISIBLE
        } else binding.tvEmptyFavorites.visibility = View.INVISIBLE

        recipesListAdapter.setOnItemClickListener(
            object : RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipeId(recipeId)
                }
            }
        )
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val bundle = Bundle()
        bundle.putInt(RecipeConstants.ARG_RECIPE_ID, recipeId)

        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
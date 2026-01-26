package ru.vysokov.recipesapp.ui.recipes.recipeslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import ru.vysokov.recipesapp.core.CategoryConstants
import ru.vysokov.recipesapp.R
import ru.vysokov.recipesapp.core.RecipeConstants
import ru.vysokov.recipesapp.data.repository.STUB
import ru.vysokov.recipesapp.data.utils.AssetLoader
import ru.vysokov.recipesapp.databinding.FragmentRecipesListBinding
import ru.vysokov.recipesapp.ui.recipes.recipe.RecipeFragment

class RecipesListFragment : Fragment() {
    private var _binding: FragmentRecipesListBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException()
    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImage: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRecipesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments

        bundle?.let {
            categoryId = bundle.getInt(CategoryConstants.ARG_CATEGORY_ID, 0)
            categoryName = bundle.getString(CategoryConstants.ARG_CATEGORY_NAME)
            categoryImage = bundle.getString(CategoryConstants.ARG_CATEGORY_IMAGE_URL)

            with(binding) {
                ivRecipes.setImageDrawable(AssetLoader.loadAssets(view.context, categoryImage))
                tvRecipesTitle.text = categoryName
            }

            initRecycler(categoryId)
        }
    }

    private fun initRecycler(categoryId: Int?) {
        val dataset = STUB.getRecipesByCategoryId(categoryId)
        val recipesListAdapter = RecipesListAdapter(dataset)
        val recipesListRecyclerView = binding.rvRecipes
        recipesListRecyclerView.adapter = recipesListAdapter

        recipesListAdapter.setOnItemClickListener(
            object : RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipeId(recipeId)
                }
            }
        )
    }

    private fun openRecipeByRecipeId(recipeId: Int?) {
        val recipe = STUB.getRecipeById(recipeId)
        val bundle = Bundle()
        bundle.putParcelable(RecipeConstants.ARG_RECIPE, recipe)

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

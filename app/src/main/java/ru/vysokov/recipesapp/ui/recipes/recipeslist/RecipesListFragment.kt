package ru.vysokov.recipesapp.ui.recipes.recipeslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.vysokov.recipesapp.core.CategoryConstants
import ru.vysokov.recipesapp.data.utils.AssetLoader
import ru.vysokov.recipesapp.databinding.FragmentRecipesListBinding

class RecipesListFragment : Fragment() {
    private var _binding: FragmentRecipesListBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException()
    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImage: String? = null
    private val viewModel: RecipesListViewModel by viewModels()
    lateinit var recipesListAdapter: RecipesListAdapter

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

            initRecycler()
            viewModel.loadRecipes(categoryId)
        }
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

            recipesListAdapter.dataSet = state.recipe
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val action = RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(recipeId)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package ru.vysokov.recipesapp.ui.recipes.recipeslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import ru.vysokov.recipesapp.R
import ru.vysokov.recipesapp.data.local.DatabaseClient
import ru.vysokov.recipesapp.data.network.NetworkClient
import ru.vysokov.recipesapp.data.repository.RecipesRepository
import ru.vysokov.recipesapp.databinding.FragmentRecipesListBinding

class RecipesListViewModelFactory(
    private val repository: RecipesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return RecipesListViewModel(repository) as T
    }
}

class RecipesListFragment : Fragment() {
    private var _binding: FragmentRecipesListBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException()
    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImage: String? = null
    private val databaseClient by lazy { DatabaseClient.getInstance(requireContext()) }

    private val viewModel: RecipesListViewModel by viewModels {
        RecipesListViewModelFactory(
            RecipesRepository(
                NetworkClient.recipesService,
                databaseClient.categoryDao(),
                databaseClient.recipesDao()
            )
        )
    }
    lateinit var recipesListAdapter: RecipesListAdapter
    private val args: RecipesListFragmentArgs by navArgs()

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
        categoryId = args.category?.id
        categoryName = args.category?.title
        categoryImage = args.category?.imageUrl
        val imageUrl = "${NetworkClient.URL_IMAGES}${categoryImage}"

        Glide.with(requireContext())
            .load(imageUrl)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(binding.ivRecipes)
        binding.tvRecipesTitle.text = categoryName

        initRecycler()
        viewModel.loadRecipes(categoryId)
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

        viewModel.errorEvent.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(context, getString(errorMessage), Toast.LENGTH_LONG).show()
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val action =
            RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(recipeId)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
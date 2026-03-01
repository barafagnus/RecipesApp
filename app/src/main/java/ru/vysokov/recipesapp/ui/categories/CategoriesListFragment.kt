package ru.vysokov.recipesapp.ui.categories

import android.app.Application
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
import androidx.recyclerview.widget.GridLayoutManager
import ru.vysokov.recipesapp.data.local.DatabaseClient
import ru.vysokov.recipesapp.data.network.NetworkClient
import ru.vysokov.recipesapp.data.repository.RecipesRepository
import ru.vysokov.recipesapp.databinding.FragmentListCategoriesBinding

class CategoriesListViewModelFactory(
    private val repository: RecipesRepository,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return CategoriesListViewModel(application, repository) as T
    }
}

class CategoriesListFragment : Fragment() {
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException()
    private lateinit var categoriesListAdapter: CategoriesListAdapter
    private val databaseClient by lazy { DatabaseClient.getInstance(requireContext()) }

    private val viewModel: CategoriesListViewModel by viewModels {
        CategoriesListViewModelFactory(
            RecipesRepository(
                NetworkClient.recipesService,
                databaseClient.categoryDao(),
                databaseClient.recipesDao()
            ),
            requireActivity().application
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        viewModel.loadCategories()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        categoriesListAdapter = CategoriesListAdapter(emptyList())

        with(binding.rvCategories) {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = categoriesListAdapter
        }

        categoriesListAdapter.setOnItemClickListener(
            object : CategoriesListAdapter.OnItemClickListener {
                override fun onItemClick(categoryId: Int) {
                    openRecipesByCategoryId(categoryId)
                }
            }
        )

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            if (!state.isLoaded) return@observe

            categoriesListAdapter.dataSet = state.categories
        }

        viewModel.errorEvent.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(context, getString(errorMessage), Toast.LENGTH_LONG).show()
        }

    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        val category = viewModel.uiState.value?.categories?.find { it.id == categoryId }
            ?: throw IllegalArgumentException()
        val action =
            CategoriesListFragmentDirections.actionCategoriesListFragmentToRecipesListFragment(
                category
            )
        findNavController().navigate(action)
    }
}
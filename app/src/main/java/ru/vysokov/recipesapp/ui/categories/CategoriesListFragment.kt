package ru.vysokov.recipesapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import ru.vysokov.recipesapp.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment() {
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException()
    private val viewModel: CategoriesListViewModel by viewModels()
    private lateinit var categoriesListAdapter: CategoriesListAdapter


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
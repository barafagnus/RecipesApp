package ru.vysokov.recipesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.vysokov.recipesapp.data.repository.STUB
import ru.vysokov.recipesapp.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment() {
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException()

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

        val dataset = STUB.getCategories()
        val categoriesListAdapter = CategoriesListAdapter(dataset)

        val categoriesListRecyclerView = view.findViewById<RecyclerView>(R.id.rvCategories)
        categoriesListRecyclerView.layoutManager = GridLayoutManager(view.context, 2)
        categoriesListRecyclerView.adapter = categoriesListAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
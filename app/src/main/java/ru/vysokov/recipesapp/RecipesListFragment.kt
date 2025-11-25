package ru.vysokov.recipesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.vysokov.recipesapp.databinding.FragmentRecipesListBinding

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
            categoryId = bundle.getInt("ARG_CATEGORY_ID", 0)
            categoryName = bundle.getString("ARG_CATEGORY_NAME")
            categoryImage = bundle.getString("ARG_CATEGORY_IMAGE_URL")

            with(binding) {
                tvCategoryId.text = "category_id $categoryId"
                tvCategoryName.text = "category_name $categoryName"
                tvCategoryImageUrl.text = "category_image_url $categoryImage"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

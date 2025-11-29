package ru.vysokov.recipesapp

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.vysokov.recipesapp.databinding.FragmentRecipeBinding
import ru.vysokov.recipesapp.models.Recipe


class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle?.getParcelable(RecipeConstants.ARG_RECIPE, Recipe::class.java)
        } else {
            bundle?.getParcelable(RecipeConstants.ARG_RECIPE)
        }

        with(binding) {
            tvRecipeImage.text = recipe?.imageUrl
            tvRecipeTitle.text = recipe?.title
            tvRecipeIngredients.text = recipe?.ingredients?.firstOrNull().toString()
            tvRecipeMethod.text = recipe?.method?.firstOrNull()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
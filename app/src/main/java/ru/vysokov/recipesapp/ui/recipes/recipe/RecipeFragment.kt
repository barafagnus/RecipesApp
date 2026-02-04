package ru.vysokov.recipesapp.ui.recipes.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.vysokov.recipesapp.R
import ru.vysokov.recipesapp.core.RecipeConstants
import ru.vysokov.recipesapp.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException()
    private val viewModel: RecipeViewModel by viewModels()
    private lateinit var ingredientsAdapter: IngredientsAdapter
    private lateinit var methodAdapter: MethodAdapter
    private var isInitUi = false

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
        val recipeId = getRecipeIdFromBundle()

        initUi(recipeId)
        viewModel.loadRecipe(recipeId)
    }

    private fun getRecipeIdFromBundle(): Int? {
        val bundle = arguments
        return bundle?.getInt(RecipeConstants.ARG_RECIPE_ID)
    }

    private fun initUi(recipeId: Int?) {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            if (!state.isLoaded) return@observe

            if (!isInitUi) {
                ingredientsAdapter = IngredientsAdapter(emptyList())
                methodAdapter = MethodAdapter(emptyList())

                with(binding.rvIngredients) {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = ingredientsAdapter
                    isNestedScrollingEnabled = false
                    setHasFixedSize(false)

                    addItemDecoration(
                        MaterialDividerItemDecoration(
                            requireContext(),
                            MaterialDividerItemDecoration.VERTICAL
                        ).apply {
                            dividerColor = ContextCompat.getColor(context, R.color.divider_color)
                            isLastItemDecorated = false
                        }
                    )
                }

                binding.sbNumberOfPortions.setOnSeekBarChangeListener(object :
                    SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        viewModel.updatePortion(progress)
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                })

                with(binding.rvMethod) {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = methodAdapter
                    isNestedScrollingEnabled = false
                    setHasFixedSize(false)

                    addItemDecoration(
                        MaterialDividerItemDecoration(
                            requireContext(),
                            MaterialDividerItemDecoration.VERTICAL
                        ).apply {
                            dividerColor = ContextCompat.getColor(context, R.color.divider_color)
                            isLastItemDecorated = false
                        }
                    )
                }
                isInitUi = true
            }

            ingredientsAdapter.updateDataSet(state.ingredients)
            methodAdapter.updateDataSet(state.method)
            ingredientsAdapter.updateIngredients(state.portionsCount)

            with(binding) {
                ivImage.setImageDrawable(state.recipeImage)
                tvTitle.text = state.title

                updateFavoriteIcon(state.isFavorite)

                ibToFavorites.setOnClickListener {
                    viewModel.onFavoritesClicked(recipeId)
                }

                numberOfPortions.text = state.portionsCount.toString()
            }
        }
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        val iconRes = if (isFavorite) R.drawable.ic_heart else R.drawable.ic_heart_empty
        binding.ibToFavorites.setImageDrawable(
            ContextCompat.getDrawable(requireContext(), iconRes)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
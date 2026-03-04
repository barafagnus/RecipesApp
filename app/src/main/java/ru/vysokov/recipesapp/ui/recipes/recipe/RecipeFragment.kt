package ru.vysokov.recipesapp.ui.recipes.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.divider.MaterialDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import ru.vysokov.recipesapp.R
import ru.vysokov.recipesapp.databinding.FragmentRecipeBinding

@AndroidEntryPoint
class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException()
    private lateinit var ingredientsAdapter: IngredientsAdapter
    private lateinit var methodAdapter: MethodAdapter
    private var isInitUi = false
    private val args: RecipeFragmentArgs by navArgs()
    private val viewModel: RecipeViewModel by viewModels()

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
        val recipeId = args.recipeId

        initUi(recipeId)
        viewModel.loadRecipe(recipeId)
    }

    private fun initUi(recipeId: Int?) {
        ingredientsAdapter = IngredientsAdapter(emptyList())
        methodAdapter = MethodAdapter(emptyList())

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            if (!state.isLoaded) return@observe

            if (!isInitUi) {
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

                binding.sbNumberOfPortions.setOnSeekBarChangeListener(
                    PortionSeekBarListener { progress ->
                        viewModel.updatePortion(progress)
                    }
                )

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
                            dividerColor =
                                ContextCompat.getColor(context, R.color.divider_color)
                            isLastItemDecorated = false
                        }
                    )
                }
                isInitUi = true
            }

            ingredientsAdapter.dataSet = state.ingredients
            methodAdapter.dataSet = state.method
            ingredientsAdapter.quantity = state.portionsCount

            if (binding.ivImage.tag != state.recipeImageUrl) {
                Glide.with(requireContext())
                    .load(state.recipeImageUrl)
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_error)
                    .into(binding.ivImage)
                binding.ivImage.tag = state.recipeImageUrl
            }

            with(binding) {
                tvTitle.text = state.title

                updateFavoriteIcon(state.isFavorite)

                ibToFavorites.setOnClickListener {
                    viewModel.onFavoritesClicked(recipeId)
                }

                numberOfPortions.text = state.portionsCount.toString()
            }
        }

        viewModel.errorEvent.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(context, getString(errorMessage), Toast.LENGTH_LONG).show()
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
        isInitUi = false
    }
}

class PortionSeekBarListener(
    private val onChangeIngredients: (Int) -> Unit
) : SeekBar.OnSeekBarChangeListener {

    override fun onProgressChanged(
        seekBar: SeekBar?,
        progress: Int,
        fromUser: Boolean
    ) {
        onChangeIngredients(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

}
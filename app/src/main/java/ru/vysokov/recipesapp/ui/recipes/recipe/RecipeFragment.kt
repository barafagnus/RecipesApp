package ru.vysokov.recipesapp.ui.recipes.recipe

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.vysokov.recipesapp.R
import ru.vysokov.recipesapp.core.RecipeConstants
import ru.vysokov.recipesapp.data.utils.AssetLoader
import ru.vysokov.recipesapp.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException()
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
        val recipeId = getRecipeIdFromBundle()
        viewModel.loadRecipe(recipeId)

        initUi(view, recipeId)

        val ingredientsAdapter = IngredientsAdapter(
            viewModel.uiState.value?.ingredients.orEmpty()
        )
        initRecycler(
            view.context,
            binding.rvIngredients,
            ingredientsAdapter
        )
        initSeekBar(ingredientsAdapter)

        initRecycler(
            view.context,
            binding.rvMethod,
            MethodAdapter(
                viewModel.uiState.value?.method.orEmpty()
            )
        )

    }

    private fun getRecipeIdFromBundle(): Int? {
        val bundle = arguments
        return bundle?.getInt(RecipeConstants.ARG_RECIPE_ID)
    }

    private fun initUi(view: View, recipeId: Int?) {

        viewModel.uiState.observe(viewLifecycleOwner) { state ->

            with(binding) {
                ivImage.setImageDrawable(AssetLoader.loadAssets(view.context, state.imageUrl))
                tvTitle.text = state.title

                updateFavoriteIcon(state.isFavorite)

                ibToFavorites.setOnClickListener {
                    viewModel.onFavoritesClicked(recipeId)
                }
            }
        }
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        val iconRes = if (isFavorite) R.drawable.ic_heart else R.drawable.ic_heart_empty
        binding.ibToFavorites.setImageDrawable(
            ContextCompat.getDrawable(requireContext(), iconRes)
        )
    }

    private fun initRecycler(
        context: Context,
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<*>
    ) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.setHasFixedSize(false)
        addDivider(context, recyclerView)
    }

    private fun initSeekBar(adapter: IngredientsAdapter) {
        binding.sbNumberOfPortions.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                adapter.updateIngredients(progress)
                binding.numberOfPortions.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun addDivider(context: Context, recyclerView: RecyclerView) {
        val divider = MaterialDividerItemDecoration(
            context,
            MaterialDividerItemDecoration.VERTICAL
        )
        with(divider) {
            dividerColor = ContextCompat.getColor(context, R.color.divider_color)
            isLastItemDecorated = false
        }
        recyclerView.addItemDecoration(divider)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
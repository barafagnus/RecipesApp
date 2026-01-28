package ru.vysokov.recipesapp.ui.recipes.recipe

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import ru.vysokov.recipesapp.data.utils.FavoritesManager
import ru.vysokov.recipesapp.databinding.FragmentRecipeBinding
import ru.vysokov.recipesapp.model.Recipe

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
        val recipe = getRecipeFromBundle()
        initUi(view, recipe)

        val ingredientsAdapter = IngredientsAdapter(recipe?.ingredients.orEmpty())
        initRecycler(
            view.context,
            binding.rvIngredients,
            ingredientsAdapter
        )
        initSeekBar(ingredientsAdapter)

        initRecycler(
            view.context,
            binding.rvMethod,
            MethodAdapter(recipe?.method.orEmpty())
        )

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            Log.i("!!!", state.isFavorite.toString())
        }
    }

    private fun getRecipeFromBundle(): Recipe? {
        val bundle = arguments

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle?.getParcelable(RecipeConstants.ARG_RECIPE, Recipe::class.java)
        } else {
            bundle?.getParcelable(RecipeConstants.ARG_RECIPE)
        }
    }

    private fun initUi(view: View, recipe: Recipe?) {
        val favorites = FavoritesManager.getFavorites(requireContext())
        val currentId = recipe?.id.toString()

        with(binding) {
            ivImage.setImageDrawable(AssetLoader.loadAssets(view.context, recipe?.imageUrl))
            tvTitle.text = recipe?.title.orEmpty()

            updateFavoriteIcon(currentId in favorites)

            ibToFavorites.setOnClickListener {
                val isFavorite = if (currentId in favorites) {
                    favorites.remove(currentId)
                    false
                } else {
                    favorites.add(currentId)
                    true
                }
                updateFavoriteIcon(isFavorite)
                FavoritesManager.saveFavorites(requireContext(), favorites)
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
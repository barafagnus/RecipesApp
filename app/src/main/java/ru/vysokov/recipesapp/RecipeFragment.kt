package ru.vysokov.recipesapp

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.vysokov.recipesapp.data.utils.AssetLoader
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
        with(binding) {
            ivImage.setImageDrawable(AssetLoader.loadAssets(view.context, recipe?.imageUrl))
            tvTitle.text = recipe?.title.orEmpty()
            ibToFavorites.setOnClickListener {
                ibToFavorites.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_heart
                    )
                )
            }
        }
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
            OnSeekBarChangeListener {
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
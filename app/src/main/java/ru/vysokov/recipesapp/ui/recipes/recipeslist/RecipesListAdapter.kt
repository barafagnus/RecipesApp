package ru.vysokov.recipesapp.ui.recipes.recipeslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.vysokov.recipesapp.R
import ru.vysokov.recipesapp.data.utils.AssetLoader
import ru.vysokov.recipesapp.databinding.ItemRecipeBinding
import ru.vysokov.recipesapp.model.Recipe

class RecipesListAdapter(dataSet: List<Recipe>) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {
    var dataSet: List<Recipe> = dataSet
        get() = field
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    class ViewHolder(
        binding: ItemRecipeBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        val ivRecipeCard = binding.ivRecipeCard
        val ivRecipeCardTitle = binding.tvRecipeCardTitle
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_recipe, viewGroup, false)

        return ViewHolder(ItemRecipeBinding.bind(view))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val recipe = dataSet[position]
        with(viewHolder) {
            ivRecipeCard.setImageDrawable(
                AssetLoader.loadAssets(
                    viewHolder.itemView.context,
                    recipe.imageUrl
                )
            )
            ivRecipeCardTitle.text = recipe.title
        }

        viewHolder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(recipe.id)
        }

    }

    override fun getItemCount() = dataSet.size

}
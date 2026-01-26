package ru.vysokov.recipesapp.ui.recipes.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.vysokov.recipesapp.R
import ru.vysokov.recipesapp.databinding.ItemIngredientBinding
import ru.vysokov.recipesapp.model.Ingredient

class IngredientsAdapter(
    private val dataSet: List<Ingredient>
) : RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {
    private var quantity: Int = 1

    class ViewHolder(
        binding: ItemIngredientBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        val tvIngredient = binding.tvIngredient
        val tvQuantity = binding.tvQuantity
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_ingredient, viewGroup, false)
        return ViewHolder(ItemIngredientBinding.bind(view))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val ingredient = dataSet[position]
        with(viewHolder) {
            tvIngredient.text = ingredient.description
            tvQuantity.text =
                "${convertQuantity(ingredient.quantity, quantity)} ${ingredient.unitOfMeasure}"
        }
    }

    override fun getItemCount(): Int = dataSet.size

    fun updateIngredients(progress: Int) {
        quantity = progress
        notifyDataSetChanged()
    }

    private fun convertQuantity(ingredientQuantity: String, sbQuantity: Int): String {
        val value = ingredientQuantity.toBigDecimalOrNull() ?: return ""
        val result = value.multiply(sbQuantity.toBigDecimal())
        return result.stripTrailingZeros().toPlainString()
    }
}
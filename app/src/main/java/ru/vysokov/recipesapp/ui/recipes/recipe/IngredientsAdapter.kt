package ru.vysokov.recipesapp.ui.recipes.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.vysokov.recipesapp.R
import ru.vysokov.recipesapp.databinding.ItemIngredientBinding
import ru.vysokov.recipesapp.model.Ingredient

class IngredientsAdapter(
    dataSet: List<Ingredient>
) : RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {
    var dataSet: List<Ingredient> = dataSet
        get() = field
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var quantity: Int = 1
        set(value) {
            field = value
            notifyDataSetChanged()
        }

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

    private fun convertQuantity(ingredientQuantity: String, sbQuantity: Int): String {
        val value = ingredientQuantity.toBigDecimalOrNull() ?: return ""
        val result = value.multiply(sbQuantity.toBigDecimal())
        return result.stripTrailingZeros().toPlainString()
    }
}
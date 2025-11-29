package ru.vysokov.recipesapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.vysokov.recipesapp.databinding.ItemIngredientBinding
import ru.vysokov.recipesapp.models.Ingredient

class IngredientsAdapter(
    private val dataSet: List<Ingredient>
) : RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

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
            tvQuantity.text = "${ingredient.quantity} ${ingredient.unitOfMeasure}"
        }
    }

    override fun getItemCount(): Int = dataSet.size
}
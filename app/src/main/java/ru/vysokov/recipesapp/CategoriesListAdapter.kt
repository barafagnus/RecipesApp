package ru.vysokov.recipesapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.vysokov.recipesapp.databinding.ItemCategoryBinding
import ru.vysokov.recipesapp.models.Category

class CategoriesListAdapter(
    private val dataSet: List<Category>
) : RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick()
    }

    class ViewHolder(
        binding: ItemCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        val ivCategoryCard: ImageView = binding.ivCategoryCard
        val tvCardTitle: TextView = binding.tvCardTitle
        val tvCardDescription: TextView = binding.tvCardDescription
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_category, viewGroup, false)
        return ViewHolder(ItemCategoryBinding.bind(view))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val category = dataSet[position]

        val drawable =
            try {
                Drawable.createFromStream(
                    viewHolder.itemView.context.assets.open(category.imageUrl),
                    null
                )
            } catch (e: Exception) {
                Log.e("!!! CategoriesListAdapter", e.stackTraceToString())
                null
            }

        with(viewHolder) {
            ivCategoryCard.setImageDrawable(drawable)
            tvCardTitle.text = category.title
            tvCardDescription.text = category.description
        }

        viewHolder.itemView.setOnClickListener {
            itemClickListener?.onItemClick()
        }
    }

    override fun getItemCount() = dataSet.size

}
package ru.vysokov.recipesapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.vysokov.recipesapp.databinding.ItemMethodBinding

class MethodAdapter(
    private val dataSet: List<String>
) : RecyclerView.Adapter<MethodAdapter.ViewHolder>() {

    class ViewHolder(
        binding: ItemMethodBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        val tvMethod = binding.tvMethod
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_method, viewGroup, false)
        return ViewHolder(ItemMethodBinding.bind(view))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val method = dataSet[position]
        viewHolder.tvMethod.text = "${position.plus(1)}. $method"
    }

    override fun getItemCount(): Int = dataSet.size
}
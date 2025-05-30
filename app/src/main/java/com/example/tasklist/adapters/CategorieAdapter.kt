package com.example.tasklist.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tasklist.data.Categorie
import com.example.tasklist.data.Task
import com.example.tasklist.data.TaskDAO
import com.example.tasklist.databinding.ItemCategorieBinding


class CategorieAdapter (
    context: Context,
    private var dataSet: List<Categorie> = emptyList(),
    private val onItemClickListener: (Int) -> Unit,
    private val onItemEditClickListener: (Int) -> Unit,
    private val onItemDeleteClickListener: (Int) -> Unit,
    private val onClickListener: (position:Int) -> Unit,
    private val onLongClickListener: (position:Int) -> Boolean
) : RecyclerView.Adapter<CategorieViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategorieViewHolder {
        val binding = ItemCategorieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategorieViewHolder(binding)
    }
    override fun getItemCount(): Int = dataSet.size
    override fun onBindViewHolder(holder: CategorieViewHolder, position: Int) {
        holder.render(dataSet[position])
        holder.itemView.setOnClickListener {
            onItemClickListener(position)
        }
        holder.binding.editButtonCategorie.setOnClickListener {
            onItemEditClickListener(position)
        }
        holder.binding.deleteButtonCategorie.setOnClickListener {
            onItemDeleteClickListener(position)
        }
        holder.itemView.setOnClickListener { onClickListener(position) }
        holder.itemView.setOnLongClickListener { onLongClickListener(position) }
        }

    fun updateData(dataSet: List<Categorie>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }
}
class CategorieViewHolder(val binding: ItemCategorieBinding) : RecyclerView.ViewHolder(binding.root) {

    private var taskDAO: TaskDAO = TaskDAO(itemView.context)
    fun render(categorie: Categorie) {
        binding.nameTextViewCategorie.text = categorie.name
        binding.countTaskTextView.text = taskDAO.countByCategoryAndDone(categorie).toString()
    }
}
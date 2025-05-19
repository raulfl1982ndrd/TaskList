package com.example.tasklist.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.tasklist.data.Task


class TaskDiffUtils(private val oldList: List<Task>,
                    private val newList: List<Task>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].id != newList[newItemPosition].id -> false
            oldList[oldItemPosition].name != newList[newItemPosition].name -> false
            oldList[oldItemPosition].done != newList[newItemPosition].done -> false
            else -> true

        }
    }
}
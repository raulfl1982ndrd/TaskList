import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tasklist.R
import com.example.tasklist.data.Task
import com.example.tasklist.databinding.ItemTaskBinding
import com.example.tasklist.utils.TaskDiffUtils

class TaskAdapter (
    private var dataSet: List<Task> = emptyList(),
    private val onItemClickListener: (Int) -> Unit,
    private val onItemEditClickListener: (Int) -> Unit,
    private val onItemClickCheckBoxListener: (Int) -> Unit,
    private val onItemClickRemoveListener: (Int) -> Unit
) : RecyclerView.Adapter<TaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }
    override fun getItemCount(): Int = dataSet.size
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.render(dataSet[position])
        holder.itemView.setOnClickListener {
            onItemClickListener(position)
        }
        holder.binding.editButtonTask.setOnClickListener {
            onItemEditClickListener(position)
        }
        holder.binding.doneCheckBox.setOnClickListener {
            onItemClickCheckBoxListener(position)
        }

        holder.binding.deleteButton.setOnClickListener {
            onItemClickRemoveListener(position)
        }

    }

    fun updateData(dataSet: List<Task>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    fun updateItems(results: List<Task>) {
        val diffUtils = TaskDiffUtils(dataSet, results)
        val diffResult = DiffUtil.calculateDiff(diffUtils)
        dataSet = results
        diffResult.dispatchUpdatesTo(this)
    }
}
class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

    fun render(task: Task) {
        binding.nameTextView.text = task.name
        binding.doneCheckBox.isChecked = task.done==1
        if (task.done==1)
            binding.nameTextView.setTextColor(itemView.context.getColor(R.color.green))
        else
            binding.nameTextView.setTextColor(itemView.context.getColor(R.color.red))
    }
}
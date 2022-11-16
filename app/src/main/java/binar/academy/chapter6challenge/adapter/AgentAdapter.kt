@file:Suppress("unused", "unused", "unused", "unused", "unused", "unused")

package binar.academy.chapter6challenge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import binar.academy.chapter6challenge.databinding.ItemAgentBinding
import binar.academy.chapter6challenge.model.AgentResponse
import com.bumptech.glide.Glide

@Suppress("unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused")
class AgentAdapter(onAgentClick: OnAgentClick) : RecyclerView.Adapter<AgentAdapter.ViewHolder>() {

    private var listAgent = arrayListOf<AgentResponse>()
    private lateinit var onAgentClick: OnAgentClick

    init {
        this.onAgentClick = onAgentClick
    }

    class ViewHolder(val binding: ItemAgentBinding) : RecyclerView.ViewHolder(binding.root)

    fun addData(list: List<AgentResponse>) {
        val positionStart = listAgent.size
        listAgent.addAll(list)
        notifyItemRangeInserted(positionStart, listAgent.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemAgentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.agentName.text = listAgent[position].name
        holder.binding.agentRole.text = listAgent[position].role
        Glide.with(holder.itemView.context)
            .load(listAgent[position].roleIcon)
            .into(holder.binding.agentIcon)
        Glide.with(holder.itemView.context)
            .load(listAgent[position].image)
            .into(holder.binding.agentImage)
        holder.itemView.setOnClickListener {
            this.onAgentClick.onAgentSelected(listAgent[position])
        }
    }

    override fun getItemCount(): Int = listAgent.size

    interface OnAgentClick {
        fun onAgentSelected(agentResponse: AgentResponse)
    }

}

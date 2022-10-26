package binar.academy.chapter6challenge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import binar.academy.chapter6challenge.database.AgentModel
import binar.academy.chapter6challenge.databinding.ItemFavoriteBinding
import com.bumptech.glide.Glide

class AgentAdapterFavorite(onAgentClick: OnAgentFavoriteClick) :
    RecyclerView.Adapter<AgentAdapterFavorite.ViewHolder>() {

    private var listAgent = arrayListOf<AgentModel>()
    private var onAgentClick: OnAgentFavoriteClick

    init {
        this.onAgentClick = onAgentClick
    }

    class ViewHolder(val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root)

    fun clearList() {
        notifyItemRangeRemoved(0, listAgent.size)
        listAgent.clear()
    }

    fun addData(list: List<AgentModel>) {
        val positionStart = listAgent.size
        listAgent.addAll(list)
        notifyItemRangeInserted(positionStart, listAgent.size)
    }

    fun delete(position: Int) {
        listAgent.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.favAgent.text = listAgent[position].name
        holder.binding.favRole.text = listAgent[position].role
        Glide.with(holder.itemView.context)
            .load(listAgent[position].roleIcon)
            .into(holder.binding.favIcon)
        Glide.with(holder.itemView.context)
            .load(listAgent[position].image)
            .into(holder.binding.favImage)
        holder.itemView.setOnClickListener {
            this.onAgentClick.onAgentSelected(listAgent[position])
        }
        holder.binding.favDelete.setOnClickListener {
            this.onAgentClick.onAgentDeleted(listAgent[position].name, position)
        }
    }

    override fun getItemCount(): Int = listAgent.size

    interface OnAgentFavoriteClick {
        fun onAgentSelected(agentResponse: AgentModel)
        fun onAgentDeleted(name: String, position: Int)
    }

}

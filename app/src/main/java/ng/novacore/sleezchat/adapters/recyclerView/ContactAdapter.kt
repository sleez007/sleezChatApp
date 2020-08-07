package ng.novacore.sleezchat.adapters.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ng.novacore.sleezchat.databinding.RowContactBinding
import ng.novacore.sleezchat.databinding.RowInviteBinding
import ng.novacore.sleezchat.db.entity.UsersEntity
import ng.novacore.sleezchat.internals.interfaces.BindableAdapter

const val USER_VIEW_TYPE  = 0
const val INVITE_VIEW_TYPE = 1
class ContactAdapter : PagedListAdapter<UsersEntity, RecyclerView.ViewHolder >(ContactDiffCbUtil()),
    BindableAdapter<PagedList<UsersEntity>> {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            USER_VIEW_TYPE ->AppUserVh.from(parent)
            else->InviteUserVh.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when(holder){
                is AppUserVh -> holder.bind(getItem(position))
                is InviteUserVh -> holder.bind(getItem(position))
            }
    }

    override fun getItemViewType(position: Int): Int {
        return if(getItem(position)?.linked == true  ) USER_VIEW_TYPE else INVITE_VIEW_TYPE
    }

    override fun setData(data: PagedList<UsersEntity>)= submitList(data)
}

class AppUserVh(val binding : RowContactBinding): RecyclerView.ViewHolder(binding.root){
    companion object{
        fun from(parent : ViewGroup): AppUserVh{
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = RowContactBinding.inflate(layoutInflater,parent,false)
            return AppUserVh(binding)
        }
    }

    fun bind(user : UsersEntity?){
        user?.let {
            binding.user = it
        }
        binding.executePendingBindings()
    }
}

class InviteUserVh(val binding : RowInviteBinding): RecyclerView.ViewHolder(binding.root){
    companion object{
        fun from(parent : ViewGroup): InviteUserVh{
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = RowInviteBinding.inflate(layoutInflater,parent,false)
            return InviteUserVh(binding)
        }
    }

    fun bind(user : UsersEntity?){
        user?.let {
            binding.user = it
        }
        binding.executePendingBindings()
    }
}

class ContactDiffCbUtil : DiffUtil.ItemCallback<UsersEntity>(){
    override fun areItemsTheSame(oldItem: UsersEntity, newItem: UsersEntity): Boolean  = oldItem._id == newItem._id

    override fun areContentsTheSame(oldItem: UsersEntity, newItem: UsersEntity): Boolean = oldItem == newItem

}
package ng.novacore.sleezchat.adapters.recyclerView

import android.content.Intent
import android.net.Uri
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
import ng.novacore.sleezchat.internals.interfaces.ClickContract
import ng.novacore.sleezchat.views.RecyclerViewFastScroller
import org.json.HTTP
import java.lang.Exception
import java.net.URI

const val USER_VIEW_TYPE = 0
const val INVITE_VIEW_TYPE = 1

class ContactAdapter(val vm: ClickContract<UsersEntity>) :
    PagedListAdapter<UsersEntity, RecyclerView.ViewHolder>(ContactDiffCbUtil()),
    BindableAdapter<PagedList<UsersEntity>>, RecyclerViewFastScroller.BubbleTextGetter {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            USER_VIEW_TYPE -> AppUserVh.from(parent)
            else -> InviteUserVh.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AppUserVh -> holder.bind(getItem(position), vm)
            is InviteUserVh -> holder.bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position)?.linked == true) USER_VIEW_TYPE else INVITE_VIEW_TYPE
    }

    override fun setData(data: PagedList<UsersEntity>) = submitList(data)
    override fun getTextToShowInBubble(position: Int): String {
        return try {
            if (itemCount > position) {
                val data = getItem(position)
                data?.contactName?.substring(0, 1) ?: ""
            } else {
                ""
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
            ""
        }

    }
}

class AppUserVh(val binding: RowContactBinding) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): AppUserVh {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = RowContactBinding.inflate(layoutInflater, parent, false)
            return AppUserVh(binding)
        }
    }

    fun bind(user: UsersEntity?, vm: ClickContract<UsersEntity>) {
        user?.let {
            binding.vm = vm
            binding.user = it
        }
        binding.executePendingBindings()
    }
}

class InviteUserVh(val binding: RowInviteBinding) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): InviteUserVh {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = RowInviteBinding.inflate(layoutInflater, parent, false)
            return InviteUserVh(binding)
        }
    }

    fun bind(user: UsersEntity?) {
        user?.let {
            binding.user = it
            binding.inviteBtn.setOnClickListener { x ->
                val intent =
                    Intent(Intent.ACTION_SENDTO).setData(Uri.parse("smsto:${it.phoneNumber}"))
                        .putExtra(
                            "sms_body",
                            "Kindly download nova chat on google play. Thank me later"
                        )
                if (intent.resolveActivity(x.context.packageManager) != null) x.context.startActivity(
                    intent
                )

            }

        }
        binding.executePendingBindings()
    }
}

class ContactDiffCbUtil : DiffUtil.ItemCallback<UsersEntity>() {
    override fun areItemsTheSame(oldItem: UsersEntity, newItem: UsersEntity): Boolean =
        oldItem._id == newItem._id

    override fun areContentsTheSame(oldItem: UsersEntity, newItem: UsersEntity): Boolean =
        oldItem == newItem

}
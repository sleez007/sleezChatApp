package ng.novacore.sleezchat.adapters.viewPager

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ng.novacore.sleezchat.R
import ng.novacore.sleezchat.databinding.ItemWizardBinding
import ng.novacore.sleezchat.model.IntroModel

class WizardAdapter(context: Context): RecyclerView.Adapter<WizardVh>() {
    val data = listOf<IntroModel>(
        IntroModel(context.getString(R.string.connect_with),context.getString(R.string.connect_with_desc),R.drawable.prev1 ),
        IntroModel(context.getString(R.string.find_friends),context.getString(R.string.find_friends_desc),R.drawable.prev2 ),
        IntroModel(context.getString(R.string.get_started),context.getString(R.string.get_start_desc),R.drawable.prev3 )
    )
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WizardVh = WizardVh.from(parent)

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: WizardVh, position: Int) =holder.bind(data[position])
}

class WizardVh(val binding:ItemWizardBinding):  RecyclerView.ViewHolder(binding.root){
    companion object{
        fun from(parent : ViewGroup)  : WizardVh {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemWizardBinding.inflate(layoutInflater, parent, false)
            return WizardVh(binding)
        }
    }

    fun bind(item : IntroModel){
        binding.item = item
        binding.executePendingBindings()
    }
}
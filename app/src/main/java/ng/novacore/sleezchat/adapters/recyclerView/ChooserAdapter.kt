package ng.novacore.sleezchat.adapters.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ng.novacore.sleezchat.R
import ng.novacore.sleezchat.activities.verification.VerificationViewModel
import ng.novacore.sleezchat.databinding.ItemImageOptionsBinding
import ng.novacore.sleezchat.internals.enums.OpenMedia
import ng.novacore.sleezchat.model.ImageChooserModel
import ng.novacore.sleezchat.utils.Event

class ChooserAdapter(private val mItemCount: Int, val  viewModel: VerificationViewModel,val sheet: BottomSheetDialogFragment) : RecyclerView.Adapter<ChooserHolder>() {
    val optionsList = listOf<ImageChooserModel>(
        ImageChooserModel(R.drawable.gallery, R.string.gallery),
        ImageChooserModel(R.drawable.camera,R.string.camera),
        ImageChooserModel(R.drawable.cancel, R.string.remove)
    )
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooserHolder = ChooserHolder.from(parent, sheet)
    override fun onBindViewHolder(holder: ChooserHolder, position: Int)= holder.bind(position, optionsList[position],viewModel)

    override fun getItemCount(): Int = mItemCount
}

class ChooserHolder(val binding : ItemImageOptionsBinding,val sheet: BottomSheetDialogFragment) : RecyclerView.ViewHolder(binding.root) {
    companion object{
        fun from(parent: ViewGroup, sheet: BottomSheetDialogFragment ): ChooserHolder{
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemImageOptionsBinding.inflate(layoutInflater, parent,false)
            return ChooserHolder(binding,sheet)
        }
    }

    fun bind(position: Int, data: ImageChooserModel, viewModel: VerificationViewModel){
        binding.avatar.setImageDrawable(ContextCompat.getDrawable(binding.avatar.context,data.imgRes))
        binding.title.text = binding.title.context.getString(data.title)
        binding.root.setOnClickListener {
            when(position){
                0->{
                    viewModel.openMedia.value = Event(OpenMedia.OPEN_GALLERY)
                }
                1->{
                    viewModel.openMedia.value = Event(OpenMedia.OPEN_CAMERA)
                }
                2->{
                    viewModel.openMedia.value = Event(OpenMedia.REMOVE_SELECTED)
                }
            }
            sheet.dismiss()

        }
        binding.executePendingBindings()
    }
}
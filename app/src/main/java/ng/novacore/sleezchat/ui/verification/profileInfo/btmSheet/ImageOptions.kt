package ng.novacore.sleezchat.ui.verification.profileInfo.btmSheet

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dagger.hilt.android.AndroidEntryPoint
import ng.novacore.sleezchat.R
import kotlinx.android.synthetic.main.fragment_image_options_chooser_btm_sheet.*
import kotlinx.android.synthetic.main.item_image_options.view.*

@AndroidEntryPoint
class ImageOptions : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image_options_chooser_btm_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        list.layoutManager = GridLayoutManager(context, 3)
        list.adapter =  ItemAdapter(3)
    }

    private inner class ViewHolder internal constructor(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) : RecyclerView.ViewHolder(
        inflater.inflate(
            R.layout.item_image_options,
            parent,
            false
        )
    ) {

        internal val text: TextView = itemView.text
    }

    private inner class ItemAdapter internal constructor(private val mItemCount: Int) :
        RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
           // holder.text.text = position.toString()
            holder.text.text = if(position % 2 == 0) "Camera" else "Gallery"
        }

        override fun getItemCount(): Int {
            return mItemCount
        }
    }

}

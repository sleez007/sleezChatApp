package ng.novacore.sleezchat.bindings

import androidx.databinding.BindingAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ng.novacore.sleezchat.R

@BindingAdapter("setFabIcon")
fun FloatingActionButton.setFabIcon(page: Int){
    when(page){
        0->{
            hide()
        }
        1->{
            setImageResource(R.drawable.ic_chat_black_24dp)
            show()
        }
        2->{
            setImageResource(R.drawable.ic_camera_alt_black_24dp)
            show()
        }
        3->{
            setImageResource(R.drawable.ic_phone_black_24dp)
            show()
        }
    }
}
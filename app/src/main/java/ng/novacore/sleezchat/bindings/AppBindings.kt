package ng.novacore.sleezchat.bindings

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ng.novacore.sleezchat.R
import ng.novacore.sleezchat.internals.interfaces.BindableAdapter
import ng.novacore.sleezchat.internals.interfaces.RetryRequest
import timber.log.Timber


//SWIPE REFRESH LAYOUTS
@BindingAdapter("refreshListener")
fun SwipeRefreshLayout.refreshListener(vm: RetryRequest){
    setOnRefreshListener { vm.retry() }
}

@BindingAdapter("shouldRefresh")
fun SwipeRefreshLayout.shouldRefresh(shouldRefresh : Boolean){
    isRefreshing = shouldRefresh
}

@BindingAdapter("data")
fun <T> RecyclerView.setRecyclerViewProperties(data: T) {
    Timber.i("AppB")
    Timber.i(data.toString())
    if (adapter is BindableAdapter<*>) {
        data?.let {
            (adapter as BindableAdapter<T>).setData(it)
        }
    }
}

@BindingAdapter("provideImg")
fun<T> ImageView.setImageSource(image : T ){
    when(image){
        is Int ->{
            this.setImageResource(image)
        }
        is String ->{
            try {
                val finalPath = image
                val options = RequestOptions()
                //Glide.with(context).load(finalPath).apply(options.placeholder(R.drawable.loading_placeholder).error(R.drawable.loading_placeholder)).into(this)
            }catch (ex: Exception){
                println(ex.message)
            }

        }
    }
}

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
package ng.novacore.sleezchat.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ng.novacore.sleezchat.R

data class ImageChooserModel(
    @DrawableRes
    val  imgRes : Int= R.drawable.ic_camera_alt_black_24dp,
    @StringRes
    val title : Int
)
package ng.novacore.sleezchat.helper.camera

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ng.novacore.sleezchat.R
import ng.novacore.sleezchat.utils.Constants
import java.io.IOException
import javax.inject.Inject

class GalleryPicker @Inject constructor() : ImageManager() {

    fun sendToExternalApp(fragment: Fragment) {
        val intent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        fragment.startActivityForResult(
            Intent.createChooser(intent, fragment.getString(R.string.select_picture)),
            Constants.REQUEST_IMAGE_FROM_GALLERY
        )
    }


    companion object{
        var currentPhotoPath : String? = null
        @Throws(IOException::class)
        suspend fun loadImageFromUri( photoUri: Uri, activity: Activity):Bitmap? {
            return withContext(Dispatchers.IO) {
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor: Cursor? =
                    activity.getContentResolver().query(photoUri, filePathColumn, null, null, null)
                if (cursor != null) {
                    cursor.moveToFirst()
                    val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
                    val picturePath: String = cursor.getString(columnIndex)
                    currentPhotoPath = picturePath
                    cursor.close()
                    BitmapFactory.decodeFile(picturePath)
                } else {
                    null
                }

            }
        }
    }
}
package ng.novacore.sleezchat.helper.camera

import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import kotlinx.coroutines.*
import ng.novacore.sleezchat.BuildConfig
import ng.novacore.sleezchat.R
import ng.novacore.sleezchat.utils.Constants
import timber.log.Timber
import java.io.File
import java.io.IOException
import javax.inject.Inject

class CameraPicker @Inject constructor(): ImageManager() {
    var currentPhotoPath : String? = null

    fun sendToExternalApp(fragment: Fragment ){
        CoroutineScope(Dispatchers.Main).launch {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(fragment.requireActivity().packageManager)?.also {
                    //Create the file where the photo should be stored
                    val photoFile :File?= try {
                        createImageFile(fragment,"profile_image.jpg")
                    } catch (ex: IOException) {
                        null
                    }
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            fragment.requireContext(),
                            "${BuildConfig.APPLICATION_ID}.fileprovider",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        fragment.startActivityForResult(takePictureIntent, Constants.REQUEST_IMAGE_FROM_CAMERA)
                    }

                }
            }
        }

    }

    @Throws(IOException::class)
   suspend fun createImageFile(fragment: Fragment, fileName: String): File{
        return withContext(Dispatchers.IO){
            val pathString = fragment.requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val mediaStorageDir = File(pathString,fragment.getString(R.string.app_name))
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Timber.i( "failed to create directory")
            }
             File(mediaStorageDir.path + File.separator + fileName).apply {
                 currentPhotoPath = absolutePath
             }
        }

        //return File(mediaStorageDir.path + File.separator + fileName)

    }




}
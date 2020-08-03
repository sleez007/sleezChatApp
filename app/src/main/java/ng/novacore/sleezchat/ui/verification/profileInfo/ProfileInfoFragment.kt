package ng.novacore.sleezchat.ui.verification.profileInfo

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ng.novacore.sleezchat.R
import ng.novacore.sleezchat.activities.verification.VerificationActivity
import ng.novacore.sleezchat.activities.verification.VerificationViewModel
import ng.novacore.sleezchat.databinding.FragmentProfileInfoBinding
import ng.novacore.sleezchat.helper.camera.CameraPicker
import ng.novacore.sleezchat.helper.camera.GalleryPicker
import ng.novacore.sleezchat.internals.enums.OpenMedia
import ng.novacore.sleezchat.managers.MyWorkManager
import ng.novacore.sleezchat.utils.Constants
import javax.inject.Inject


@AndroidEntryPoint
class ProfileInfoFragment : Fragment() {
    @Inject lateinit var galleryPicker: GalleryPicker
    @Inject lateinit var cameraPicker: CameraPicker

    lateinit var viewModel: VerificationViewModel

    private val needed =arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.WRITE_CONTACTS
    )
    var binding: FragmentProfileInfoBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileInfoBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = (activity as VerificationActivity).viewModel
        binding?.apply {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
            fab.setOnClickListener {
                requestPermissions()
            }
        }
        initializeBackgroundJobs()
        viewModel.openMedia.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let {
                when(it){
                    OpenMedia.OPEN_GALLERY->{
                        galleryPicker.sendToExternalApp(this)
                    }
                    OpenMedia.OPEN_CAMERA->{
                        cameraPicker.sendToExternalApp(this)
                    }
                    OpenMedia.REMOVE_SELECTED->{
                        binding?.imageView?.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.profile))
                    }
                }
            }
        })
        viewModel.bitMap.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let {
                //Glide.with(requireContext()).load(it).into(binding!!.imageView)
                binding?.imageView?.setImageBitmap(it)
            }
        })
        viewModel.currentFrag.observe(viewLifecycleOwner, Observer {

            it?.getContentIfNotHandled()?.let {
                if(it == 3){
                    viewModel.updateProfile()
                }
            }
        })

    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun requestPermissions() {
        when {
            checkIfAllIsGranted()-> {
                // You can use the API that requires the permission.
                openModal(3)

            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected. In this UI,
                // include a "cancel" or "no thanks" button that allows the user to
                // continue using your app without granting the permission.
                //showInContextUI(...)
                // Todo("Build educational UI")
            }
            shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)->{
                //DO THIS FOR THE REMAINIG PERMISSIONS LATER
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                // You can directly ask for the permission.
                requestPermissions(
                    needed,
                    Constants.GRANT_MULTIPLE_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun checkIfAllIsGranted():Boolean{
        var count = 0
        needed.forEach {
           if (ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED) ++count
        }
        return count == needed.size
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            Constants.GRANT_MULTIPLE_PERMISSION_REQUEST_CODE ->{
                if(grantResults.isNotEmpty() && checkIfAllIsGranted()){
                    //openModal
                    //todo write a logic to determine what to pass as an option
                    openModal(3)
                }
            }
        }
    }

    /**
     * @param itemCount determines the number of items to be displayed on the bottomsheet if all permissions
     * were granted i.e an extra item to delete image selected
     */
    private fun openModal(itemCount : Int){
        val action = ProfileInfoFragmentDirections.actionNavProfileInfoToNavImgOptionBtmSheet(itemCount)
        findNavController().navigate(action)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_IMAGE_FROM_CAMERA && resultCode == RESULT_OK) {
            binding?.let {bd->
                cameraPicker.currentPhotoPath?.let {
                    viewModel.decodeImageOnBgThread(it,bd.imageView.width, bd.imageView.height)
                }
            }
        }
        else if(requestCode == Constants.REQUEST_IMAGE_FROM_GALLERY && resultCode == RESULT_OK && data!=null){
            val photoUri: Uri? = data.data
            photoUri?.let {
                viewModel.decodeGalleryUriToBitmap(it,requireActivity())
            }
        }
    }


    private fun initializeBackgroundJobs(){
        MyWorkManager.initializeAppWorkers(requireContext().applicationContext)
        MyWorkManager.synchronizeContactsWithServerAsap(requireContext().applicationContext)
    }


}

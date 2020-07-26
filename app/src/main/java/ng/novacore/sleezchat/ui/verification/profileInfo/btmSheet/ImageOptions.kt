package ng.novacore.sleezchat.ui.verification.profileInfo.btmSheet

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ng.novacore.sleezchat.activities.verification.VerificationActivity
import ng.novacore.sleezchat.activities.verification.VerificationViewModel
import ng.novacore.sleezchat.adapters.recyclerView.ChooserAdapter
import ng.novacore.sleezchat.databinding.FragmentImageOptionsChooserBtmSheetBinding

@AndroidEntryPoint
class ImageOptions : BottomSheetDialogFragment() {
    lateinit var viewModel: VerificationViewModel
    val args : ImageOptionsArgs by navArgs()
    var binding : FragmentImageOptionsChooserBtmSheetBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentImageOptionsChooserBtmSheetBinding.inflate(inflater, container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = (activity as VerificationActivity).viewModel
        binding?.list?.layoutManager = GridLayoutManager(context, args.itemCount)
        binding?.list?.adapter =  ChooserAdapter(args.itemCount,viewModel, this)
    }

    override fun onDestroyView() {
        binding?.list?.adapter = null
        binding = null
        super.onDestroyView()
    }
}

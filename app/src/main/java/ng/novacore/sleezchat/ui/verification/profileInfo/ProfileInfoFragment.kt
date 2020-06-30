package ng.novacore.sleezchat.ui.verification.profileInfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ng.novacore.sleezchat.databinding.FragmentProfileInfoBinding

@AndroidEntryPoint
class ProfileInfoFragment : Fragment() {

    var binding : FragmentProfileInfoBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileInfoBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            linearLayout.setOnClickListener {
                val action = ProfileInfoFragmentDirections.actionNavProfileInfoToNavImgOptionBtmSheet(2)
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

}

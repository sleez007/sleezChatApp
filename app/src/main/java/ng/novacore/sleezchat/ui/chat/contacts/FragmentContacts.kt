package ng.novacore.sleezchat.ui.chat.contacts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ng.novacore.sleezchat.R
import ng.novacore.sleezchat.adapters.recyclerView.ContactAdapter
import ng.novacore.sleezchat.databinding.FragmentContactsBinding
import ng.novacore.sleezchat.utils.RecyclerViewMargin

@AndroidEntryPoint
class FragmentContacts : Fragment() {

    var binding : FragmentContactsBinding? = null

    val viewModel : FragmentContactsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactsBinding.inflate(inflater, container, false)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        eventListeners()
        wireAdapter()

    }

    private fun eventListeners(){

    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    fun wireAdapter(){
        val adapter = ContactAdapter()
        binding?.recyclerView?.addItemDecoration(RecyclerViewMargin(16,1))
        binding?.recyclerView?.adapter = adapter
    }

}
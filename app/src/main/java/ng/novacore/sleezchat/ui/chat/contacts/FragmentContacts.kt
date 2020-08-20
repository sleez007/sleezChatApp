package ng.novacore.sleezchat.ui.chat.contacts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ng.novacore.sleezchat.R
import ng.novacore.sleezchat.adapters.recyclerView.ContactAdapter
import ng.novacore.sleezchat.databinding.FragmentContactsBinding
import ng.novacore.sleezchat.utils.BaseFrag
import ng.novacore.sleezchat.utils.RecyclerViewMargin

@AndroidEntryPoint
class FragmentContacts : BaseFrag() {

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

    override fun provideToolbar(): Toolbar? = binding?.toolbar

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        eventListeners()
        wireAdapter()
    }

    private fun eventListeners(){
        viewModel.selectedChat.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let {
                FragmentContactsDirections.actionGlobalNavMsg().apply {
                    findNavController().navigate(this)
                }
            }
        })
    }

    override fun onDestroyView() {
        binding?.recyclerView?.adapter = null
        binding = null
        super.onDestroyView()
    }

    fun wireAdapter(){
        val adapter = ContactAdapter(viewModel)
        binding?.recyclerView?.addItemDecoration(RecyclerViewMargin(16,1))
        binding?.recyclerView?.adapter = adapter
        binding?.fastscroll?.setRecyclerView(binding!!.recyclerView)
        binding?.fastscroll?.setViewsToUse(R.layout.fast_scroll_bubble, R.id.fastscroller_bubble,  R.id.fastscroller_handle)
    }

}
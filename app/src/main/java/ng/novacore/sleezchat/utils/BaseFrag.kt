package ng.novacore.sleezchat.utils

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import ng.novacore.sleezchat.R
import ng.novacore.sleezchat.activities.MainActivity

abstract class BaseFrag: Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        provideToolbar()?.setupWithNavController(navController,appBarConfiguration)
    }

    protected abstract fun provideToolbar() : Toolbar?
}
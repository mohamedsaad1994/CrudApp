package com.app.crudapp.core.views.progressDialog

import android.app.AlertDialog
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.app.crudapp.R
import timber.log.Timber

class ProgressDialogImpl : ProgressDialogHandler, LifecycleEventObserver {

    private var dialog: AlertDialog? = null

    override fun initDialogWithLifecycleOwner(context: Context) {
        (context as LifecycleOwner).lifecycle.addObserver(this)
        dialog = AlertDialog.Builder(context)
            .setView(R.layout.progress)
            .setCancelable(false)
            .create()
    }

    override fun changeDialogVisibility(isVisible: Boolean) {
        Timber.tag("toggleLoadingDialog").d(isVisible.toString())
        if (isVisible) {
            dialog?.show()
            dialog?.window?.setDimAmount(0.5f)
            dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        } else {
            dialog?.dismiss()
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_DESTROY -> {
                changeDialogVisibility(false)
            }

            else -> Unit
        }
    }
}

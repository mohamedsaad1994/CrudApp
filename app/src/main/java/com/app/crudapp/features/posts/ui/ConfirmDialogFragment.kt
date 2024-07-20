package com.app.crudapp.features.posts.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.app.crudapp.R

class ConfirmDialogFragment(
    private val onConfirm: (isConfirmed: Boolean) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(getString(R.string.are_you_sure_to_delete))
            .setPositiveButton(
                getString(R.string.confirm)
            ) { dialogInterface, _ ->
                dialogInterface.dismiss()
                onConfirm.invoke(true)
            }
            .setNegativeButton(
                getString(R.string.cancel)
            ) { dialogInterface, _ ->
                dialogInterface.dismiss()
                onConfirm.invoke(false)
            }

        builder.create()
        return builder.create()
    }
    companion object {
        const val TAG = "ConfirmDialogFragment"
    }
}

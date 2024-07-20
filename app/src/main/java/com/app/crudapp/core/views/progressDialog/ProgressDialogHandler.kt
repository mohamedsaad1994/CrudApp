package com.app.crudapp.core.views.progressDialog

import android.content.Context

interface ProgressDialogHandler {
    fun initDialogWithLifecycleOwner(context:Context)
    fun changeDialogVisibility(isVisible: Boolean)
}

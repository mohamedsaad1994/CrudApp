package com.app.crudapp.core.networkExeption

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CodeTextDataModel(
    @SerializedName("code_text")
    val codeText:String
)

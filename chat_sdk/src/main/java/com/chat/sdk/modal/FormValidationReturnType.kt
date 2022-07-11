package com.chat.sdk.modal


internal data class FormValidationReturnType(val param: ArrayList<FormValidationSubmitType>, val valid:Boolean)

internal data class FormValidationSubmitType(val key: String, val value:String)  {

}
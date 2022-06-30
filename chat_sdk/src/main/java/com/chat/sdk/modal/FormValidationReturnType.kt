package com.chat.sdk.modal


data class FormValidationReturnType(val param: ArrayList<FormValidationSubmitType>, val valid:Boolean)

data class FormValidationSubmitType(val key: String, val value:String)  {

}
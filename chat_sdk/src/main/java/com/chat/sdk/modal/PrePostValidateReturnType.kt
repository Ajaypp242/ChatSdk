package com.chat.sdk.modal


data class PrePostValidateReturnType(val param: ArrayList<PrePostValidateSubmitType>, val valid:Boolean)

data class PrePostValidateSubmitType(val key: String, val value:String)  {

}
package com.chat.sdk.network

import com.chat.sdk.modal.*
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*

internal interface ApiClient {
    @FormUrlEncoded
    @POST("getchatsettings")
    suspend fun getData(
        @Field("site_id") site_id: String,
        @Field("proprofs_language_id") proprofs_language_id: String,
        @Field("ProProfs_Session") ProProfs_Session: String?,
        @Field("ProProfs_Current_URL") ProProfs_Current_URL: String,
        @Field("ProProfs_refferal_URL") ProProfs_refferal_URL: String,
        @Field("ProProfs_device_id") ProProfs_device_id: String,
        @Field("ProProfs_Chat_Token") ProProfs_Chat_Token: String,
        @Field("ProProfs_Chat_Email") ProProfs_Chat_Email: String,
        @Field("ProProfs_Chat_l2s_cv") ProProfs_Chat_l2s_cv: String,
        @Field("AccountCode") AccountCode: String,
        @Field("ProProfsGroupIdHardCoded") ProProfsGroupIdHardCoded: String,
        @Field("_ProProfs_custom_langauge_for_bot") _ProProfs_custom_langauge_for_bot: String,
    ): Response<ChatSettingData>

    @FormUrlEncoded
    @POST("prechat")
    suspend fun preChat(
        @Field("ProProfs_session") ProProfs_session: String,
        @Field("ProProfs_language_id") ProProfs_language_id: String?,
        @Field("ProProfs_site_id") ProProfs_site_id: String,
        @Field("pp_operator_label") pp_operator_label: String,
        @Field("pp_group_label") pp_group_label: String,
        @Field("pp_department_label") pp_department_label: String,
        @Field("pp_visitor_email") pp_visitor_email: String,
        @Field("pp_visitor_name") pp_visitor_name: String,
        @Field("ProProfs_field_counter") ProProfs_field_counter: String?,
        @Field("ProProfs_device_id") ProProfs_device_id: String?,
        @Field("AccountCode") AccountCode: String,
        @Field("DepartmentRouting") DepartmentRouting: String,
        @Field("ProProfs_Chat_l2s_cv") ProProfs_Chat_l2s_cv: String,
        @Field("ProProfs_Current_URL_manual") ProProfs_Current_URL_manual: String,
        @FieldMap others_string: HashMap<String, String>,
        @FieldMap others_array: HashMap<String, ArrayList<String>>
    ): Response<PreChatResponse>

    @FormUrlEncoded
    @POST("chat")
    suspend fun getChat(
        @Field("site_id") site_id: String?,
        @Field("proprofs_language_id") proprofs_language_id: String?,
        @Field("ProProfs_Session") ProProfs_Session: String?,
        @Field("ProProfs_Msg_Counter") ProProfs_Msg_Counter: String,
        @Field("ProProfs_Visitor_TimeZone") ProProfs_Visitor_TimeZone: String,
        @Field("ProProfs_invitation_type") ProProfs_invitation_type: String,
        @Field("ProProfs_Current_URL") ProProfs_Current_URL: String,
        @Field("ProProfsGroupIdHardCoded") ProProfsGroupIdHardCoded: String,
        @Field("ProProfs_Visitor_name") ProProfs_Visitor_name: String?,
        @Field("ProProfs_Visitor_email") ProProfs_Visitor_email: String?,
        @Field("ProProfs_typing_message") ProProfs_typing_message: String
    ): Response<ChatData>

    @FormUrlEncoded
    @POST("offlinemessage")
    suspend fun sendOfflineMessage(
        @Field("pp_time_tracker_status") pp_time_tracker_status: String,
        @Field("off_ProProfs_session") off_ProProfs_session: String?,
        @Field("off_ProProfs_language_id") off_ProProfs_language_id: String?,
        @Field("off_ProProfs_site_id") off_ProProfs_site_id: String,
        @Field("pp_department_offline_label") pp_department_offline_label: String,
        @Field("off_pp_visitor_name") off_pp_visitor_name: String,
        @Field("off_pp_visitor_email") off_pp_visitor_email: String,
        @Field("off_ProProfs_field_counter") off_ProProfs_field_counter: String,
        @Field("ProProfs_device_id") ProProfs_device_id: String,
        @Field("AccountCode") AccountCode: String,
        @Field("DepartmentRouting1") DepartmentRouting1: String,
        @Field("ProProfs_Current_URL_manual") ProProfs_Current_URL_manual: String,
    ): Response<JSONObject>

    @FormUrlEncoded
    @POST("send_visitor_message")
    suspend fun sendVisitorMessage(
        @Field("ProProfs_Session") ProProfs_Session: String?,
        @Field("ProProfs_Message") ProProfs_Message: String,
    ): Response<VisitorMessageResponse>

    @FormUrlEncoded
    @POST("genrate_transcript")
    suspend fun generateTranscript(
        @Field("ProProfs_Session") ProProfs_Session: String?,
        @Field("ProProfs_site_id") ProProfs_site_id: String?,
        @Field("ProProfs_closed_by") ProProfs_closed_by: String?,
    ): Response<String>

    @FormUrlEncoded
    @POST("submitrating")
    suspend fun submitRating(
        @Field("Site_id") Site_id: String,
        @Field("ProProfs_Session") ProProfs_Session: String,
        @Field("ProProfs_rating") ProProfs_rating: Int
    ): Response<Any>

    @FormUrlEncoded
    @POST("postchat")
    suspend fun postChat(
        @Field("proprofs_transcript") proprofs_transcript: Int,
        @Field("post_ProProfs_rating") post_ProProfs_rating: Int,
        @Field("post_ProProfs_session") post_ProProfs_session: String,
        @Field("post_ProProfs_language_id") post_ProProfs_language_id: String,
        @Field("post_ProProfs_site_id") post_ProProfs_site_id: String,
        @FieldMap others_string: HashMap<String, String>,
        @FieldMap others_array: HashMap<String, ArrayList<String>>,
        @Field("post_ProProfs_field_counter") post_ProProfs_field_counter: String,
        @Field("AccountCode") AccountCode: String
    ): Response<Any>

    @FormUrlEncoded
    @POST("updatemessagestatus")
    suspend fun updateMessageStatus(
        @Field("ProProfs_Session") ProProfs_Session: String
    )

    @FormUrlEncoded
    @POST("uploadimage")
    suspend fun uploadImage(
        @Field("pp_img_counter") pp_img_counter: String,
        @Field("session_id_image") session_id_image: String,
        @Field("site_id") site_id: String,
        @Field("from") from: String,
        @Field("sdk_image_url") sdk_image_url:String,
        ): Response<ImageUploadResponse>
}



package com.example.pruebaqinaya

import com.google.gson.annotations.SerializedName

data class userResponse(
    @SerializedName( "access_token")
    val accessToken: String?=null,

    @SerializedName("token_type")
    val tokenType: String?=null,

    @SerializedName( "have_trial")
    val haveTrial: Boolean?=null,

    val user: User
)
data class User (
    val id: Long,
    val name: String,
    val email: String,
    val phone: String,

    @SerializedName("ad_username")
    val adUsername: Any? = null,

    @SerializedName("ad_password")
    val adPassword: Any? = null,

    @SerializedName( "country_id")
    val countryID: String?=null,

    @SerializedName( "currency_id")
    val currencyID: String?=null,

    @SerializedName( "referrer_id")
    val referrerID: Any? = null,

    @SerializedName( "current_team_id")
    val currentTeamID: Any? = null,

    @SerializedName( "current_organization_id")
    val currentOrganizationID: Any? = null,

    @SerializedName( "email_subscription")
    val emailSubscription: String?=null,

    @SerializedName("email_verified_at")
    val emailVerifiedAt: String?=null,

    @SerializedName( "profile_photo_path")
    val profilePhotoPath: Any? = null,

    @SerializedName("computer_counter")
    val computerCounter: String?=null,

    @SerializedName( "start_trial")
    val startTrial: String?=null,

    val oldUser: Any? = null,

    @SerializedName( "created_at")
    val createdAt: String?=null,

    @SerializedName( "updated_at")
    val updatedAt: String?=null,

    @SerializedName("referral_link")
    val referralLink: String?=null,

    @SerializedName( "profile_photo_url")
    val profilePhotoURL: String?=null
)


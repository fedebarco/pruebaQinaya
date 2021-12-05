package com.example.pruebaqinaya

import com.google.gson.annotations.SerializedName

data class userResponse(
    @SerializedName( "access_token")
    val accessToken: String,

    @SerializedName("token_type")
    val tokenType: String,

    @SerializedName( "have_trial")
    val haveTrial: Boolean,

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
    val countryID: String,

    @SerializedName( "currency_id")
    val currencyID: String,

    @SerializedName( "referrer_id")
    val referrerID: Any? = null,

    @SerializedName( "current_team_id")
    val currentTeamID: Any? = null,

    @SerializedName( "current_organization_id")
    val currentOrganizationID: Any? = null,

    @SerializedName( "email_subscription")
    val emailSubscription: String,

    @SerializedName("email_verified_at")
    val emailVerifiedAt: String,

    @SerializedName( "profile_photo_path")
    val profilePhotoPath: Any? = null,

    @SerializedName("computer_counter")
    val computerCounter: String,

    @SerializedName( "start_trial")
    val startTrial: String,

    val oldUser: Any? = null,

    @SerializedName( "created_at")
    val createdAt: String,

    @SerializedName( "updated_at")
    val updatedAt: String,

    @SerializedName("referral_link")
    val referralLink: String,

    @SerializedName( "profile_photo_url")
    val profilePhotoURL: String
)


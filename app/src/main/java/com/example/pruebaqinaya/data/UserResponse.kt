package com.example.pruebaqinaya.data

import com.squareup.moshi.Json

data class UserResponse(
    @Json( name = "access_token")
    val accessToken: String?=null,

    @Json(name = "token_type")
    val tokenType: String?=null,

    @Json( name = "have_trial")
    val haveTrial: Boolean?=null,

    val user: User
)
data class User (
    val id: Long,
    val name: String,
    val email: String,
    val phone: String,

    @Json(name = "ad_username")
    val adUsername: Any? = null,

    @Json(name = "ad_password")
    val adPassword: Any? = null,

    @Json( name = "country_id")
    val countryID: String?=null,

    @Json( name = "currency_id")
    val currencyID: String?=null,

    @Json( name = "referrer_id")
    val referrerID: Any? = null,

    @Json( name = "current_team_id")
    val currentTeamID: Any? = null,

    @Json( name = "current_organization_id")
    val currentOrganizationID: Any? = null,

    @Json( name="email_subscription")
    val emailSubscription: String?=null,

    @Json(name = "email_verified_at")
    val emailVerifiedAt: String?=null,

    @Json( name = "profile_photo_path")
    val profilePhotoPath: Any? = null,

    @Json(name = "computer_counter")
    val computerCounter: String?=null,

    @Json( name = "start_trial")
    val startTrial: String?=null,

    val oldUser: Any? = null,

    @Json( name = "created_at")
    val createdAt: String?=null,

    @Json( name = "updated_at")
    val updatedAt: String?=null,

    @Json(name = "referral_link")
    val referralLink: String?=null,

    @Json( name = "profile_photo_url")
    val profilePhotoURL: String?=null
)


package com.example.vladislav.android5

//class User {
//    private var name: String? = null
//    private var last_name: String? = null
//    private var email: String? = null
//    private var phone: String? = null
//    private var image_uri: String? = null
//}

data class User(
        var username: String? = "",
        var last_name: String? = "",
        var email: String? = "",
        var phone: String? = "",
        var image_uri: String? = ""
)

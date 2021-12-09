package com.android.iotproject.data

import android.content.Context
import android.util.Log
import com.android.iotproject.R
import com.android.iotproject.data.model.LoggedInUser
import com.android.volley.Request
import com.android.volley.Response

interface VolleyResponse {
    fun processFinish(output: String?)
}

class LoginRepository(val context: Context) {
    var user: LoggedInUser? = null

    val isLoggedIn: Boolean
        get() = user != null

    init {
        user = null
    }

    fun logout() {
        user = null
    }

    fun login(
        username: String,
        password: String,
        responseOK: VolleyResponse,
        responseERROR: VolleyResponse
    ) {
        // handle login
        val url = context.getString(R.string.login_url)
        val params: MutableMap<String, String> = HashMap()
        params["username"] = username
        params["password"] = password
        val response = Response.Listener<String> {
            Log.i("Data Source", it.toString())
            user = LoggedInUser(it.toString(), "")
            responseOK.processFinish(it)
        }
        val error = Response.ErrorListener {
            Log.i("Data Source", it.toString())
            responseERROR.processFinish(it.toString())
        }
        val req = CustomRequest(
            Request.Method.POST, url,
            CustomRequest.APPLICATION_URL_ENCODED, params, response, error
        )
        ResquestInstance.getInstance(context).addToRequestQueue(req)
    }

}
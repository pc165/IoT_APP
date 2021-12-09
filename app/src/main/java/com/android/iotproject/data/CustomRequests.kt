package com.android.iotproject.data

import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest


class CustomRequest(
    method: Int,
    url: String,
    contentType: String,
    params: MutableMap<String, String>,
    listener: Response.Listener<String>,
    errorListener: Response.ErrorListener
) : StringRequest(method, url, listener, errorListener) {
    companion object {
        const val CONTENT_APP: String = "application/x-www-form-urlencoded; charset=UTF-8"
    }

    private var mMethod: Int = method
    private var mUrl: String = url
    private var mContentType: String = contentType
    private var mParams: MutableMap<String, String> = params

    override fun getBodyContentType(): String {
        return mContentType
    }

    @Throws(AuthFailureError::class)
    override fun getParams(): Map<String, String> {
        return mParams
    }
}

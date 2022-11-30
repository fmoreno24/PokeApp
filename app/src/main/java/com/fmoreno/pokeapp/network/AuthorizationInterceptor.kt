package com.fmoreno.pokeapp.network

import com.fmoreno.pokeapp.ui.base.BaseEvents
import com.fmoreno.pokeapp.ui.base.BaseObservableViewModel
import com.fmoreno.pokeapp.ui.base.NetworkEvents
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.SocketTimeoutException

var retryCount = 0

/**
 * Esta clase actua como un middleware que intercepta todas las peticiones y respuestas de los servicios
 */
class AuthorizationInterceptor : Interceptor {
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {


        var continueRequest = true
        val request = chain.request()
        val isHeaderRetry = (request.header("isRetry") ?: "false").toBoolean()

        if (bodyToString(request.body()).contains("REINTENTO")) {
            retryCount++
            if (retryCount == 30) {
                BaseObservableViewModel.networkSubject.onNext(NetworkEvents.MaxNumberRetry)
                continueRequest = false
            }
        } else {
            retryCount = 0
        }

        if (continueRequest) {
            try {

                val response = chain.proceed(request)

                val responseString = response.body()?.string()

                if (response.code() == 500) {

                    BaseObservableViewModel.baseSubject.onNext(
                        BaseEvents.ShowAlertDialogInMenu(
                            "",
                            errorServerMessage(responseString)
                        )
                    )
                    return Response.Builder()
                        .code(600) //aborted request.
                        .request(chain.request())
                        .build()

                } else if (response.code() == 404) {
                    BaseObservableViewModel.baseSubject.onNext(
                        BaseEvents.ShowAlertDialogInMenu(
                            "Error",
                            "ha ocurrido un error de conexión"
                        )
                    )
                    return Response.Builder()
                        .code(600) //aborted request.
                        .request(chain.request())
                        .build()
                } else {
                    return response.newBuilder()
                        .body(
                            ResponseBody.create(response.body()?.contentType(), responseString)
                        )
                        .build()
                }
            } catch (e: SocketTimeoutException) {
                if (!isHeaderRetry) {
                    BaseObservableViewModel.networkSubject.onNext(NetworkEvents.SocketTimeoutException)
                }
            }
            return chain.proceed(request)
        } else {
            return Response.Builder()
                .code(600) //aborted request.
                .request(chain.request())
                .build()
        }
    }

    private fun bodyToString(request: RequestBody?): String {
        try {
            val buffer = Buffer()
            if (request != null)
                request.writeTo(buffer)
            else
                return ""
            return buffer.readUtf8()
        } catch (e: IOException) {
            return "did not work"
        }
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun errorServerMessage(responseString: String?): String {

        return try {
            val jsonObject = JSONObject(responseString)
            val message = jsonObject.getString("mensaje")
            message
        } catch (e: JSONException) {
            "Error de conexión servidor"
        }

    }
}
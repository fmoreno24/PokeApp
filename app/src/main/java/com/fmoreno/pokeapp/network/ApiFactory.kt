package com.fmoreno.pokeapp.network

import com.fmoreno.pokeapp.network.Constant.Companion.BASE_URL
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Clase usada para realizar el llamado de los servicios por medio de la librería retrofit
 */
class ApiFactory {
    // Inicialización del api factory
    init {
        setup()
    }

    companion object {
        // Timeout usado por defecto al momento de llamar los servicios
        private val defaultTimeOut: Long = 60L
        // URL con la cual se llama a los servicios
        private var url = BASE_URL
        private var retrofit: Retrofit.Builder? = null

        /**
         * Método público usado para llamar la configuración de la api
         */
        fun build(): PokemonService? {
            return setup(defaultTimeOut)
                ?.build()?.create(PokemonService::class.java)
        }

        /**
         * Método privado usado para retornar la librería de retrofit ya configurada con los parámetros necesarios
         * para realizar el llamado de los servicios
         */
        private fun setup(timeOut: Long = defaultTimeOut): Retrofit.Builder? {
            retrofit = Retrofit.Builder()
            // Creación del cliente HTTP para realizar el llamado de los servicios
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val clientBuilder = HttpHelper.getUnsafeOkHttpClient()

                .readTimeout((timeOut * 10), TimeUnit.SECONDS)
                //  .addInterceptor(logging)
                .addInterceptor(AuthorizationInterceptor())

            // En caso de ser necesario se asigna el certificado SSL a la petición de los servicios
            /*if (BuildConfig.SHOULD_USE_SSL) {
                clientBuilder = SSLConfig().addSSLToHttpClientBuilder(clientBuilder, PagaTodoApplication.getAppContext())
            }*/
            // Configuración del cliente de retrofit que será retornado para poder proceder con el llamado de los servicios
            retrofit?.client(clientBuilder.build())
                ?.baseUrl(url)
                ?.addConverterFactory(GsonConverterFactory.create())
                ?.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            return retrofit
        }
    }
}
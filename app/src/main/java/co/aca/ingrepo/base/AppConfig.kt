package co.aca.ingrepo.base

abstract class AppConfig {

    // 2 dk
    val SESSION_TIMEOUT: Long = 120

    /**
     * @return base endpoint url
     */
    abstract fun apiUrl(): String
}
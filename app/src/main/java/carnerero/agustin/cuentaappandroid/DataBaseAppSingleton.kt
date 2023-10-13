package carnerero.agustin.cuentaappandroid


import android.content.Context

object DataBaseAppSingleton {
    private var instance: DataBaseApp? = null
    fun getInstance(context: Context?): DataBaseApp {
        if (instance == null) {
            instance = DataBaseApp(context, "cuentaApp", null, 1)
        }
        return instance as DataBaseApp
    }
}

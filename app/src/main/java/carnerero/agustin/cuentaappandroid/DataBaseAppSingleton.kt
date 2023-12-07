package carnerero.agustin.cuentaappandroid


import android.content.Context
/* Para garantizar que la instancia se cree de manera segura en entornos multiproceso,
 puedes usar synchronized */
object DataBaseAppSingleton {
    private var instance: DataBaseApp? = null
    fun getInstance(context: Context?): DataBaseApp {
        if (instance == null) {
            synchronized(this) {
                if (instance == null) {
                    instance = DataBaseApp(context, AppConst.NAMEDB, null, AppConst.VERSIONDB)
                }
            }
        }
        return instance as DataBaseApp
    }
}

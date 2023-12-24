package carnerero.agustin.cuentaappandroid.observer

interface Subject {
    fun addObserver(observador: Observer)
    fun removeObserver(observador: Observer)
    fun notifyObserver()
}
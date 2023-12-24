package carnerero.agustin.cuentaappandroid.observer

public class IncomesSubject : Subject {
    private val observers = mutableListOf<Observer>()
    private var incomes = 0
    fun obtenerIngresos(): Int {
        return incomes
    }
    fun updateIncomes(newIncomes: Int) {
        incomes = newIncomes
        notifyObserver()
    }
    override fun addObserver(observer: Observer) {
        observers.add(observer)
    }
    override fun removeObserver(observer: Observer) {
        observers.remove(observer)
    }
    override fun notifyObserver() {
        observers.forEach { it.upDate() }
    }
}


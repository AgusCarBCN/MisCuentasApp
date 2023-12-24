package carnerero.agustin.cuentaappandroid.observer

class ExpensesObserver (private val subject: IncomesSubject,limit:Double) : Observer {
    private val limitExpenses = limit

    override fun upDate() {
        val incomes = subject.obtenerIngresos()
        // Lógica para verificar y notificar cuando los gastos alcanzan el 80% de los ingresos
        val expenses = obtenerGastos()  // Reemplaza esto con la lógica real para obtener los gastos
        if (expenses > incomes * limitExpenses) {
            println("¡Atención! Los gastos han alcanzado el 80% de los ingresos.")
        }
    }

    private fun obtenerGastos(): Int {
        // Lógica para obtener los gastos (debes implementar esto según tu aplicación)
        return 0
    }


}
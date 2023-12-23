package carnerero.agustin.cuentaappandroid.interfaces


    interface IObservable {
        val observers: ArrayList<Observer>

        fun add(observer: Observer) {
            observers.add(observer)
        }

        fun remove(observer: Observer) {
            observers.remove(observer)
        }

        fun sendUpdateEvent() {
            observers.forEach { it.upDate() }
        }
    }

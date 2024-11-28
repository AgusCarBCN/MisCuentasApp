package carnerero.agustin.cuentaappandroid.presentation.ui.createaccounts.model

data class Currency(private val code: String, private val description:String ,private val idResource:Int)

{
    val currencyCode: String
        get() = code

    val currencyDescription: String
    get() = description

    val flag:Int
    get() = idResource

    override fun toString(): String {
        return "$currencyCode  $description"
    }
}
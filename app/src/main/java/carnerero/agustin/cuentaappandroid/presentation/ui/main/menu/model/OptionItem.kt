package carnerero.agustin.cuentaappandroid.presentation.ui.main.menu.model

data class OptionItem(private val resourceTitle: Int, private val resourceIcon: Int) {
    val resourceTitleItem: Int
        get() = resourceTitle
    val resourceIconItem: Int
        get() = resourceIcon
}
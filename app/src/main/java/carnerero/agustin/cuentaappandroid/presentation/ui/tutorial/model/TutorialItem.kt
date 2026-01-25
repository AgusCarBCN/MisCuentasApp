package carnerero.agustin.cuentaappandroid.presentation.ui.tutorial.model

import androidx.annotation.DrawableRes

data class TutorialItem(
    private val title: String,
    private val description: String,
    @param:DrawableRes private val icon: Int
) {

    val titleItem: String
        get() = title

    val descriptionItem: String
        get() = description

    val iconItem: Int
        get() = icon

}
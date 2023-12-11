package carnerero.agustin.cuentaappandroid.customdialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.databinding.ActivityCreateUserBinding


class ChangeValuesProfile(context: Context):AlertDialog(context) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = LayoutInflater.from(context)

        val dialogView = inflater.inflate(R.layout.custom_infodialog, null)

        val titleTextView =dialogView.findViewById<TextView>(R.id.tv_title)
        val valueToChange=dialogView.findViewById<EditText>(R.id.et_valuetochange)

        titleTextView.text = "Título Personalizado"


        button.setOnClickListener {
            // Puedes realizar acciones adicionales al hacer clic en el botón aquí.
            dismiss() // Cierra el cuadro de diálogo.
        }

        setView(dialogView)
    }
}
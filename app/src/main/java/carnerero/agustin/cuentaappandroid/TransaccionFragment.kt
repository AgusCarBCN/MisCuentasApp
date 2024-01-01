package carnerero.agustin.cuentaappandroid

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.dao.CuentaDao
import carnerero.agustin.cuentaappandroid.dao.MovimientoBancarioDAO
import carnerero.agustin.cuentaappandroid.databinding.FragmentTransaccionBinding
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario
import java.text.SimpleDateFormat
import java.util.Date

class TransaccionFragment : Fragment() {

    // Variables para almacenar el valor de los elementos seleccionados en los spinners
    private var selectedAccountFrom: String? = null
    private var selectedAccountTo: String? = null

    // Instancia de la base de datos y DAOs necesarios
    private val admin = DataBaseAppSingleton.getInstance(context)
    private val cuentaDao = CuentaDao(admin)
    private val movimientoBancarioDAO = MovimientoBancarioDAO(admin)
    private val cuentas = cuentaDao.listarTodasLasCuentas()
    private val arrayCuentas = Array(cuentas.size) { "" }
    // View Binding para acceder a los componentes de la interfaz de usuario
    private var _binding: FragmentTransaccionBinding? = null
    private val binding get() = _binding!!


    // Método llamado para crear la vista del fragmento
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflar el diseño de fragment_transaccion.xml utilizando View Binding
        _binding = FragmentTransaccionBinding.inflate(inflater, container, false)
        val view = binding.root

        // Obtener todos los componentes del fragmento
        val importe = binding.etImportetrans
        val tvAccountFrom = binding.tvcuentaorigen
        val tvAccountTo = binding.tvcuentadestino
        val aceptar = binding.btnAceptar
        val salir = binding.btnSalir

        //Llenar arrayCuentas
        for (i in 0 until cuentas.size) {
            arrayCuentas[i]=cuentas.get(i).iban
        }
        selectedAccountFrom=arrayCuentas[0]
        if(arrayCuentas.size>1){
            selectedAccountTo=arrayCuentas[1]
        }else selectedAccountTo=arrayCuentas[0]

        tvAccountFrom.setOnClickListener {
            showSelectAccountDialog(tvAccountFrom,true)
        }

        tvAccountTo.setOnClickListener {
            showSelectAccountDialog(tvAccountTo,false)
        }

        // Configurar el evento de clic para el botón "Aceptar"
        aceptar.setOnClickListener {
            // Obtener la fecha actual en formato dd/MM/yyyy
            val fechaImporte = SimpleDateFormat("dd/MM/yyyy").format(Date())

            // Obtener el importe del EditText
            val importeText = importe.text.toString().trim()

            // Verificar si el campo de importe está vacío
            if (importeText.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.msgemptyfield),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (selectedAccountTo == selectedAccountFrom) {
                // Verificar si las cuentas de origen y destino son las mismas
                Toast.makeText(
                    requireContext(),
                    getString(R.string.msgtransfersame),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // Realizar la transferencia ya que el campo de importe no está vacío

                Toast.makeText(
                    requireContext(),
                    getString(R.string.msgntransfer),
                    Toast.LENGTH_SHORT
                ).show()

                // Convertir el importe a valores numéricos (positivo y negativo)
                val importeNegativo = -importeText.toDouble()
                val importePositivo = importeText.toDouble()

                // Actualizar saldos de cuentas
                cuentaDao.actualizarSaldo(importeNegativo, selectedAccountFrom.toString())
                cuentaDao.actualizarSaldo(importePositivo, selectedAccountTo.toString())

                // Registrar la transacción en el historial de movimientos
                val movimientoBancarioOrigen = MovimientoBancario(
                    importeNegativo,
                    "Transacción realizada",
                    selectedAccountFrom.toString(),
                    fechaImporte
                )
                val movimientoBancarioDestino = MovimientoBancario(
                    importePositivo,
                    "Transacción recibida",
                    selectedAccountTo.toString(),
                    fechaImporte
                )
                movimientoBancarioDAO.nuevoImporte(movimientoBancarioOrigen)
                movimientoBancarioDAO.nuevoImporte(movimientoBancarioDestino)

                // Limpiar el campo de importe
                importe.text.clear()

                // Actualizar el fragmento de saldo en la actividad principal
                (activity as MainActivity).actualizarFragmentSaldo()
            }
        }

        // Configurar el evento de clic para el botón "Salir"
        salir.setOnClickListener {

            // Volver a la pantalla de inicio
            (activity as MainActivity).inicio()
        }

        return view
    }

    // Método llamado cuando el fragmento es destruido
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Importante para evitar fugas de memoria
    }

    private fun showSelectAccountDialog(cuenta:TextView,cuentaOrigin:Boolean){
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.custom_selectaccount_dialog, null)
        val title = dialogView.findViewById<TextView>(R.id.tv_dialogtitleaccount)
        val account = dialogView.findViewById<NumberPicker>(R.id.accountpicker)
        val confirmButton = dialogView.findViewById<Button>(R.id.btn_confirmaccount)
        val currentAccount=arrayCuentas[0]
        account.wrapSelectorWheel=true
        account.minValue = 0
        account.maxValue = arrayCuentas.size - 1
        account.value=arrayCuentas.indexOf(currentAccount)
        account.displayedValues=arrayCuentas
        title.text = getString(R.string.select_account)
        builder.setView(dialogView)
        val dialog = builder.create()
        confirmButton.setOnClickListener {

            cuenta.text = arrayCuentas[account.value]
            dialog.dismiss()
            if(cuentaOrigin){
                selectedAccountTo=arrayCuentas[account.value]
            }else{
                selectedAccountFrom=arrayCuentas[account.value]
            }

        }
        // Configurar el fondo transparente
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

    }

}

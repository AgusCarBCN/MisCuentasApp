package carnerero.agustin.cuentaappandroid



import android.app.AlertDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import carnerero.agustin.cuentaappandroid.dao.CuentaDao
import carnerero.agustin.cuentaappandroid.dao.MovimientoBancarioDAO
import carnerero.agustin.cuentaappandroid.databinding.FragmentNewAmountBinding
import carnerero.agustin.cuentaappandroid.model.Cuenta
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.abs

class NewAmountFragment : Fragment() {

    // Variable para manejar el View Binding
    private var _binding: FragmentNewAmountBinding? = null
    private val binding get() = _binding!!
    // Instancias necesarias para acceder a la base de datos y realizar operaciones
    private val admin = DataBaseAppSingleton.getInstance(context)
    private val movDao = MovimientoBancarioDAO(admin)
    private val cuentaDao = CuentaDao(admin)
    private val cuentas = cuentaDao.listarTodasLasCuentas()
    private val arrayCuentas = Array(cuentas.size) { "" }
    private lateinit var selectedAccount: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Obtener argumentos si los hay
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflar el diseño del fragmento utilizando View Binding
        _binding = FragmentNewAmountBinding.inflate(inflater, container, false)
        val view = binding.root

        // Acceso a los componentes de la interfaz
        val tvCuenta = binding.tvcuentanewamount
        val nuevoIngreso = binding.btnNuevoingreso
        val nuevoGasto = binding.btnNuevogasto
        val descripcion = binding.etDescripcion
        val importe = binding.etImporte

        //Llenar arrayCuentas
        for (i in 0 until cuentas.size) {
            arrayCuentas[i]= cuentas[i].iban
        }
        selectedAccount=""

        tvCuenta.setOnClickListener {
            if(cuentas.size==0){
                tvCuenta.text=getString(R.string.noaccounts)
                Toast.makeText(
                    requireContext(),getString(R.string.create_your_account),
                    Toast.LENGTH_SHORT
                ).show()
            }else {
                showSelectAccountDialog()
            }
        }



        // Acciones a realizar cuando se hace clic en el botón de nuevo ingreso
        nuevoIngreso.setOnClickListener {
            val fechaImporte = SimpleDateFormat("dd/MM/yyyy").format(Date())

            if (importe.text.isNullOrBlank() || descripcion.text.isNullOrBlank()) {
                if (importe.text.isNullOrBlank() && descripcion.text.isNullOrBlank()) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.msgemptiesfield),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (importe.text.isNullOrBlank()) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.msgemptyamount),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    if (descripcion.text.isNullOrBlank()) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.msgemptydes),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }else if(selectedAccount.isBlank())   {
                    Toast.makeText(
                        requireContext(),
                        "${getString(R.string.msgselectaccount)} ${getString(R.string.incomem)}",
                        Toast.LENGTH_SHORT
                    ).show()

            } else {

                val movimientoBancario = MovimientoBancario(
                    importe.text.toString().trim().toDouble(),
                    descripcion.text.toString(),
                    selectedAccount,
                    fechaImporte
                )
                movDao.nuevoImporte(movimientoBancario)
                Toast.makeText(
                    requireContext(),
                    "${getString(R.string.msgincome)}: $selectedAccount",
                    Toast.LENGTH_SHORT
                ).show()
                cuentaDao.actualizarSaldo(
                    importe.text.toString().trim().toDouble(),
                    selectedAccount
                )
                importe.text.clear()
                descripcion.text.clear()
                // Actualizar el fragmento de saldo en la actividad principal
                (activity as MainActivity).actualizarFragmentSaldo()
            }
        }

        // Acciones a realizar cuando se hace clic en el botón de nuevo gasto
        nuevoGasto.setOnClickListener {
            val fechaImporte = SimpleDateFormat("dd/MM/yyyy").format(Date())

            if (importe.text.isNullOrBlank() || descripcion.text.isNullOrBlank()) {
                if (importe.text.isNullOrBlank() && descripcion.text.isNullOrBlank()) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.msgemptiesfield),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (importe.text.isNullOrBlank()) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.msgemptyamount),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    if (descripcion.text.isNullOrBlank()) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.msgemptydes),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        else if(selectedAccount.isBlank())   {
                Toast.makeText(
                    requireContext(),
                    "${getString(R.string.msgselectaccount)} ${getString(R.string.billm)}",
                    Toast.LENGTH_SHORT
                ).show()

            }

            else {
                val cuenta=searchAccount(cuentas,selectedAccount)
                val saldo= cuenta?.saldo
                val importeText = importe.text.toString()
                val importeNumerico = if (importeText.isNotEmpty()) -importeText.toDouble() else 0.0
                // Controlar que el importe no sea superior a los saldos de las cuentas
                if (abs(importeNumerico) > saldo!!) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.alertamounts),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val movimientoBancario = MovimientoBancario(
                        importeNumerico,
                        descripcion.text.toString(),
                        selectedAccount,
                        fechaImporte
                    )
                    movDao.nuevoImporte(movimientoBancario)
                    Toast.makeText(
                        requireContext(),
                        "${getString(R.string.msgbill)}: $selectedAccount",
                        Toast.LENGTH_SHORT
                    ).show()
                    cuentaDao.actualizarSaldo(importeNumerico, selectedAccount)
                    importe.text.clear()
                    descripcion.text.clear()
                    // Actualizar el fragmento de saldo en la actividad principal
                    (activity as MainActivity).actualizarFragmentSaldo()
                }
            }
        }

        return view
    }

    override fun onDestroyView() {
        // Importante para evitar fugas de memoria al destruir la vista del fragmento
        super.onDestroyView()
        _binding = null
    }



    private fun searchAccount(cuentas: List<Cuenta>?, iban: String): Cuenta? {

        for (cuenta in cuentas!!) {
            if (iban == cuenta.iban) {
                return cuenta
            }
        }
        return null
    }

    private fun showSelectAccountDialog(){
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
            val tvAccount = binding.tvcuentanewamount
            tvAccount.text = arrayCuentas[account.value]
            dialog.dismiss()
            selectedAccount=arrayCuentas[account.value]
        }
        // Configurar el fondo transparente
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

    }
}

package carnerero.agustin.cuentaappandroid

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import carnerero.agustin.cuentaappandroid.dao.CuentaDao
import carnerero.agustin.cuentaappandroid.dao.MovimientoBancarioDAO
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.abs


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewAmountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewAmountFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val admin = DataBaseAppSingleton.getInstance(context)
    private val movDao = MovimientoBancarioDAO(admin)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var selectedItem: String? = null

        // Inflate the layout for this fragment
        val rootview = inflater.inflate(R.layout.fragment_new_amount, container, false)

        //Recupero dni del usuario que inicio sesion
        val sharedPreferences =
            requireContext().getSharedPreferences("dataLogin", Context.MODE_PRIVATE)

        val dni = sharedPreferences.getString("dni", "") ?: ""
        val spinnerCuentas = rootview.findViewById<Spinner>(R.id.sp_cuentas)
        val nuevoIngreso: Button = rootview.findViewById(R.id.btn_nuevoingreso)
        val nuevoGasto: Button = rootview.findViewById(R.id.btn_nuevogasto)
        val descripcion: EditText = rootview.findViewById(R.id.et_descripcion)
        val importe: EditText = rootview.findViewById(R.id.et_importe)
        val adapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item)
        val cuentaDao = CuentaDao(admin)
        val cuentas = cuentaDao.listarCuentasPorDNI(dni)
        val saldo1 = cuentas[0].saldo
        val saldo2 = cuentas[1].saldo

        adapter.add("Selecciona una cuenta")
        adapter.add(cuentas[0].iban)
        adapter.add(cuentas[1].iban)
        spinnerCuentas.adapter = adapter
        spinnerCuentas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedItem = adapter.getItem(position)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        nuevoIngreso.setOnClickListener {
            val fechaImporte= SimpleDateFormat("dd/MM/yyyy").format(Date())
            if (selectedItem == "Selecciona una cuenta") {
                Toast.makeText(
                    requireContext(),
                    "Debes seleccionar una cuenta para añadir un nuevo ingreso",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (importe.text.isNullOrBlank() || descripcion.text.isNullOrBlank()) {
                Toast.makeText(
                    requireContext(),
                    "Los campos de importe y descripción no pueden estar vacíos",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val movimientoBancario = MovimientoBancario(
                    importe.text.toString().trim().toDouble(),
                    descripcion.text.toString(),
                    selectedItem.toString(),
                    fechaImporte
                )
                movDao.nuevoImporte(movimientoBancario)
                Toast.makeText(
                    requireContext(),
                    "Nuevo ingreso en: $selectedItem",
                    Toast.LENGTH_SHORT
                ).show()
                cuentaDao.actualizarSaldo(
                    importe.text.toString().trim().toDouble(),
                    selectedItem.toString()
                )
                (activity as NavActivity).actualizarFragmentSaldo()
            }
        }

        nuevoGasto.setOnClickListener {
            val fechaImporte= SimpleDateFormat("dd/MM/yyyy").format(Date())
            if (selectedItem == "Selecciona una cuenta") {
                Toast.makeText(
                    requireContext(),
                    "Debes seleccionar una cuenta para añadir un nuevo gasto",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (importe.text.isNullOrBlank() || descripcion.text.isNullOrBlank()) {
                Toast.makeText(
                    requireContext(),
                    "Los campos de importe y descripción no pueden estar vacíos",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val importeText = importe.text.toString()
                val importeNumerico = if (importeText.isNotEmpty()) -importeText.toDouble() else 0.0
                //Controlo que el importe no sea superior a saldos de las cuentas
                if (abs(importeNumerico) > saldo1 || abs(importeNumerico) > saldo2) {
                    Toast.makeText(
                        requireContext(),
                        "El importe del gasto no puede ser mayor que el saldo actual de la cuenta",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val movimientoBancario = MovimientoBancario(
                        importeNumerico,
                        descripcion.text.toString(),
                        selectedItem.toString(),
                        fechaImporte
                    )
                    movDao.nuevoImporte(movimientoBancario)
                    Toast.makeText(
                        requireContext(),
                        "Nuevo gasto en: $selectedItem",
                        Toast.LENGTH_SHORT
                    ).show()
                    cuentaDao.actualizarSaldo(importeNumerico, selectedItem.toString())
                    (activity as NavActivity).actualizarFragmentSaldo()
                }
            }
        }


        return rootview
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewAmountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewAmountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}
package carnerero.agustin.cuentaappandroid



import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.dao.CuentaDao
import carnerero.agustin.cuentaappandroid.dao.MovimientoBancarioDAO
import carnerero.agustin.cuentaappandroid.databinding.FragmentNewAmountBinding
import carnerero.agustin.cuentaappandroid.model.Cuenta
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
    // Parámetros que podrían ser utilizados en el futuro
    private var param1: String? = null
    private var param2: String? = null

    // Variable para manejar el View Binding
    private var _binding: FragmentNewAmountBinding? = null
    private val binding get() = _binding!!

    // Instancias necesarias para acceder a la base de datos y realizar operaciones
    private val admin = DataBaseAppSingleton.getInstance(context)
    private val movDao = MovimientoBancarioDAO(admin)
    private lateinit var selectedItem: String
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Obtener argumentos si los hay
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflar el diseño del fragmento utilizando View Binding
        _binding = FragmentNewAmountBinding.inflate(inflater, container, false)
        val view = binding.root

        // Recuperar el DNI del usuario que inició sesión
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val dni = sharedPreferences.getString(getString(R.string.id), null)

        // Acceso a los componentes de la interfaz
        val spinnerCuentas = binding.spCuentas
        val nuevoIngreso = binding.btnNuevoingreso
        val nuevoGasto = binding.btnNuevogasto
        val descripcion = binding.etDescripcion
        val importe = binding.etImporte

        // Creación del adapter para el spinner
        val adapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item)
        val cuentaDao = CuentaDao(admin)
        val cuentas = dni?.let { cuentaDao.listarCuentasPorDNI(it) }


        // Configuración del adapter y del spinner
        with(adapter) {
            add(getString(R.string.select_account))
            cuentas?.forEach { cuenta ->
                add(cuenta.iban)
            }
        }
        spinnerCuentas.adapter = adapter

        // Listener para el spinner que guarda la cuenta seleccionada
        spinnerCuentas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedItem = adapter.getItem(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Acciones a realizar cuando no se selecciona nada
            }
        }

        // Acciones a realizar cuando se hace clic en el botón de nuevo ingreso
        nuevoIngreso.setOnClickListener {
            val fechaImporte = SimpleDateFormat("dd/MM/yyyy").format(Date())

            if (selectedItem == getString(R.string.select_account)) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.alertnewamount),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (importe.text.isNullOrBlank() || descripcion.text.isNullOrBlank()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.msgemptiesfield),
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                val movimientoBancario = MovimientoBancario(
                    importe.text.toString().trim().toDouble(),
                    descripcion.text.toString(),
                    selectedItem,
                    fechaImporte
                )
                movDao.nuevoImporte(movimientoBancario)
                Toast.makeText(
                    requireContext(),
                    "${getString(R.string.msgincome)}: $selectedItem",
                    Toast.LENGTH_SHORT
                ).show()
                cuentaDao.actualizarSaldo(
                    importe.text.toString().trim().toDouble(),
                    selectedItem
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

            if (selectedItem == getString(R.string.select_account)) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.alertnewamount),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (importe.text.isNullOrBlank() || descripcion.text.isNullOrBlank()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.msgemptiesfield),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val cuenta=searchAccount(cuentas,selectedItem)
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
                        selectedItem,
                        fechaImporte
                    )
                    movDao.nuevoImporte(movimientoBancario)
                    Toast.makeText(
                        requireContext(),
                        "${getString(R.string.msgbill)}: $selectedItem",
                        Toast.LENGTH_SHORT
                    ).show()
                    cuentaDao.actualizarSaldo(importeNumerico, selectedItem)
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

    companion object {
        /**
         * Este método estático se puede utilizar para crear una nueva instancia del fragmento.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewAmountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun searchAccount(cuentas: List<Cuenta>?, iban: String): Cuenta? {

        for (cuenta in cuentas!!) {
            if (iban.equals(cuenta.iban)) {
                return cuenta
            }
        }
        return null
    }


}

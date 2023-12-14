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
import carnerero.agustin.cuentaappandroid.databinding.FragmentTransaccionBinding
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario
import java.text.SimpleDateFormat
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TransaccionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransaccionFragment : Fragment() {

    // Parámetros de la instancia (puedes cambiarlos según tus necesidades)
    private var param1: String? = null
    private var param2: String? = null

    // Variables para almacenar el valor de los elementos seleccionados en los spinners
    private var selectedItemOrigen: String? = null
    private var selectedItemDestino: String? = null

    // Instancia de la base de datos y DAOs necesarios
    private val admin = DataBaseAppSingleton.getInstance(context)
    private val cuentaDao = CuentaDao(admin)
    private val movimientoBancarioDAO = MovimientoBancarioDAO(admin)

    // View Binding para acceder a los componentes de la interfaz de usuario
    private var _binding: FragmentTransaccionBinding? = null
    private val binding get() = _binding!!

    // SharedPreferences para almacenar y recuperar datos de forma sencilla
    private lateinit var sharedPreferences: SharedPreferences

    // Método llamado cuando se crea el fragmento
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recuperar los argumentos proporcionados al crear la instancia del fragmento
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

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
        val cuentaOrigen = binding.spCuentaorigen
        val cuentaDestino = binding.spCuentadestino
        val aceptar = binding.btnAceptar
        val salir = binding.btnSalir

        // Recuperar el DNI del usuario que inició sesión
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val dni = sharedPreferences.getString(getString(R.string.userdni), null)

        // Crear un adaptador de cadena (String) para llenar los Spinners
        val adapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item)

        // Obtener las cuentas del usuario logeado con el DNI
        val cuentas = dni?.let { cuentaDao.listarCuentasPorDNI(it) }
        // Verificar si la lista de cuentas es nula o vacía
        if (cuentas.isNullOrEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.noaccounts), Toast.LENGTH_SHORT).show()
            return binding.root
        //Verifico si solo hay una cuenta
        }
        if(cuentas.size==1){
            Toast.makeText(requireContext(), getString(R.string.oneaccount), Toast.LENGTH_SHORT).show()
            return binding.root
        }

        // Llenar los dos Spinners con las cuentas disponibles
        with(adapter) {
            cuentas?.forEach { cuenta ->
                add(cuenta.iban)
            }
        }
        cuentaOrigen.adapter = adapter
        cuentaDestino.adapter = adapter

        // Seleccionar por defecto la cuenta principal en el Spinner de cuenta origen y la secundaria en cuenta destino
        cuentaOrigen.setSelection(0)
        cuentaDestino.setSelection(1)

        // Configurar el manejo de eventos al seleccionar elementos en los Spinners
        cuentaOrigen.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedItemOrigen = adapter.getItem(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Manejar el caso cuando no se selecciona nada
            }
        }

        cuentaDestino.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedItemDestino = adapter.getItem(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Manejar el caso cuando no se selecciona nada
            }
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
            } else if (selectedItemDestino == selectedItemOrigen) {
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
                cuentaDao.actualizarSaldo(importeNegativo, selectedItemOrigen.toString())
                cuentaDao.actualizarSaldo(importePositivo, selectedItemDestino.toString())

                // Registrar la transacción en el historial de movimientos
                val movimientoBancarioOrigen = MovimientoBancario(
                    importeNegativo,
                    "Transacción realizada",
                    selectedItemOrigen.toString(),
                    fechaImporte
                )
                val movimientoBancarioDestino = MovimientoBancario(
                    importePositivo,
                    "Transacción recibida",
                    selectedItemDestino.toString(),
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

    // Método companion utilizado para crear una nueva instancia del fragmento
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TransaccionFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TransaccionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

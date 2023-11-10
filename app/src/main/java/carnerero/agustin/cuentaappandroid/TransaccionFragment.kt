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
import carnerero.agustin.cuentaappandroid.utils.Utils
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
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //Variables donde se almacenara el valor de los items selecionados de cada spinner
    private var selectedItemOrigen: String? = null
    private var selectedItemDestino: String? = null
    private val admin = DataBaseAppSingleton.getInstance(context)
    private val cuentaDao = CuentaDao(admin)
    private val movimientoBancarioDAO = MovimientoBancarioDAO(admin)
    private var _binding: FragmentTransaccionBinding? = null
    private lateinit var sharedPreferences: SharedPreferences
    private val binding get() = _binding!!
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
    ): View {
        _binding = FragmentTransaccionBinding.inflate(inflater, container, false)
        val view = binding.root
        //Obtener todos los componentes del fragment
        val importe = binding.etImportetrans
        val cuentaOrigen = binding.spCuentaorigen
        val cuentaDestino = binding.spCuentadestino
        val aceptar = binding.btnAceptar
        val salir = binding.btnSalir
        //Recupero dni del usuario que inicio sesion
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val dni = sharedPreferences.getString(getString(R.string.id), null)

        //Creo  un adaptador de cadena (String) para llenar un Spinner
        val adapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item)

        //Obtengo las cuentas del usuario logeado con el dni
        val cuentas = dni?.let { cuentaDao.listarCuentasPorDNI(it) }
        //Lleno los dos spinners
        adapter.add(cuentas?.get(0)?.iban)
        adapter.add(cuentas?.get(1)?.iban)
        cuentaOrigen.adapter = adapter
        cuentaDestino.adapter = adapter
        //Selecciono los elementos por defecto en origen la cuenta principal y en destino la secundaria
        cuentaOrigen.setSelection(0)
        cuentaDestino.setSelection(1)

        /*este código maneja la selección de elementos en un Spinner y muestra un mensaje de notificación
         (Toast) para indicar qué elemento ha sido seleccionado. La variable selectedItem se
         utiliza para almacenar el elemento seleccionado para su uso posterior al confirmar la transferencia
         con el boton aceptar.*/
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

            }
        }
        aceptar.setOnClickListener {
            val fechaImporte= SimpleDateFormat("dd/MM/yyyy").format(Date())
            val importeText = importe.text.toString().trim() // Usamos trim para eliminar espacios en blanco al principio o al final
            Utils.sound(requireContext())
            if (importeText.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.msgemptyfield),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (selectedItemDestino == selectedItemOrigen) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.msgtransfersame),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Utils.sound(requireContext())
                // Aquí puedes continuar con el proceso de transferencia ya que el campo de importe no está vacío
                Toast.makeText(
                    requireContext(),
                    getString(R.string.msgntransfer),
                    Toast.LENGTH_SHORT
                ).show()
                val importeNegativo = -importeText.toDouble()
                val importePositivo = importeText.toDouble()
                cuentaDao.actualizarSaldo(importeNegativo, selectedItemOrigen.toString())
                cuentaDao.actualizarSaldo(importePositivo, selectedItemDestino.toString())
                // Egreso en cuenta de origen
                var movimientoBancario = MovimientoBancario(
                    importeNegativo,
                    "Transacción realizada",
                    selectedItemOrigen.toString(),fechaImporte
                )
                movimientoBancarioDAO.nuevoImporte(movimientoBancario)
                // Ingreso en cuenta de destino
                movimientoBancario = MovimientoBancario(
                    importePositivo,
                    "Transacción recibida",
                    selectedItemDestino.toString(),fechaImporte
                )
                movimientoBancarioDAO.nuevoImporte(movimientoBancario)
                importe.text.clear()
                (activity as MainActivity).actualizarFragmentSaldo()
            }
        }

        salir.setOnClickListener{
            Utils.sound(requireContext())
            (activity as MainActivity).inicio()
        }
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Importante para evitar fugas de memoria

    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TransaccionFragment.
         */
        // TODO: Rename and change types and number of parameters
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
package carnerero.agustin.cuentaappandroid


import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import carnerero.agustin.cuentaappandroid.dao.CuentaDao
import carnerero.agustin.cuentaappandroid.dao.MovimientoBancarioDAO
import carnerero.agustin.cuentaappandroid.databinding.FragmentNewAmountBinding
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario
import carnerero.agustin.cuentaappandroid.utils.Utils
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
    private var _binding:FragmentNewAmountBinding?=null
    private val binding get() = _binding!!
    private val admin = DataBaseAppSingleton.getInstance(context)
    private val movDao = MovimientoBancarioDAO(admin)
    private var selectedItem: String? = null
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
        _binding= FragmentNewAmountBinding.inflate(inflater,container,false)
        val view = binding.root
        //Recupero dni del usuario que inicio sesion
        val sharedPreferences =
            requireContext().getSharedPreferences("dataLogin", Context.MODE_PRIVATE)
        val dni = sharedPreferences.getString("dni", "") ?: ""
        //Acceso a componentes
        val spinnerCuentas=binding.spCuentas
        val nuevoIngreso=binding.btnNuevoingreso
        val nuevoGasto=binding.btnNuevogasto
        val descripcion=binding.etDescripcion
        val importe=binding.etImporte

        //Creacion de adapter
        val adapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item)
        val cuentaDao = CuentaDao(admin)
        val cuentas = cuentaDao.listarCuentasPorDNI(dni)
        val saldo1 = cuentas[0].saldo
        val saldo2 = cuentas[1].saldo
        with(adapter) {
            add(getString(R.string.select_account))
            add(cuentas[0].iban)
            add(cuentas[1].iban)
        }
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
            Utils.sound(requireContext())
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
                Utils.sound(requireContext())
                val movimientoBancario = MovimientoBancario(
                    importe.text.toString().trim().toDouble(),
                    descripcion.text.toString(),
                    selectedItem.toString(),
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
                    selectedItem.toString()
                )
                importe.text.clear()
                descripcion.text.clear()
                (activity as MainActivity).actualizarFragmentSaldo()
            }
        }

        nuevoGasto.setOnClickListener {
            val fechaImporte= SimpleDateFormat("dd/MM/yyyy").format(Date())
            Utils.sound(requireContext())
            if (selectedItem == getString(R.string.select_account)) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.alertnewamount) ,
                    Toast.LENGTH_SHORT
                ).show()
            } else if (importe.text.isNullOrBlank() || descripcion.text.isNullOrBlank()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.msgemptiesfield),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Utils.sound(requireContext())
                val importeText = importe.text.toString()
                val importeNumerico = if (importeText.isNotEmpty()) -importeText.toDouble() else 0.0
                //Controlo que el importe no sea superior a saldos de las cuentas
                if (abs(importeNumerico) > saldo1 || abs(importeNumerico) > saldo2) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.alertamounts),
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
                        "${getString(R.string.msgbill)}: $selectedItem",
                        Toast.LENGTH_SHORT
                    ).show()
                    cuentaDao.actualizarSaldo(importeNumerico, selectedItem.toString())
                    importe.text.clear()
                    descripcion.text.clear()
                    (activity as MainActivity).actualizarFragmentSaldo()

                }
            }
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
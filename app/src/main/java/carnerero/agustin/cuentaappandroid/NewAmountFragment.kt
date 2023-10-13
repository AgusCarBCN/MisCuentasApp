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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
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
        var selectedItem:String?=null

        // Inflate the layout for this fragment
        val rootview= inflater.inflate(R.layout.fragment_new_amount, container, false)

        //Recupero dni del usuario que inicio sesion
        val sharedPreferences = requireContext().getSharedPreferences("MiPreferencia", Context.MODE_PRIVATE)
        //val dni = sharedPreferences.getString("dni", "")
        val dni = sharedPreferences.getString("dni", "") ?: ""
        val spinnerCuentas = rootview.findViewById<Spinner>(R.id.sp_cuentas)
        val nuevoIngreso:Button=rootview.findViewById(R.id.btn_nuevoingreso)
        val nuevoGasto:Button=rootview.findViewById(R.id.btn_nuevogasto)
        val descripcion:EditText=rootview.findViewById(R.id.et_descripcion)
        val importe:EditText=rootview.findViewById(R.id.et_importe)
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item)
        // Conecta a la base de datos y obtén los datos de la tabla "cuentas"
        val admin=   DataBaseAppSingleton.getInstance(context)
        //val admin=DataBaseApp(context,"cuentaApp",null,1)
        val cuentaDao=CuentaDao(admin)
        val cuentas=cuentaDao.listarCuentasPorDNI(dni)
        adapter.add(cuentas.get(0).iban)
        adapter.add(cuentas.get(1).iban)

        spinnerCuentas.adapter = adapter
        spinnerCuentas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedItem = adapter.getItem(position)
                Toast.makeText(
                    requireContext(),
                    "Elemento seleccionado: $selectedItem",
                    Toast.LENGTH_SHORT
                ).show()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No es necesario implementar nada aquí si no se desea realizar ninguna acción específica
            }
        }
        nuevoIngreso.setOnClickListener {
            val movDao:MovimientoBancarioDAO=MovimientoBancarioDAO(admin)
            if (selectedItem != null) {
                val movimientoBancario:MovimientoBancario=MovimientoBancario(importe.text.toString().toDouble(),
                    descripcion.text.toString(),selectedItem.toString())
                movDao.nuevoImporte(movimientoBancario)
                Toast.makeText(
                    requireContext(),
                    "nuevo ingreso en: $selectedItem",
                    Toast.LENGTH_SHORT
                ).show()
                cuentaDao.actualizarSaldo(importe.text.toString().toDouble(),selectedItem.toString())
                (activity as NavActivity).actualizarFragmentSaldo()

            } else {
                Toast.makeText(
                    requireContext(),
                    "Ningún elemento seleccionado",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        nuevoGasto.setOnClickListener {
            val movDao: MovimientoBancarioDAO = MovimientoBancarioDAO(admin)
            if (selectedItem != null) {
                val importeText = importe.text.toString()
                val importeNumerico = if (importeText.isNotEmpty()) -importeText.toDouble() else 0.0 // Convertir a cantidad negativa

                val movimientoBancario = MovimientoBancario(importeNumerico, descripcion.text.toString(), selectedItem.toString())
                movDao.nuevoImporte(movimientoBancario)
                Toast.makeText(
                    requireContext(),
                    "Nuevo gasto en: $selectedItem",
                    Toast.LENGTH_SHORT
                ).show()
                cuentaDao.actualizarSaldo(importeNumerico,selectedItem.toString())
                //actualizar el fragment con saldo actualizados cargando de nuevo el fragment
                (activity as NavActivity).actualizarFragmentSaldo()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Ningún elemento seleccionado",
                    Toast.LENGTH_SHORT
                ).show()
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
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
 * Use the [TransaccionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransaccionFragment : Fragment() {
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

        //Variables donde se almacenara el valor de los items selecionados de cada spinner
        var selectedItemOrigen:String?=null
        var selectedItemDestino:String?=null
        //Inflate the layout for this fragment
        val rootview=inflater.inflate(R.layout.fragment_transaccion, container, false)
        //Obtener todos los componentes del fragment
        val importe: EditText =rootview.findViewById(R.id.et_importetrans)
        val cuentaOrigen: Spinner =rootview.findViewById(R.id.sp_cuentaorigen)
        val cuentaDestino:Spinner=rootview.findViewById(R.id.sp_cuentadestino)
        val aceptar: Button =rootview.findViewById(R.id.btn_aceptar)
        val salir:Button=rootview.findViewById(R.id.btn_salir)
        //Recupero dni del usuario que inicio sesion
        val sharedPreferences = requireContext().getSharedPreferences("dataLogin", Context.MODE_PRIVATE)
        val dni = sharedPreferences.getString("dni", "") ?: ""
        //Creo  un adaptador de cadena (String) para llenar un Spinner
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item)
        // Conecta a la base de datos y obtén los datos de la tabla "cuentas"

        val admin=DataBaseAppSingleton.getInstance(context)
       //DataBaseApp(context,"cuentaApp",null,1)
        val cuentaDao= CuentaDao(admin)
        val movimientoBancarioDAO= MovimientoBancarioDAO(admin)
        //Obtengo las cuentas del usuario logeado con el dni
        val cuentas=cuentaDao.listarCuentasPorDNI(dni)
        //Lleno los dos spinners
        adapter.add(cuentas.get(0).iban)
        adapter.add(cuentas.get(1).iban)

        cuentaOrigen.adapter = adapter
        cuentaDestino.adapter=adapter
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
                Toast.makeText(
                    requireContext(),
                    "Elemento seleccionado: $selectedItemOrigen",
                    Toast.LENGTH_SHORT
                ).show()
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
                Toast.makeText(
                    requireContext(),
                    "Elemento seleccionado: $selectedItemDestino",
                    Toast.LENGTH_SHORT
                ).show()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        aceptar.setOnClickListener(){
            if(selectedItemDestino==selectedItemOrigen){
                Toast.makeText(
                    requireContext(),
                    "No se puede realizar transferencia en una misma cuenta",
                    Toast.LENGTH_SHORT
                ).show()

            }else{
                Toast.makeText(
                    requireContext(),
                    "Transferencia realizada con exito",
                    Toast.LENGTH_SHORT
                ).show()
                val importeText = importe.text.toString()
                val importeNegativo = if (importeText.isNotEmpty()) -importeText.toDouble() else 0.0
                val importePositivo=importeText.toDouble()
                cuentaDao.actualizarSaldo(importeNegativo,selectedItemOrigen.toString())
                cuentaDao.actualizarSaldo(importePositivo,selectedItemDestino.toString())
                //Egreso en cuenta de origen
                var movimientoBancario= MovimientoBancario(importeNegativo,
                    "Transaccion realizada",selectedItemOrigen.toString())
                movimientoBancarioDAO.nuevoImporte(movimientoBancario)
                //Ingreso en cuenta de destino
                movimientoBancario=MovimientoBancario(importePositivo,
                    "Transaccion recibida",selectedItemDestino.toString())
                movimientoBancarioDAO.nuevoImporte(movimientoBancario)

                (activity as NavActivity).actualizarFragmentSaldo()
            }
        }
        salir.setOnClickListener(){
            (activity as NavActivity).inicio()
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
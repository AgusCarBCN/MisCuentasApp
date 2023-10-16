package carnerero.agustin.cuentaappandroid

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.SearchView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentContainerView
import carnerero.agustin.cuentaappandroid.dao.CuentaDao
import carnerero.agustin.cuentaappandroid.dao.MovimientoBancarioDAO
import carnerero.agustin.cuentaappandroid.dao.MovimientoBancarioDAOProxy
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ConsultaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConsultaFragment : Fragment() {
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
        // Inflate the layout for this fragment
        val rootview= inflater.inflate(R.layout.fragment_consulta, container, false)

        //Variable que contendra la opcion seleccionada en spinner
        var selectedItem:String?=null
        //Obtenemos los componentes del fragment
        val etDateFrom=rootview.findViewById<EditText>(R.id.et_datefrom)
        val etDateTo=rootview.findViewById<EditText>(R.id.et_dateto)
        val searchView:EditText=rootview.findViewById(R.id.et_search)
        val ingresos:RadioButton=rootview.findViewById(R.id.rb_ingresos)
        val gastos:RadioButton=rootview.findViewById(R.id.rb_gastos)
        val ingresosygastos:RadioButton=rootview.findViewById(R.id.rb_ingresosygastos)
        val importeDesde:EditText=rootview.findViewById(R.id.et_importedesde)
        val importeHasta:EditText=rootview.findViewById(R.id.et_importehasta)
        val spConsulta:Spinner=rootview.findViewById(R.id.sp_consulta)
        val buscar:Button=rootview.findViewById(R.id.btn_buscar)
        val cancel:Button=rootview.findViewById(R.id.btn_cancelabuscar)
        //Recupero dni del usuario que inicio sesion
        val sharedPreferences = requireContext().getSharedPreferences("dataLogin", Context.MODE_PRIVATE)
        val dni = sharedPreferences.getString("dni", "") ?: ""
        //Rellenar spiner spConsulta
        //Creo adapter
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1)
        // Conecta a la base de datos y obtén los datos de la tabla "cuentas"
        val admin=   DataBaseAppSingleton.getInstance(context)
        //val admin=DataBaseApp(context,"cuentaApp",null,1)
        val cuentaDao= CuentaDao(admin)
        val cuentas=cuentaDao.listarCuentasPorDNI(dni)
        adapter.add(cuentas.get(0).iban)
        adapter.add(cuentas.get(1).iban)
        spConsulta.adapter = adapter
        //Obtener todos los movimientos de la base de datos
        val movDaoProxy=MovimientoBancarioDAOProxy(MovimientoBancarioDAO (admin))
        //val movDao: MovimientoBancarioDAO = MovimientoBancarioDAO(admin)
        val movList:ArrayList<MovimientoBancario> =movDaoProxy.listarMovimientos()
        //Filtrar los movimientos obtenidos

        //Muestra DatePickerDialog al cliquear edit text
        etDateFrom.setOnClickListener(){
            showDatePickerDialog(etDateFrom)

        }
        etDateTo.setOnClickListener(){
            showDatePickerDialog(etDateTo)
        }
        spConsulta.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
            }
        }
        buscar.setOnClickListener(){

        }
        cancel.setOnClickListener(){
            (activity as NavActivity).inicio()
        }
        return rootview
    }
    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.AppTheme_DialogTheme,


            { _, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth-${month + 1}-$year" // Formato de fecha deseado
                editText.setText(selectedDate)
            },
            year,
            month,
            day
        )
        val window = datePickerDialog.window

        //Color de fondo
        window?.setBackgroundDrawableResource(R.color.lightYellow)

        datePickerDialog.show()
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ConsultaFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ConsultaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
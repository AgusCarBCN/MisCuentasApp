package carnerero.agustin.cuentaappandroid

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import carnerero.agustin.cuentaappandroid.dao.CuentaDao
import carnerero.agustin.cuentaappandroid.dao.MovimientoBancarioDAO
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario
import kotlin.math.abs

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
    private val admin=  DataBaseAppSingleton.getInstance(context)
    private var selectedItem :String?=null
    private val movDao=MovimientoBancarioDAO (admin)
    private var movList:ArrayList<MovimientoBancario> =movDao.getAll()
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

        //Obtenemos los componentes del fragment
        val etDateFrom:EditText=rootview.findViewById(R.id.et_datefrom)
        val etDateTo:EditText=rootview.findViewById(R.id.et_dateto)
        val select:RadioGroup=rootview.findViewById(R.id.selectImporte)
        val ingresos:RadioButton=rootview.findViewById(R.id.rb_ingresos)
        val gastos:RadioButton=rootview.findViewById(R.id.rb_gastos)
        val ingresosygastos:RadioButton=rootview.findViewById(R.id.rb_ingresosygastos)
        val spConsulta:Spinner=rootview.findViewById(R.id.sp_consulta)
        val buscar:Button=rootview.findViewById(R.id.btn_buscar)
        val cancel:Button=rootview.findViewById(R.id.btn_cancelabuscar)
        //Recupero dni del usuario que inicio sesion
        val sharedPreferences = requireContext().getSharedPreferences("dataLogin", Context.MODE_PRIVATE)
        val dni = sharedPreferences.getString("dni", "") ?: ""
        //Rellenar spiner spConsulta
        //Creo adapter
       val adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_dropdown_item )
        // Conecta a la base de datos y obtén los datos de la tabla "cuentas"
        //val admin=   DataBaseAppSingleton.getInstance(context)

        val cuentaDao= CuentaDao(admin)
        val cuentas=cuentaDao.listarCuentasPorDNI(dni)

        adapter.add("Selecciona una cuenta")
        adapter.add(cuentas[0].iban)
        adapter.add(cuentas[1].iban)
        selectedItem=adapter.getItem(0)
        spConsulta.adapter = adapter
        //Obtener todos los movimientos de la base de datos
         //movDaoProxy=MovimientoBancarioDAOProxy(MovimientoBancarioDAO (admin))
        //val movDao: MovimientoBancarioDAO = MovimientoBancarioDAO(admin)
        //var movList:ArrayList<MovimientoBancario> =movDaoProxy.listarMovimientos()
        //Filtrar los movimientos obtenidos

        //Muestra DatePickerDialog al cliquear edit text
        etDateFrom.setOnClickListener{
            showDatePickerDialog(etDateFrom)

        }
        etDateTo.setOnClickListener{
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
                if (position == 0) {
                    Toast.makeText(
                        requireContext(),
                        "Selecciona una cuenta para habilitar la busqueda",
                        Toast.LENGTH_SHORT
                    ).show()
                   ingresos.isEnabled=false
                    gastos.isEnabled=false
                    ingresosygastos.isEnabled=false
                }else {
                    ingresos.isEnabled=true
                    gastos.isEnabled=true
                    ingresosygastos.isEnabled=true
                    Toast.makeText(
                        requireContext(),
                        "Elemento seleccionado: $selectedItem",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        fun updateMovList() {


            when (select.checkedRadioButtonId) {
                R.id.rb_ingresos -> movList.retainAll(movDao.getIncome(selectedItem.toString()).toSet())
                R.id.rb_gastos -> movList.retainAll(movDao.getBills(selectedItem.toString()).toSet())
                R.id.rb_ingresosygastos -> movList.retainAll(movDao.getIncomeandBills(selectedItem.toString())
                    .toSet())
            }

        }

        // Asignar un listener al grupo de RadioButtons
        select.setOnCheckedChangeListener { _, _ ->
            updateMovList()
        }


        //Enviamos el arrayList movList desde este fragmento al fragmento ListOfMovFragment
        //Crear un Bundle y agregar el ArrayList como argumento
        //val bundle = Bundle()
        //bundle.putParcelableArrayList("clave_movimientos", movList)
        //Inicia ListOfFragment y pasa el arrayList movList como argumento al clickear buscar
        buscar.setOnClickListener{
            //Importes
            val importeDesde: EditText = rootview.findViewById(R.id.et_importedesde)
            val importeHasta: EditText = rootview.findViewById(R.id.et_importehasta)
            val importeDesdeNum: Double? = importeDesde.text.toString().toDoubleOrNull()
            val importeHastaNum: Double? = importeHasta.text.toString().toDoubleOrNull()
            //Fechas

            val fechaDesdeStr: String = etDateFrom.text.toString()
            val fechaHastaStr: String = etDateTo.text.toString()
            //por texto
            val searchByText:EditText=rootview.findViewById(R.id.et_search)
            val searchText:String=searchByText.text.toString()
            if(selectedItem.toString() == "Selecciona una cuenta"){
                Toast.makeText(
                    requireContext(),
                    "Debes seleccionar una cuenta para mostrar resultados",
                    Toast.LENGTH_SHORT
                ).show()
            }else {
                if (importeDesdeNum != null && importeHastaNum != null) {
                    val movListFilter = movList.filter { abs(it.importe) >= importeDesdeNum && abs(it.importe) <= importeHastaNum } as ArrayList<MovimientoBancario>
                    movList.clear() // Limpia la lista actual
                    movList.addAll(movListFilter)
                }
                if (fechaDesdeStr.isNotBlank() && fechaHastaStr.isNotBlank()) {
                    movList = movList.filter { it.fechaImporte in fechaDesdeStr..fechaHastaStr } as ArrayList<MovimientoBancario>
                }
                if(searchText.isNotBlank()){
                    movList=movList.filter { it.descripcion.contains(searchText)}as ArrayList<MovimientoBancario>
                }
                val bundle = Bundle()
                bundle.putParcelableArrayList("clave_movimientos", movList)
                val fragmentList = ListOfMovFragment()
                fragmentList.arguments = bundle
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fcv_main_container, fragmentList)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }
        cancel.setOnClickListener{
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


            { _, yearFormat, monthFormat, dayOfMonth ->
                val selectedDate = "$dayOfMonth-${monthFormat + 1}-$yearFormat" // Formato de fecha deseado
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
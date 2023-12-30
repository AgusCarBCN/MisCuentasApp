package carnerero.agustin.cuentaappandroid

import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.dao.CuentaDao
import carnerero.agustin.cuentaappandroid.dao.MovimientoBancarioDAO
import carnerero.agustin.cuentaappandroid.databinding.FragmentConsultaBinding
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.abs


class ConsultaFragment : Fragment() {

    private var _binding: FragmentConsultaBinding? = null
    private val binding get() = _binding!!
    private val admin = DataBaseAppSingleton.getInstance(context)
    private var selectedItem: String? = null
    private var result = 0.0
    private val movDao = MovimientoBancarioDAO(admin)
    private var movList: ArrayList<MovimientoBancario> = movDao.getAll()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflar el diseño para este fragmento
        _binding = FragmentConsultaBinding.inflate(inflater, container, false)
        val view = binding.root

        // Componentes del fragmento
        val etDateFrom = binding.etDatefrom
        val etDateTo = binding.etDateto
        val select = binding.selectImporte
        val ingresos = binding.rbIngresos
        val gastos = binding.rbGastos
        val ingresosygastos = binding.rbIngresosygastos
        val spConsulta = binding.spConsulta
        val buscar = binding.btnBuscar
        val cancel = binding.btnCancelabuscar
        val searchByText: EditText = binding.etSearch
        val importeDesde=binding.etImportedesde
        val importeHasta=binding.etImportehasta

        // Recuperar DNI del usuario que inició sesión
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())


        // Rellenar el spinner spConsulta
        val adapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item)
        val cuentaDao = CuentaDao(admin)
        val cuentas=cuentaDao.listarTodasLasCuentas()
        // Configuración del adapter y del spinner
        with(adapter) {
            add(getString(R.string.select_account))
            cuentas.forEach { cuenta ->
                add(cuenta.iban)
            }
        }
        selectedItem = adapter.getItem(0)
        spConsulta.adapter = adapter

        // Mostrar DatePickerDialog al hacer clic en el EditText
        etDateFrom.setOnClickListener {
            showDatePickerDialog(etDateFrom)
        }
        etDateTo.setOnClickListener {
            showDatePickerDialog(etDateTo)
        }

        // Listener del spinner
        spConsulta.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedItem = adapter.getItem(position)
                if (position == 0) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.select_account),
                        Toast.LENGTH_SHORT
                    ).show()
                    ingresos.isEnabled = false
                    gastos.isEnabled = false
                    ingresosygastos.isEnabled = false
                } else {
                    ingresos.isEnabled = true
                    gastos.isEnabled = true
                    ingresosygastos.isEnabled = true
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

        // Función para actualizar la lista de movimientos
        fun updateMovList() {
            when (select.checkedRadioButtonId) {
                R.id.rb_ingresos -> movList.retainAll(
                    movDao.getIncome(selectedItem.toString()).toSet()
                )

                R.id.rb_gastos -> movList.retainAll(
                    movDao.getBills(selectedItem.toString()).toSet()
                )

                R.id.rb_ingresosygastos -> movList.retainAll(
                    movDao.getIncomeandBills(selectedItem.toString())
                        .toSet()
                )
            }
        }

        // Botón de búsqueda
        buscar.setOnClickListener {
            // Importes

            val importeDesdeNum: Double ?= importeDesde.text.toString().toDoubleOrNull()
            val importeHastaNum: Double? = importeHasta.text.toString().toDoubleOrNull()

            // Fechas
            val fechaDesdeStr: String = etDateFrom.text.toString()
            val fechaHastaStr: String = etDateTo.text.toString()
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            var fechaValida = true
            var importeValido = true

            // Por texto
            val searchText: String = searchByText.text.toString()



            if (selectedItem.toString() == getString(R.string.select_account)) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.select_account),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                updateMovList()

                if (importeDesdeNum != null && importeHastaNum != null) {
                    if (importeDesdeNum > importeHastaNum) {
                        importeValido = false
                    } else {
                        movList =
                            movList.filter { abs(it.importe) >= importeDesdeNum && abs(it.importe) <= importeHastaNum } as ArrayList<MovimientoBancario>
                    }
                }

                if (fechaDesdeStr.isNotBlank() && fechaHastaStr.isNotBlank()) {
                    val fechaTo = LocalDate.parse(fechaDesdeStr, formatter)
                    val fechaFrom = LocalDate.parse(fechaHastaStr, formatter)

                    if (fechaFrom.isBefore(fechaTo)) {
                        fechaValida = false
                    } else {
                        movList = movList.filter {
                            val fechaMovimiento = LocalDate.parse(it.fechaImporte, formatter)
                            fechaMovimiento.isAfter(fechaTo.minusDays(1)) && fechaMovimiento.isBefore(
                                fechaFrom.plusDays(1)
                            )
                        } as ArrayList<MovimientoBancario>
                    }
                }

                if (searchText.isNotBlank()) {
                    movList =
                        movList.filter { it.descripcion.contains(searchText) } as ArrayList<MovimientoBancario>
                }

                if (!fechaValida || !importeValido) {
                    // La fecha desde es mayor que la fecha hasta, muestra un mensaje de error
                    if (!fechaValida) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.msgdates),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    if (!importeValido) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.alertamounts),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {

                    result = calculateResult(movList)
                    val bundle = Bundle()
                    bundle.putParcelableArrayList("clave_movimientos", movList)
                    bundle.putDouble("Result", result)
                    val fragmentList = ListOfMovFragment()
                    val fragmentSaldo = ResultFragment()
                    fragmentList.arguments = bundle
                    fragmentSaldo.arguments = bundle
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fcv_main_container, fragmentList)
                    transaction.replace(R.id.info_container, fragmentSaldo)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
            }
        }

        // Botón de cancelar
        cancel.setOnClickListener {

            (activity as MainActivity).inicio()
        }

        return view
    }

    // Liberar el view binding
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Formato de fecha 01/09/2023
    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.AppTheme_DialogTheme,
            { _, yearFormat, monthFormat, dayOfMonth ->
                val dayFormatted = String.format(Locale.getDefault(), "%02d", dayOfMonth)
                val monthFormatted = String.format(Locale.getDefault(), "%02d", monthFormat + 1)
                val selectedDate = "$dayFormatted/$monthFormatted/$yearFormat"
                editText.setText(selectedDate)
            },
            year,
            month,
            day
        )
        //val window = datePickerDialog.window

        datePickerDialog.show()
    }

    private fun calculateResult(movList: ArrayList<MovimientoBancario>): Double {
        var result = 0.0
        for (i in 0 until movList.size) {
            result += movList[i].importe
        }
        return result
    }
}

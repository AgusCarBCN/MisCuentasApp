package carnerero.agustin.cuentaappandroid

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
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
    private var selectedAccount: String? = null
    private var result = 0.0
    private val movDao = MovimientoBancarioDAO(admin)
    private val cuentaDao = CuentaDao(admin)
    private var movList: ArrayList<MovimientoBancario> = movDao.getAll()
    private val cuentas = cuentaDao.listarTodasLasCuentas()
    private val arrayCuentas = Array(cuentas.size) { "" }

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
        val tvCuenta = binding.tvcuentasearch
        val buscar = binding.btnBuscar
        val cancel = binding.btnCancelabuscar
        val searchByText: EditText = binding.etSearch
        val importeDesde=binding.etImportedesde
        val importeHasta=binding.etImportehasta

        //Llenar arrayCuentas
        for (i in 0 until cuentas.size) {
            arrayCuentas[i]= cuentas[i].iban

        }


        tvCuenta.setOnClickListener {
            showSelectAccountDialog()
        }

            // Mostrar DatePickerDialog al hacer clic en el EditText
        etDateFrom.setOnClickListener {
            showDatePickerDialog(etDateFrom)
        }
        etDateTo.setOnClickListener {
            showDatePickerDialog(etDateTo)
        }



        // Función para actualizar la lista de movimientos
        fun updateMovList() {
            when (select.checkedRadioButtonId) {
                R.id.rb_ingresos -> movList.retainAll(
                    movDao.getIncome(selectedAccount.toString()).toSet()
                )

                R.id.rb_gastos -> movList.retainAll(
                    movDao.getBills(selectedAccount.toString()).toSet()
                )

                R.id.rb_ingresosygastos -> movList.retainAll(
                    movDao.getIncomeandBills(selectedAccount.toString())
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



            if (selectedAccount.toString() == getString(R.string.select_account)) {
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

    private fun showSelectAccountDialog(){
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.custom_selectaccount_dialog, null)
        val title = dialogView.findViewById<TextView>(R.id.tv_dialogtitleaccount)
        val account = dialogView.findViewById<NumberPicker>(R.id.accountpicker)
        val confirmButton = dialogView.findViewById<Button>(R.id.btn_confirmaccount)
        val currentAccount=arrayCuentas[0]
        account.wrapSelectorWheel=true
        account.minValue = 0
        account.maxValue = arrayCuentas.size - 1
        account.value=arrayCuentas.indexOf(currentAccount)
        account.displayedValues=arrayCuentas
        title.text = getString(R.string.select_account)
        builder.setView(dialogView)
        val dialog = builder.create()
        confirmButton.setOnClickListener {
            val tvAccount = binding.tvcuentasearch
            tvAccount.text = arrayCuentas[account.value]
            dialog.dismiss()
            selectedAccount=arrayCuentas[account.value]
                }
        // Configurar el fondo transparente
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

    }

}

package carnerero.agustin.cuentaappandroid



import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.dao.CuentaDao
import carnerero.agustin.cuentaappandroid.dao.MovimientoBancarioDAO
import carnerero.agustin.cuentaappandroid.databinding.FragmentBarChartBinding
import carnerero.agustin.cuentaappandroid.utils.Utils
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.math.abs
import kotlin.properties.Delegates


class BarChartFragment : Fragment() {

    // Variables para parámetros, base de datos, valores seleccionados y componentes del gráfico

    private val admin = DataBaseAppSingleton.getInstance(context)
    private var selectedIban: String? = null
    private var selectedYear:Int?=null
    private var rate:Double=1.0
    private val cuentaDao = CuentaDao(admin)
    private val movDao = MovimientoBancarioDAO(admin)
    private lateinit var ingresosTotales: ArrayList<Float>
    private lateinit var gastosTotales: ArrayList<Float>
    private lateinit var resultados: ArrayList<Float>
    private lateinit var barChart: BarChart
    private lateinit var barDataSetIngresos: BarDataSet
    private lateinit var barDataSetGastos: BarDataSet
    private lateinit var barDataSetResultados: BarDataSet
    private lateinit var barEntriesList: ArrayList<BarEntry>
    private lateinit var sharedPreferences: SharedPreferences
    private val months = arrayOf(
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    )

    // View binding
    private var _binding: FragmentBarChartBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflar el diseño para este fragmento
        _binding = FragmentBarChartBinding.inflate(inflater, container, false)

        // Inicializar el gráfico y los Spinners
        barChart = binding.barChart
        val spCuenta = binding.spCuenta
        val tvSelectYear=binding.tvyear
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        rate=sharedPreferences.getString(getString(R.string.conversion_rate), "1.0").toString().toDouble()
        val cuentas = cuentaDao.listarTodasLasCuentas()
        // Verificar si la lista de cuentas es nula o vacía
                if (cuentas.isEmpty()) {
                    Toast.makeText(requireContext(), getString(R.string.noaccounts), Toast.LENGTH_SHORT).show()
                    return binding.root
                }
            val years = arrayOf("2023", "2024", "2025", "2026", "2027")

            // Crear adaptadores
            val adapterCuenta = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item
            )

            // Configuración del adapter y del spinner
            with(adapterCuenta) {
                cuentas.forEach { cuenta ->
                    add(cuenta.iban)
                }
            }



            // Establecer adaptadores en los Spinners
            spCuenta.adapter= adapterCuenta
            selectedYear=Utils.getYear()
            tvSelectYear.text=Utils.getYear().toString()

            // Establecer valores predeterminados

            selectedIban = cuentas[0].iban

            // Escuchadores de los Spinners para la selección de elementos
            spCuenta.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    barChart.clear()
                    selectedIban = adapterCuenta.getItem(position)

                    /*se ejecutará en un hilo diferente al hilo principal, evitando bloquear la interfaz de usuario mientras se realiza la operación.*/
                    lifecycleScope.launch {
                        updateChart(selectedIban.toString(), selectedYear!!)
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

    tvSelectYear.setOnClickListener {
       showSelectYearDialog()

    }


        return binding.root
    }

    // Calcular resultados para la cuenta y el año seleccionados
    private fun calculateResult(iban: String, year: Int) {

            gastosTotales = ArrayList()
            ingresosTotales = ArrayList()
            resultados = ArrayList()
            val ingresos = movDao.getIncome(iban)
            val gastos = movDao.getBills(iban)

            // Calcular resultados mensuales
            for (i in 1..12) {
                val gastoMes = Utils.calcularImporteMes(i, year, gastos)*rate.toFloat()
                val ingresoMes = Utils.calcularImporteMes(i, year, ingresos)*rate.toFloat()
                val resultadoMes = ingresoMes + gastoMes
                ingresosTotales.add(ingresoMes)
                gastosTotales.add(abs(gastoMes))
                resultados.add(resultadoMes)
            }


    }

    // Crear y configurar el gráfico de barras
    private fun createBarChart() {
        with(barChart) {
            // Crear BarDataSets para ingresos, gastos y resultados
            barDataSetIngresos = BarDataSet(getBarChartData(ingresosTotales), "Ingresos")
            barDataSetIngresos.color = ContextCompat.getColor(requireContext(), R.color.darkGreenPistacho)
            barDataSetGastos = BarDataSet(getBarChartData(gastosTotales), "Gastos")
            if(Utils.isDarkTheme) barDataSetGastos.color =ContextCompat.getColor(requireContext(), R.color.coralred)
            else barDataSetGastos.color =ContextCompat.getColor(requireContext(), R.color.red)
            barDataSetResultados = BarDataSet(getBarChartData(resultados), "Resultados")
            if(Utils.isDarkTheme) barDataSetResultados.color = ContextCompat.getColor(requireContext(), R.color.lightblue)
            else barDataSetResultados.color = ContextCompat.getColor(requireContext(), R.color.blue)

            // Crear BarData y configurar el gráfico de barras
            val barData = BarData(barDataSetIngresos, barDataSetGastos, barDataSetResultados)
            barData.setValueTextSize(12f)
            data = barData
            description.isEnabled = false

            // Configurar el eje X
            if(Utils.isDarkTheme){
                xAxis.textColor=ContextCompat.getColor(requireContext(),R.color.white)
                barData.setValueTextColor(ContextCompat.getColor(requireContext(),R.color.white))

            }
            val xAxis = barChart.xAxis
            xAxis.valueFormatter = IndexAxisValueFormatter(months)
            xAxis.setCenterAxisLabels(true)
            xAxis.textSize = 12f
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            xAxis.isGranularityEnabled = true

            // Configurar tamaño de las barras y espaciado
            setVisibleXRangeMaximum(3f)
            val barSpace = 0.065f
            val groupSpace = 0.2f
            barData.barWidth = 0.20f
            xAxis.axisMinimum = 0f
            xAxis.axisMaximum = barData.getGroupWidth(groupSpace, barSpace)*12
            // Animación y otras configuraciones
            animate()
            setNoDataText("")
            groupBars(0f, groupSpace, barSpace)
            invalidate()
            setDrawValueAboveBar(true)
            isHighlightFullBarEnabled = true
            setDrawMarkers(true)
        }
    }

    // Convertir lista de cantidades a lista de BarEntry
    private fun getBarChartData(listOfAmount: ArrayList<Float>): ArrayList<BarEntry> {
        barEntriesList = ArrayList()
        for (i in 0 until listOfAmount.size) {
            barEntriesList.add(BarEntry(i.toFloat(), listOfAmount[i]))
        }
        return barEntriesList
    }

    private fun showSelectYearDialog(){
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.custom_selectyear_dialog, null)
        val title=dialogView.findViewById<TextView>(R.id.tv_dialogtitleeyear)
        val year=dialogView.findViewById<NumberPicker>(R.id.yearpicker)
        val confirmButton = dialogView.findViewById<Button>(R.id.btn_confirmyear)
        val minYear = 2000 //Año mínimo según tus necesidades
        val maxYear = 2100 //Año máximo según tus necesidades
        val yearsArray = (minYear..maxYear).toList().map { it.toString() }.toTypedArray()
        val currentYear=Utils.getYear()
        year.wrapSelectorWheel=true

        year.minValue = minYear
        year.maxValue = maxYear
        year.value = currentYear
        year.displayedValues = yearsArray
        title.text=getString(R.string.select_year)
        builder.setView(dialogView)
        val dialog = builder.create()
        confirmButton.setOnClickListener {
            var tvYear=binding.tvyear
            tvYear.text=year.value.toString()
            dialog.dismiss()
            selectedYear=year.value
            updateChart(selectedIban.toString(), selectedYear!!)
        }

        // Configurar el fondo transparente
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

    }

    // Actualizar el gráfico con nuevos datos
    private fun updateChart(iban: String, year: Int) {
        calculateResult(iban, year)
        createBarChart()
    }

    // Liberar el view binding
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

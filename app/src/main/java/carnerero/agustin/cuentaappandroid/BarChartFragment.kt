package carnerero.agustin.cuentaappandroid



import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.dao.CuentaDao
import carnerero.agustin.cuentaappandroid.dao.MovimientoBancarioDAO
import carnerero.agustin.cuentaappandroid.databinding.FragmentBarChartBinding
import carnerero.agustin.cuentaappandroid.utils.Utils
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlin.math.abs

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BarChartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BarChartFragment : Fragment() {

    // Variables para parámetros, base de datos, valores seleccionados y componentes del gráfico
    private var param1: String? = null
    private var param2: String? = null
    private val admin = DataBaseAppSingleton.getInstance(context)
    private var selectedIban: String? = null
    private var selectedYear: String? = null
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
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
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
        val spYears = binding.spYear
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val dni = sharedPreferences.getString(getString(R.string.userdni), null)
        val cuentas = dni?.let { cuentaDao.listarCuentasPorDNI(it) }
        // Verificar si la lista de cuentas es nula o vacía
                if (cuentas.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), getString(R.string.noaccounts), Toast.LENGTH_SHORT).show()
                    return binding.root
                }
            val years = arrayOf("2023", "2024", "2025", "2026", "2027")

            // Crear adaptadores
            val adapterCuenta = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item
            )
            val adapterYear = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item
            )

            // Configuración del adapter y del spinner
            with(adapterCuenta) {

                cuentas?.forEach { cuenta ->
                    add(cuenta.iban)
                }
            }

            for (i in 0..4) {
                adapterYear.add(years[i])
            }

            // Establecer adaptadores en los Spinners
            spCuenta.adapter = adapterCuenta
            spYears.adapter = adapterYear

            // Establecer valores predeterminados
            selectedYear = years[0]
            selectedIban = cuentas?.get(0)?.iban

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
                    updateChart(selectedIban.toString(), selectedYear.toString().toInt())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            spYears.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    barChart.clear()
                    selectedYear = adapterYear.getItem(position)
                    updateChart(selectedIban.toString(), selectedYear.toString().toInt())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
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
            val gastoMes = Utils.calcularImporteMes(i, year, gastos)
            val ingresoMes = Utils.calcularImporteMes(i, year, ingresos)
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
            xAxis.setAxisMaximum(barData.getGroupWidth(groupSpace, barSpace)*12)
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

    companion object {
        /**
         * Utilice este método de fábrica para crear una nueva instancia de
         * este fragmento utilizando los parámetros proporcionados.
         *
         * @param param1 Parámetro 1.
         * @param param2 Parámetro 2.
         * @return Una nueva instancia de BarChartFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) = BarChartFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }
}

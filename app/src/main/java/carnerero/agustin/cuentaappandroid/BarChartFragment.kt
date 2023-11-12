package carnerero.agustin.cuentaappandroid


import android.app.SharedElementCallback
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
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
    // TODO: Rename and change types of parameters
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
        "Enero",
        "Febrero",
        "Marzo",
        "Abril",
        "Mayo",
        "Junio",
        "Julio",
        "Agosto",
        "Septiembre",
        "Octubre",
        "Noviembre",
        "Diciembre"
    )

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
        // Inflate the layout for this fragment
        _binding = FragmentBarChartBinding.inflate(inflater, container, false)

        barChart = binding.barChart
        val spCuenta = binding.spCuenta
        val spYears = binding.spYear
        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(requireContext())
        val dni=sharedPreferences.getString(getString(R.string.id),null)
        val cuentas = dni?.let { cuentaDao.listarCuentasPorDNI(it) }
        val years = arrayOf("2023", "2024", "2025", "2026", "2027")
        //Creo adapters
        //Creo adapter
        val adapterCuenta =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item)
        //Creo adapter
        val adapterYear =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item)
        //Agrega elementos a adaptadores

        adapterCuenta.add(cuentas?.get(0)?.iban)
        adapterCuenta.add(cuentas?.get(1)?.iban)

        for(i in 0..4) {
            adapterYear.add(years[i])

        }
        //Rellena Spinners
        spCuenta.adapter = adapterCuenta
        spYears.adapter = adapterYear
        //este código establece un listener en un Spinner que captura la selección de un iban de una cuenta
        // muestra un mensaje Toast de la cuenta seleccionado. También tiene una función
        // de respaldo para cuando no se selecciona ningún element
        //Inicializamos en año 0 y cuenta principal
        selectedYear=years[0]
        selectedIban= cuentas?.get(0)?.iban

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
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
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
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        return binding.root
    }

    private fun calculateResult(iban: String, year: Int) {
        gastosTotales = ArrayList()
        ingresosTotales = ArrayList()
        resultados = ArrayList()
        val ingresos = movDao.getIncome(iban)
        val gastos = movDao.getBills(iban)
        for (i in 1..12) {
            val gastoMes = Utils.calcularImporteMes(i, year, gastos)
            val ingresoMes = Utils.calcularImporteMes(i, year, ingresos)
            val resultadoMes = ingresoMes + gastoMes
            ingresosTotales.add(ingresoMes)
            gastosTotales.add(abs(gastoMes))
            resultados.add(resultadoMes)
        }

    }
    private fun createBarChart() {
        //Creacion y configuracion del grafico de barras
        //Creacion de barDataSet de los ingresos,gasto y resultados
        with(barChart) {
            barDataSetIngresos = BarDataSet(getBarChartData(ingresosTotales), "Ingresos")
            barDataSetIngresos.color = ContextCompat.getColor(requireContext(), R.color.darkGreenPistacho)
            barDataSetGastos = BarDataSet(getBarChartData(gastosTotales), "Gastos")
            barDataSetGastos.color = ContextCompat.getColor(requireContext(), R.color.red)
            barDataSetResultados = BarDataSet(getBarChartData(resultados), "Resultados")
            barDataSetResultados.color = ContextCompat.getColor(requireContext(), R.color.blue)
            val barData = BarData(barDataSetIngresos, barDataSetGastos, barDataSetResultados)
            barData.setValueTextSize(12f)
            //Configuracion de los datos en el grafico de barras
            data = barData
            description.isEnabled = false
            //Configuracion del eje x
            val xAxis = barChart.xAxis
            //Añadir los meses en el eje X y centramos las etiquetas
            xAxis.valueFormatter = IndexAxisValueFormatter(months)
            xAxis.setCenterAxisLabels(true)
            xAxis.textSize = 12f
            //Configuracion del ejeX
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            xAxis.isGranularityEnabled = true
            isDragEnabled = true
            //Configuracion del tamaño de las barras y espacio entre ellas
            setVisibleXRangeMaximum(3f)
            val barSpace = 0.065f
            val groupSpace = 0.2f
            barData.barWidth = 0.20f
            xAxis.axisMinimum = 0f
            animate()
            setNoDataText("")
            groupBars(0f, groupSpace, barSpace)
            invalidate()
            setDrawValueAboveBar(true) // Muestra los valores encima de las barras
            isHighlightFullBarEnabled =
                true // Permite que se destaquen barras individuales al tocarlas
            setDrawMarkers(true) // Muestra tooltips cuando se toca una barra
        }
    }

    private fun getBarChartData(listOfAmount: ArrayList<Float>): ArrayList<BarEntry> {
        barEntriesList = ArrayList()
        for (i in 0..<listOfAmount.size) {
            barEntriesList.add(BarEntry(i.toFloat(), listOfAmount[i]))
        }
        return barEntriesList
    }

    private fun updateChart(iban: String, year: Int) {
            calculateResult(iban, year)
            // Actualiza el gráfico con los nuevos datos
            createBarChart()
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
         * @return A new instance of fragment BarChartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) = BarChartFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }
}
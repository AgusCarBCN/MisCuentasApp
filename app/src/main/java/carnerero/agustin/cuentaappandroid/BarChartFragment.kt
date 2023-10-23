package carnerero.agustin.cuentaappandroid


import android.content.Context
import android.graphics.Color
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
import carnerero.agustin.cuentaappandroid.databinding.FragmentBarChartBinding
import carnerero.agustin.cuentaappandroid.utils.Calculos
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
    private val movDao=MovimientoBancarioDAO(admin)
    lateinit var ingresosTotales:ArrayList<Float>
    lateinit var gastosTotales:ArrayList<Float>
    lateinit var resultados:ArrayList<Float>
    lateinit var barChart: BarChart
    lateinit var barData: BarData
    lateinit var barDataSetIngresos: BarDataSet
    lateinit var barDataSetGastos: BarDataSet
    lateinit var barDataSetResultados: BarDataSet
    lateinit var barEntriesList: ArrayList<BarEntry>

    var months = arrayOf(
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
        //return inflater.inflate(R.layout.fragment_bar_chart, container, false)
        barChart = binding.barChart
        val spCuenta = binding.spCuenta
        val spYears = binding.spYear
        val sharedPreferences =
            requireContext().getSharedPreferences("dataLogin", Context.MODE_PRIVATE)
        val dni = sharedPreferences.getString("dni", "") ?: ""
        val cuentas = cuentaDao.listarCuentasPorDNI(dni)
        val years = arrayOf("2023", "2024", "2025", "2026", "2027")
        //Creo adapters
        //Creo adapter
        val adapterCuenta =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item)
        //Creo adapter
        val adapterYear =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item)
        //Agrega elementos a adaptadores
        adapterCuenta.add("Selecciona una cuenta")
        adapterCuenta.add(cuentas[0].iban)
        adapterCuenta.add(cuentas[1].iban)

        adapterYear.add("Seleccion año")
        for (i in 0..<years.size) {
            adapterYear.add(years[i])
        }
        //Rellena Spinners
        spCuenta.adapter = adapterCuenta
        spYears.adapter = adapterYear
        //este código establece un listener en un Spinner que captura la selección de un iban de una cuenta
        // muestra un mensaje Toast de la cuenta seleccionado. También tiene una función
        // de respaldo para cuando no se selecciona ningún element
        spCuenta.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedIban = adapterCuenta.getItem(position)
                Toast.makeText(
                    requireContext(),
                    "Elemento seleccionado: $selectedIban",
                    Toast.LENGTH_SHORT
                ).show()
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
                selectedYear = adapterYear.getItem(position)
                Toast.makeText(
                    requireContext(),
                    "Elemento seleccionado: $selectedYear",
                    Toast.LENGTH_SHORT
                ).show()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


        //Obtenemos los ingresos y los gastos
        val ingresos=movDao.getIncome("BBVA")
        val gastos=movDao.getBills("BBVA")
        //Calculo de gastos por mes


            val gastoMes= Calculos.calcularImporteMes(10,2023,gastos)
            gastosTotales=ArrayList<Float>()
            gastosTotales.add(abs(gastoMes))
        Toast.makeText(
            requireContext(),
            "gasto total ${abs(gastoMes)}",
            Toast.LENGTH_SHORT
        ).show()


        //Creacion y configuracion del grafico de barras
        //Creacion de barDataSet de los ingresos,gasto y resultados
        barDataSetIngresos = BarDataSet(getBarChartDataForSet1(), "Ingresos")
        barDataSetIngresos.color = Color.GREEN
        //barDataSetIngresos.setColor(R.color.red)
        barDataSetGastos = BarDataSet(getBarChartDataForSet2(), "Gastos")
        barDataSetGastos.color=Color.RED
        barDataSetResultados = BarDataSet(getBarChartDataForSet3(), "Resultados")
        barDataSetResultados.color=Color.BLUE
        barData = BarData(barDataSetIngresos, barDataSetGastos, barDataSetResultados)
        //Configuracion de los datos en el grafico de barras
        barChart.data = barData
        barChart.description.isEnabled = false
        //Configuracion del eje x
        val xAxis = barChart.xAxis
        //Añadir los meses en el eje X y centramos las etiquetas
        xAxis.valueFormatter = IndexAxisValueFormatter(months)
        xAxis.setCenterAxisLabels(true)
        //Configuracion del ejeX
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        barChart.isDragEnabled = true
        //Configuracion del tamaño de las barras y espacio entre ellas
        barChart.setVisibleXRangeMaximum(3f)
        val barSpace = 0.065f
        val groupSpace = 0.2f
        barData.barWidth = 0.20f
        barChart.xAxis.axisMinimum = 0f
        //animacion del grafico.
        barChart.animate()
        barChart.groupBars(0f, groupSpace, barSpace)
        barChart.invalidate()
        return binding.root
    }

    private fun getBarChartDataForSet1(): ArrayList<BarEntry> {
        barEntriesList = ArrayList()

        // on below line we are adding data
        // to our bar entries list
        barEntriesList.add(BarEntry(1f, 1000f))
        barEntriesList.add(BarEntry(2f, 2000f))
        barEntriesList.add(BarEntry(3f, 5000f))
        barEntriesList.add(BarEntry(4f, 4000f))
        barEntriesList.add(BarEntry(5f, 5000f))
        barEntriesList.add(BarEntry(6f, 5000f))
        barEntriesList.add(BarEntry(7f, 5000f))
        barEntriesList.add(BarEntry(8f, 5000f))
        barEntriesList.add(BarEntry(9f, 5000f))
        barEntriesList.add(BarEntry(10f, 5000f))
        barEntriesList.add(BarEntry(11f, 5000f))
        barEntriesList.add(BarEntry(12f, 5000f))
        return barEntriesList
    }

    private fun getBarChartDataForSet2(): ArrayList<BarEntry> {
        barEntriesList = ArrayList()

       barEntriesList.add(BarEntry(1f,2000f))
        barEntriesList.add(BarEntry(2f, 2000f))
        barEntriesList.add(BarEntry(3f, 3000f))
        barEntriesList.add(BarEntry(4f, 4000f))
        barEntriesList.add(BarEntry(5f, 5000f))
        barEntriesList.add(BarEntry(6f, 5000f))
        barEntriesList.add(BarEntry(7f, 5000f))
        barEntriesList.add(BarEntry(8f, 5000f))
        barEntriesList.add(BarEntry(9f, 5000f))
        barEntriesList.add(BarEntry(10f,gastosTotales[0]))
        barEntriesList.add(BarEntry(11f, 5000f))
        barEntriesList.add(BarEntry(12f, 5000f))
        return barEntriesList
    }

    private fun getBarChartDataForSet3(): ArrayList<BarEntry> {
        barEntriesList = ArrayList()
        // on below line we are adding data
        // to our bar entries list
        barEntriesList.add(BarEntry(1f, 2000f))
        barEntriesList.add(BarEntry(2f, 4000f))
        barEntriesList.add(BarEntry(3f, 6000f))
        barEntriesList.add(BarEntry(4f, 8000f))
        barEntriesList.add(BarEntry(5f, 1000f))
        barEntriesList.add(BarEntry(6f, 5000f))
        barEntriesList.add(BarEntry(7f, 500f))
        barEntriesList.add(BarEntry(8f, 5000f))
        barEntriesList.add(BarEntry(9f, 5000f))
        barEntriesList.add(BarEntry(10f, 50f))
        barEntriesList.add(BarEntry(11f, 5000f))
        barEntriesList.add(BarEntry(12f, 10000f))

        return barEntriesList
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
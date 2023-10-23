package carnerero.agustin.cuentaappandroid


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import carnerero.agustin.cuentaappandroid.dao.CuentaDao
import carnerero.agustin.cuentaappandroid.databinding.FragmentBarChartBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

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
    lateinit var barChart: BarChart

    // a variable for bar data
    lateinit var barData: BarData

    // on below line we are creating a
    // variable for bar data set
    lateinit var barDataSetIngresos: BarDataSet
    lateinit var barDataSetGastos: BarDataSet
    lateinit var barDataSetResultados: BarDataSet

    // on below line we are creating array list for bar data
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
        // chart data to add data to our array list


        // on below line we are creating a new bar data set
        barDataSetIngresos = BarDataSet(getBarChartDataForSet1(), "Ingresos")
        barDataSetIngresos.color = Color.GREEN
        //barDataSetIngresos.setColor(R.color.red)
        barDataSetGastos = BarDataSet(getBarChartDataForSet2(), "Gastos")
        barDataSetGastos.color=Color.RED
        barDataSetResultados = BarDataSet(getBarChartDataForSet3(), "Resultados")
        barDataSetResultados.color=Color.BLUE

        barData = BarData(barDataSetIngresos, barDataSetGastos, barDataSetResultados)

        // on below line we are setting data to our chart
        barChart.data = barData

        // on below line we are setting description enabled.
        barChart.description.isEnabled = false

        // on below line setting x axis
        val xAxis = barChart.xAxis

        // below line is to set value formatter to our x-axis and
        // we are adding our days to our x axis.
        xAxis.valueFormatter = IndexAxisValueFormatter(months)

        // below line is to set center axis
        // labels to our bar chart.
        xAxis.setCenterAxisLabels(true)

        // below line is to set position
        // to our x-axis to bottom.
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        // below line is to set granularity
        // to our x axis labels.
        xAxis.granularity = 1f

        // below line is to enable
        // granularity to our x axis.
        xAxis.isGranularityEnabled = true

        // below line is to make our
        // bar chart as draggable.
        barChart.isDragEnabled = true

        // below line is to make visible
        // range for our bar chart.
        barChart.setVisibleXRangeMaximum(3f)

        // below line is to add bar
        // space to our chart.
        val barSpace = 0.065f

        // below line is use to add group
        // spacing to our bar chart.
        val groupSpace = 0.2f

        // we are setting width of
        // bar in below line.
        barData.barWidth = 0.20f


        // below line is to set minimum
        // axis to our chart.
        barChart.xAxis.axisMinimum = 0f

        // below line is to
        // animate our chart.
        barChart.animate()

        // below line is to group bars
        // and add spacing to it.
        barChart.groupBars(0f, groupSpace, barSpace)

        // below line is to invalidate
        // our bar chart.
        barChart.invalidate()

        return binding.root

    }

    private fun getBarChartDataForSet1(): ArrayList<BarEntry> {
        barEntriesList = ArrayList()

        // on below line we are adding data
        // to our bar entries list
        barEntriesList.add(BarEntry(1f, 1f))
        barEntriesList.add(BarEntry(2f, 2f))
        barEntriesList.add(BarEntry(3f, 5f))
        barEntriesList.add(BarEntry(4f, 4f))
        barEntriesList.add(BarEntry(5f, 5f))
        barEntriesList.add(BarEntry(6f, 5f))
        barEntriesList.add(BarEntry(7f, 5f))
        barEntriesList.add(BarEntry(8f, 5f))
        barEntriesList.add(BarEntry(9f, 5f))
        barEntriesList.add(BarEntry(10f, 5f))
        barEntriesList.add(BarEntry(11f, 5f))
        barEntriesList.add(BarEntry(12f, 5f))
        return barEntriesList
    }

    private fun getBarChartDataForSet2(): ArrayList<BarEntry> {
        barEntriesList = ArrayList()
        // on below line we are adding
        // data to our bar entries list
        barEntriesList.add(BarEntry(1f, 1f))
        barEntriesList.add(BarEntry(2f, 2f))
        barEntriesList.add(BarEntry(3f, 3f))
        barEntriesList.add(BarEntry(4f, 4f))
        barEntriesList.add(BarEntry(5f, 5f))
        barEntriesList.add(BarEntry(6f, 5f))
        barEntriesList.add(BarEntry(7f, 5f))
        barEntriesList.add(BarEntry(8f, 5f))
        barEntriesList.add(BarEntry(9f, 5f))
        barEntriesList.add(BarEntry(10f, 5f))
        barEntriesList.add(BarEntry(11f, 5f))
        barEntriesList.add(BarEntry(12f, 5f))
        return barEntriesList
    }

    private fun getBarChartDataForSet3(): ArrayList<BarEntry> {
        barEntriesList = ArrayList()
        // on below line we are adding data
        // to our bar entries list
        barEntriesList.add(BarEntry(1f, 2f))
        barEntriesList.add(BarEntry(2f, 4f))
        barEntriesList.add(BarEntry(3f, 6f))
        barEntriesList.add(BarEntry(4f, 8f))
        barEntriesList.add(BarEntry(5f, 10f))
        barEntriesList.add(BarEntry(6f, 5f))
        barEntriesList.add(BarEntry(7f, 5f))
        barEntriesList.add(BarEntry(8f, 5f))
        barEntriesList.add(BarEntry(9f, 5f))
        barEntriesList.add(BarEntry(10f, 5f))
        barEntriesList.add(BarEntry(11f, 5f))
        barEntriesList.add(BarEntry(12f, 5f))

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
package carnerero.agustin.cuentaappandroid

import android.R
import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import carnerero.agustin.cuentaappandroid.dao.CuentaDao
import carnerero.agustin.cuentaappandroid.dao.MovimientoBancarioDAO
import carnerero.agustin.cuentaappandroid.databinding.FragmentBarChartBinding
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario

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
    private val cuentaDao= CuentaDao(admin)
    private var _binding:FragmentBarChartBinding?=null
    private val binding get() = _binding!!
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
        _binding= FragmentBarChartBinding.inflate(inflater,container,false)
        //return inflater.inflate(R.layout.fragment_bar_chart, container, false)
        val barChart=binding.barchart
        val spCuenta=binding.spCuenta
        val spYears=binding.spYear
        val sharedPreferences =
            requireContext().getSharedPreferences("dataLogin", Context.MODE_PRIVATE)
        val dni = sharedPreferences.getString("dni", "") ?: ""
        val cuentas=cuentaDao.listarCuentasPorDNI(dni)
        val years=arrayOf("2023","2024","2025","2026","2027")
        //Creo adapters
        //Creo adapter
        val adapterCuenta =
            ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_dropdown_item)
        //Creo adapter
        val adapterYear =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item)
        //Agrega elementos a adaptadores
        adapterCuenta.add("Selecciona una cuenta")
        adapterCuenta.add(cuentas[0].iban)
        adapterCuenta.add(cuentas[1].iban)

        adapterYear.add("Seleccion año")
        for (i in 0..<years.size){
            adapterYear.add(years[i])
        }
        //Rellena Spinners
        spCuenta.adapter = adapterCuenta
        spYears.adapter = adapterYear

        return binding.root

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
        fun newInstance(param1: String, param2: String) =
            BarChartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
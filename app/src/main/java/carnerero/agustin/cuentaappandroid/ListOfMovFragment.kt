package carnerero.agustin.cuentaappandroid

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import carnerero.agustin.cuentaappandroid.adapter.AdapterMov
import carnerero.agustin.cuentaappandroid.databinding.FragmentListOfMovBinding
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario
import java.util.Locale


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ListOfMovFragment : Fragment() ,OnLocaleListener{
    // Parámetros que puedes renombrar según su uso
    private var param1: String? = null
    private var param2: String? = null
    private var lang:String?=null
    private var country:String?=null
    private lateinit var sharedPreferences: SharedPreferences
    // Adaptador y vista para la lista de movimientos
    private lateinit var adaptermovimientos: AdapterMov
    private lateinit var recyclerView: RecyclerView

    // View Binding para acceder a las vistas de diseño
    private var _binding: FragmentListOfMovBinding? = null
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
    ): View {
        // Inflar el diseño de fragmento y asignarlo a _binding
        _binding = FragmentListOfMovBinding.inflate(inflater, container, false)
        // Obtener preferencias compartidas
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        lang=sharedPreferences.getString(getString(R.string.lang), null)
        country=sharedPreferences.getString(getString(R.string.country), null)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuración del RecyclerView y el adaptador
        val movimientos = arguments?.getParcelableArrayList("clave_movimientos", MovimientoBancario::class.java)
        recyclerView = binding.rvMovimientos
        adaptermovimientos= movimientos?.let { AdapterMov(it,this) }!!
        recyclerView.apply {
            this.layoutManager= LinearLayoutManager(context)
            adapter=adaptermovimientos
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Importante para evitar fugas de memoria
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListOfMovFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun getLocale(): Locale = Locale(lang.toString(),country.toString())

}

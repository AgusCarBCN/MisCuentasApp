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
import carnerero.agustin.cuentaappandroid.adapter.AdapterBal
import carnerero.agustin.cuentaappandroid.adapter.AdapterMov
import carnerero.agustin.cuentaappandroid.dao.CuentaDao
import carnerero.agustin.cuentaappandroid.databinding.FragmentListOfAccountsBinding
import carnerero.agustin.cuentaappandroid.databinding.FragmentListOfMovBinding
import carnerero.agustin.cuentaappandroid.model.Cuenta
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListOfAccountsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListOfAccountsFragment : Fragment(),OnLocaleListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var lang:String?=null
    private var country:String?=null
    private var dni:String?=null
    private lateinit var sharedPreferences: SharedPreferences
    // Adaptador y vista para la lista de movimientos
    private lateinit var adapterCuentas: AdapterBal
    private lateinit var recyclerView: RecyclerView
    // Instancia de la base de datos
    private val admin = DataBaseAppSingleton.getInstance(context)
    private val cuentaDao= CuentaDao(admin)
    private var cuentas:ArrayList<Cuenta> =ArrayList<Cuenta>()
    // View Binding para acceder a las vistas de diseño
    private var _binding:FragmentListOfAccountsBinding? = null
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
        _binding = FragmentListOfAccountsBinding.inflate(inflater, container, false)
        // Obtener preferencias compartidas
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        lang = sharedPreferences.getString(getString(R.string.lang), "es")
        country = sharedPreferences.getString(getString(R.string.country), "ES")
        dni = sharedPreferences.getString(getString(R.string.id), null)
        cuentas= dni?.let { cuentaDao.listarCuentasPorDNI(it) } as ArrayList<Cuenta>
        recyclerView = binding.rvCuentas
        adapterCuentas=cuentas?.let { AdapterBal(it,this) }!!
        recyclerView.apply {
            this.layoutManager= LinearLayoutManager(context)
            adapter=adapterCuentas
        }


        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListOfAccountsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListOfAccountsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun getLocale(): Locale = Locale(lang.toString(),country.toString())

}
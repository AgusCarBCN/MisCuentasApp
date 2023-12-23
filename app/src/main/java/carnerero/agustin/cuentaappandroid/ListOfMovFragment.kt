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
import carnerero.agustin.cuentaappandroid.interfaces.OnLocaleListener
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario
import java.util.Locale




class ListOfMovFragment : Fragment() , OnLocaleListener {

    private lateinit var lang:String
    private lateinit var country:String
    private lateinit var currency:String
    private lateinit var conversionRate:String
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
        currency=sharedPreferences.getString(getString(R.string.basecurrency), null)?:"EUR"
        when(currency){
            "EUR"->{
                lang=sharedPreferences.getString(getString(R.string.lang), null)?:"es"
                country=sharedPreferences.getString(getString(R.string.country), null)?:"ES"
            }
            "USD"->{
                lang=sharedPreferences.getString(getString(R.string.lang), null)?:"en"
                country=sharedPreferences.getString(getString(R.string.country), null)?:"US"
            }else->{
                lang=sharedPreferences.getString(getString(R.string.lang), null)?:"en"
                country=sharedPreferences.getString(getString(R.string.country), null)?:"GB"
            }
        }
        lang=sharedPreferences.getString(getString(R.string.lang), null)?:"es"
        country=sharedPreferences.getString(getString(R.string.country), null)?:"ES"
        conversionRate = sharedPreferences.getString(getString(R.string.conversion_rate), "1.0") ?: "1.0"
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



    override fun getLocale(): Locale = Locale(lang,country)
    override fun getConversionRate(): Double =conversionRate.toDouble()

}

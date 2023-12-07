package carnerero.agustin.cuentaappandroid

import android.content.Context
import android.content.SharedPreferences
import android.icu.text.NumberFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.dao.CuentaDao
import carnerero.agustin.cuentaappandroid.databinding.FragmentSaldoBinding
import carnerero.agustin.cuentaappandroid.model.Cuenta
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SaldoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SaldoFragment : Fragment() {

    // Parámetros de la instancia (puedes cambiarlos según tus necesidades)
    private var param1: String? = null
    private var param2: String? = null

    // View Binding para acceder a los componentes de la interfaz de usuario
    private var _binding: FragmentSaldoBinding? = null
    private val binding get() = _binding!!

    // Instancia de la base de datos
    private val admin = DataBaseAppSingleton.getInstance(context)

    // SharedPreferences para almacenar y recuperar datos de forma sencilla
    private lateinit var sharedPreferences: SharedPreferences

    // Método llamado cuando se crea el fragmento
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recuperar los argumentos proporcionados al crear la instancia del fragmento
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    // Método llamado para crear la vista del fragmento
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflar el diseño de fragment_saldo.xml utilizando View Binding
        _binding = FragmentSaldoBinding.inflate(inflater, container, false)
        val view = binding.root

        // Recuperar valores de SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val dni = sharedPreferences.getString(getString(R.string.id), null)
        val lang = sharedPreferences.getString(getString(R.string.lang), "es")
        val country = sharedPreferences.getString(getString(R.string.country), "ES")

        // Instancia de CuentaDao para acceder a la base de datos y obtener la información de las cuentas
        val dao = CuentaDao(admin)
        val cuentas: List<Cuenta>? = dni?.let { dao.listarCuentasPorDNI(it) }

        // Referencias a los TextView en el diseño
        val cuenta1 = binding.tvCuenta
        val cuenta2 = binding.tvCuenta2
        val saldo1 = binding.tvSaldo
        val saldo2 = binding.tvSaldo2

        // Establecer la Locale para el formato de moneda
        val euroLocale = Locale(lang!!, country!!)
        val currencyFormat = NumberFormat.getCurrencyInstance(euroLocale)

        // Mostrar información de las cuentas y saldos en los TextView
        cuenta1.text = cuentas?.get(0)?.iban
        cuenta2.text = cuentas?.get(1)?.iban

        // Configurar el formato del saldo y el color del texto en función del valor
        saldo1.apply {
            text = cuentas?.get(0)?.let { currencyFormat.format(it.saldo).toString() }
            setTextColor(ContextCompat.getColor(context, R.color.darkGreenPistacho))
        }
        saldo2.apply {
            text = cuentas?.get(1)?.let { currencyFormat.format(it.saldo).toString() }
            setTextColor(ContextCompat.getColor(context, R.color.darkGreenPistacho))
        }

        return view
    }

    // Método llamado cuando el fragmento es destruido
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Importante para evitar fugas de memoria
    }

    // Método companion utilizado para crear una nueva instancia del fragmento
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SaldoFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SaldoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

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
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentSaldoBinding?=null
    private val admin=DataBaseAppSingleton.getInstance(context)
    private lateinit var sharedPreferences: SharedPreferences
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
        _binding= FragmentSaldoBinding.inflate(inflater,container,false)
        val view = binding.root
        // Inflate the layout for this fragment
        //Recupero dni del usuario que inicio sesion
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(requireContext())
        val dni=sharedPreferences.getString(getString(R.string.id),null)
        val dao= CuentaDao(admin)
        val cuentas: List<Cuenta>? = dni?.let { dao.listarCuentasPorDNI(it) }
        val cuenta1=binding.tvCuenta1
        val cuenta2=binding.tvCuenta2
        val saldo1=binding.tvSaldo1
        val saldo2=binding.tvSaldo2
        val euroLocale = Locale("es", "ES") // Establecer la Locale a español/españa para formato en euros
        val currencyFormat = NumberFormat.getCurrencyInstance(euroLocale)
        cuenta1.text = cuentas?.get(0)?.iban
        cuenta2.text = cuentas?.get(1)?.iban
        saldo1.apply {
            text= cuentas?.get(0)?.let { currencyFormat.format(it.saldo).toString() }
            setTextColor(ContextCompat.getColor(context, R.color.darkGreenPistacho))
        }
        saldo2.apply {
            text= cuentas?.get(1)?.let { currencyFormat.format(it.saldo).toString() }
            setTextColor(ContextCompat.getColor(context, R.color.darkGreenPistacho))
        }
        return view
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
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
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
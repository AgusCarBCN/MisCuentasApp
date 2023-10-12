package carnerero.agustin.cuentaappandroid

import android.content.Intent
import android.icu.text.NumberFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentContainerView
import carnerero.agustin.cuentaappandroid.dao.CuentaDao
import carnerero.agustin.cuentaappandroid.model.Cuenta
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        val rootview= inflater.inflate(R.layout.fragment_home, container, false)

        val admin=DataBaseApp(context,"cuentaApp",null,1)
        val dao:CuentaDao= CuentaDao(admin)
        val intent:Intent
        val cuentas:List<Cuenta> =dao.listarTodasLasCuentas()
        val cuenta1:TextView=rootview.findViewById(R.id.tv_cuenta1)
        val cuenta2:TextView=rootview.findViewById(R.id.tv_cuenta2)
        val saldo1:TextView=rootview.findViewById(R.id.tv_saldo1)
        val saldo2:TextView=rootview.findViewById(R.id.tv_saldo2)

        val euroLocale = Locale("es", "ES") // Establecer la Locale a español/españa para formato en euros
        val currencyFormat = NumberFormat.getCurrencyInstance(euroLocale)

        cuenta1.setText(cuentas.get(0).iban.toString())
        cuenta2.setText(cuentas.get(1).iban.toString())
        saldo1.setText(currencyFormat.format(cuentas.get(0).saldo).toString())
        saldo2.setText(currencyFormat.format(cuentas.get(1).saldo).toString())
        return rootview
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
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}
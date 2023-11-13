package carnerero.agustin.cuentaappandroid

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.databinding.FragmentAjustesBinding
import carnerero.agustin.cuentaappandroid.databinding.FragmentNewAmountBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AjustesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AjustesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentAjustesBinding?=null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
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
        _binding= FragmentAjustesBinding.inflate(inflater,container,false)
        val view = binding.root
        val switch= binding.switchdark
        //Recupero dni del usuario que inicio sesion
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(requireContext())
        // Obtiene el estado actual del modo oscuro desde SharedPreferences
        val enableDarkTheme = sharedPreferences.getBoolean(getString(R.string.preferences_enable), false)
        // Establece el estado inicial del Switch
        switch.isChecked = enableDarkTheme
        applyTheme(enableDarkTheme)
        switch.setOnCheckedChangeListener { _, isChecked ->

            applyTheme(isChecked)
        }

        return view
    }
    override fun onPause() {
        super.onPause()
        // Guarda el estado del Switch en SharedPreferences cuando la actividad se pausa
        val switch: SwitchCompat = binding.switchdark
        sharedPreferences.edit().putBoolean(getString(R.string.preferences_enable), switch.isChecked).apply()
    }
    fun applyTheme(enableDarkTheme: Boolean) {
        if (enableDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AjustesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AjustesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
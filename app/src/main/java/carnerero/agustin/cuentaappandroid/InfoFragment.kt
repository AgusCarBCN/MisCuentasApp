package carnerero.agustin.cuentaappandroid

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.dao.UsuarioDao
import carnerero.agustin.cuentaappandroid.databinding.FragmentCalculatorBinding
import carnerero.agustin.cuentaappandroid.databinding.FragmentInfoBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InfoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var sharedPreferences: SharedPreferences
    private val admin = DataBaseAppSingleton.getInstance(context)
    private val userDao=UsuarioDao(admin)
    // Variable para manejar el View Binding
    private var _binding: FragmentInfoBinding? = null
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
        _binding = FragmentInfoBinding.inflate(inflater, container, false)

        val view = binding.root
        // Obtener el nombre del usuario almacenado en SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val dni = sharedPreferences.getString(getString(R.string.id), null)!!
        val pass = sharedPreferences.getString(getString(R.string.password), null)!!
        val user= userDao.obtenerUsuarioPorDniYPassword(dni,pass)
        with(binding){
            val etDni=etdni.setText(user?.dni)
            val etName=etname.setText(user?.nombre)
            val etAddress=etaddress.setText(user?.domicilio)
            val etCity=etcity.setText(user?.ciudad)
            val etEmail=etemail.setText(user?.email)
            val etPass=etpass.setText(user?.password)
        }




       return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
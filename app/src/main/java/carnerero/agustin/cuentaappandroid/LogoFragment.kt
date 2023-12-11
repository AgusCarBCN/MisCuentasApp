package carnerero.agustin.cuentaappandroid

import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.databinding.FragmentHomeBinding



private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class LogoFragment : Fragment() {

    // Parámetros opcionales
    private var param1: String? = null
    private var param2: String? = null

    // View Binding para acceder a las vistas de diseño
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // SharedPreferences para almacenar y recuperar datos de configuración
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
        // Inflar el diseño del fragmento y asignarlo a _binding
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        // Obtener el nombre del usuario almacenado en SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val name = sharedPreferences.getString(getString(R.string.name), null)
        val img=sharedPreferences.getString(getString(R.string.img_photo),null)
        if(img!=null) {
            val imgUri = Uri.parse(img)
            val picture = binding.imgPhoto
            picture.setImageURI(imgUri)
        }
        // Mostrar un saludo personalizado en el TextView
        val wellcome = binding.tvWellcome
        val msg = "${wellcome.text} $name"
        wellcome.text = msg

        return view
    }

    companion object {
        // Método factory para crear una nueva instancia de LogoFragment
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LogoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

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


class LogoFragment : Fragment() {


    // View Binding para acceder a las vistas de diseño
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // SharedPreferences para almacenar y recuperar datos de configuración
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflar el diseño del fragmento y asignarlo a _binding
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        // Obtener el nombre del usuario almacenado en SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val name = sharedPreferences.getString(getString(R.string.username), null)
        val img=sharedPreferences.getString(getString(R.string.img_photo),null)



        if(img!=null) {
            val imgUri = Uri.parse(img)
            val picture = binding.imgProfile
            picture.setImageURI(imgUri)
        }
        // Mostrar un saludo personalizado en el TextView
        val wellcome = binding.tvWellcome
        val msg = "${wellcome.text} $name"
        wellcome.text = msg

        return view
    }


}

package carnerero.agustin.cuentaappandroid

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.dao.UsuarioDao
import carnerero.agustin.cuentaappandroid.databinding.FragmentInfoBinding
import carnerero.agustin.cuentaappandroid.utils.Utils

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
    private lateinit var dni:String
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
         dni = sharedPreferences.getString(getString(R.string.id), null)!!
        val pass = sharedPreferences.getString(getString(R.string.password), null)!!
        val user = userDao.obtenerUsuarioPorDniYPassword(dni, pass)

        //Iniciamos textView con la informacion del usuario
        with(binding){
            tvdni.text = user?.dni
            tvname.text=user?.nombre
            tvaddress.text=user?.domicilio
            tvcity.text=user?.ciudad
            tvzip.text=user?.codigoPostal
            tvEmail.text=user?.email
            tvpass.text=user?.password
        }


        // Definir listas de elementos de la interfaz de usuario
        val imgList = listOf(binding.imgid, binding.imgname, binding.imgemail, binding.imgaddress, binding.imgzip,binding.imgcity, binding.imgpass)
        val titleList = listOf(getString(R.string.id), getString(R.string.name), getString(R.string.email),getString(R.string.address), getString(R.string.zipcode), getString(R.string.city), getString(R.string.password))
        var textViewList = listOf(binding.tvdni, binding.tvname, binding.tvEmail, binding.tvaddress,binding.tvzip, binding.tvcity,binding.tvpass)
        val columnsDataBase=listOf(AppConst.DNI,AppConst.NAME,AppConst.EMAIL,AppConst.ADDRESS,AppConst.ZIP,AppConst.CITY,AppConst.PASSWORD)
        // Iterar sobre los elementos de imgList
        for (i in imgList.indices) {

            // Verificar si el tema es oscuro y cambiar el color del ícono
            if (Utils.isDarkTheme) {
                changeIconColor(imgList[i])
            }

            // Asignar un OnClickListener a cada elemento de imgList
            imgList[i].setOnClickListener {
                // Llamar a la función changeField con el TextView correspondiente y el título correspondiente
                 changeField(textViewList[i], titleList[i],columnsDataBase[i])


            }
        }


        return view

    }
    private fun changeField(textView:TextView,title:String,column:String) {
        val builder = AlertDialog.Builder(context,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background)
        builder.setTitle("${getString(R.string.change)} ${title}")
        val editText = EditText(context)

        builder.setView(editText)
        builder.setPositiveButton("Aceptar") { _, _ ->
            // Aquí obtienes el nuevo nombre desde el EditText
            val newValue=editText.text.toString()
            textView.text=newValue
            userDao.updateUserField(dni,column,newValue)
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()

        }
        val dialog = builder.create()
        dialog.show()

    }
    private fun changeIconColor(img :ImageView){
        img.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white))
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
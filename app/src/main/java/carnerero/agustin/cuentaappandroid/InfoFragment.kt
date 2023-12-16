package carnerero.agustin.cuentaappandroid




import android.app.AlertDialog
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.dao.UsuarioDao
import carnerero.agustin.cuentaappandroid.databinding.FragmentInfoBinding
import carnerero.agustin.cuentaappandroid.utils.Utils
import java.io.File
import java.io.FileOutputStream

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
    private lateinit var imgPicture: ImageView
    private val admin = DataBaseAppSingleton.getInstance(context)
    private val userDao=UsuarioDao(admin)
    private lateinit var dni:String

    val pickMedia=registerForActivityResult(ActivityResultContracts.PickVisualMedia()){uri->
        if (uri != null) {
            // Guardar la imagen en la memoria externa
            val imagePath = saveImageToExternalStorage(uri)
            // Verificar si la imagen se guardó correctamente
            if (imagePath != null) {
                // Guardar la ruta del archivo en las preferencias compartidas
                sharedPreferences.edit().putString(getString(R.string.img_photo), imagePath).apply()
                // Mostrar la imagen en tu ImageView
                imgPicture.setImageURI(uri)
            } else {
                // Manejar el caso en el que no se pudo guardar la imagen
            }
        } else {
            // Manejar el caso en el que la URI es nula
        }
    }
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
    ): View {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        val view = binding.root
        // Obtener el nombre del usuario almacenado en SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
         dni = sharedPreferences.getString(getString(R.string.userdni), null)!!
        val pass = sharedPreferences.getString(getString(R.string.userpass), null)!!
        val imgStr=sharedPreferences.getString(getString(R.string.img_photo),"")
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
        val imgIconCamera=binding.imgiconcamera
        //Cargar y mostrar imagen

        imgPicture=binding.imgPhoto
        imgPicture.setImageURI(Uri.parse(imgStr))

        val imgList = listOf(binding.imgid, binding.imgname, binding.imgemail, binding.imgaddress, binding.imgzip,binding.imgcity, binding.imgpass)
        val titleList = listOf(getString(R.string.id), getString(R.string.name), getString(R.string.email),getString(R.string.address), getString(R.string.zipcode), getString(R.string.city), getString(R.string.password))
        val textViewList = listOf(binding.tvdni, binding.tvname, binding.tvEmail, binding.tvaddress,binding.tvzip, binding.tvcity,binding.tvpass)
        val columnsDataBase=listOf(AppConst.DNI,AppConst.NAME,AppConst.EMAIL,AppConst.ADDRESS,AppConst.ZIP,AppConst.CITY,AppConst.PASSWORD)
        // Iterar sobre los elementos de imgList
        for (i in imgList.indices) {

            // Verificar si el tema es oscuro y cambiar el color del ícono
            if (Utils.isDarkTheme) {
                changeIconColor(imgList[i])
                changeIconColor(imgIconCamera)

            }

            // Asignar un OnClickListener a cada elemento de imgList
            imgList[i].setOnClickListener {
                // Llamar a la función changeField con el TextView correspondiente y el título correspondiente
                 changeField(textViewList[i], titleList[i],columnsDataBase[i])
            }
        }



        imgIconCamera.setOnClickListener {
            if (ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable()) {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

            }
        }

        return view

    }



    private fun saveImageToExternalStorage(uri: Uri): String? {
        try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val file = File(requireContext().externalCacheDir, "image.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()

            return null
        }
    }
    private fun changeField(textView: TextView, title: String, column: String) {
        val builder = AlertDialog.Builder(context)

        // Inflar el diseño personalizado
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.custom_dialog_one_field, null)
        var msgHint=""
        // Obtener referencias a las vistas dentro del diseño personalizado
        val dialogTitle = dialogView.findViewById<TextView>(R.id.tv_dialogtitle)
        val editText = dialogView.findViewById<EditText>(R.id.et_dialoginfo)
        val confirmButton = dialogView.findViewById<Button>(R.id.btn_dialogconfirm)
        val cancelButton = dialogView.findViewById<Button>(R.id.btn_dialogcancel)
        if(title.equals(getString(R.string.city))||title.equals(getString(R.string.address))
            ||title.equals(getString(R.string.password))){
            msgHint="${getString(R.string.newfieldF)} ${title}"
            }else{
        msgHint="${getString(R.string.newfield)} ${title}"}
        // Configurar el contenido del AlertDialog con el diseño personalizado
        builder.setView(dialogView)


        // Configurar propiedades específicas del diseño
        dialogTitle.text = "${getString(R.string.change)} $title"
        editText.hint = msgHint

        // Crear el AlertDialog antes de usarlo para poder cerrarlo más adelante
        val dialog = builder.create()

        // Configurar el evento de clic para el botón personalizado de confirmar
        confirmButton.setOnClickListener {
            val newValue = editText.text.toString()
            textView.text = newValue
            userDao.updateUserField(dni, column, newValue)
            // Cerrar el AlertDialog
            dialog.dismiss()
        }

        // Configurar el evento de clic para el botón personalizado de cancelar
        cancelButton.setOnClickListener {
            // Realizar las acciones deseadas al hacer clic en el botón personalizado de cancelar
            dialog.cancel()
        }
// Configurar el fondo transparente
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
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
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
import androidx.appcompat.widget.TooltipCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.databinding.FragmentInfoBinding
import carnerero.agustin.cuentaappandroid.utils.Utils
import java.io.File
import java.io.FileOutputStream


class InfoFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var imgPicture: ImageView
    private val pickMedia=registerForActivityResult(ActivityResultContracts.PickVisualMedia()){uri->
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
        val login = sharedPreferences.getString(getString(R.string.userlogin), null)!!
        val name = sharedPreferences.getString(getString(R.string.username), null)!!
        val pass = sharedPreferences.getString(getString(R.string.userpass), null)!!
        val imgStr=sharedPreferences.getString(getString(R.string.img_photo),"")

        //Iniciamos textView con la informacion del usuario
        with(binding){
            tvlogin.text=login
            tvname.text=name
            tvpass.text=pass
        }
        // Definir listas de elementos de la interfaz de usuario
        val imgIconCamera=binding.imgIconcameraprofile
        //Cargar y mostrar imagen
        imgPicture=binding.imgProfile
        imgPicture.setImageURI(Uri.parse(imgStr))
        val lyList = listOf(binding.lyid, binding.lyname, binding.lypass)
        val imgList = listOf(binding.imgid, binding.imgname, binding.imgpass)
        val editIcons= listOf(binding.editid,binding.editname,binding.editpass)
        val titleList =
            listOf(getString(R.string.userName), getString(R.string.name), getString(R.string.password))
        val textViewList = listOf(binding.tvlogin, binding.tvname, binding.tvpass)
        val saveValueList: List<String> = listOf(
            getString(R.string.userlogin),
            getString(R.string.username),
            getString(R.string.userpass)
        )
        for (i in lyList.indices) {
            // Verificar si el tema es oscuro y cambiar el color del ícono
            if (Utils.isDarkTheme) {
                changeIconColor(imgList[i])
                changeIconColor(editIcons[i])

            }
            lyList[i].setOnClickListener {
                // Llamar a la función changeField con el TextView correspondiente y el título correspondiente
                changeField(textViewList[i], titleList[i], saveValueList[i])
            }
        }

        // Definir el tooltip para la cámara
        TooltipCompat.setTooltipText(imgIconCamera, "Cambiar foto de perfil")
        imgIconCamera.setOnClickListener {
            if (ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable(requireContext())) {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }
        return view
    }
    private fun saveImageToExternalStorage(uri: Uri): String? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val file = File(requireContext().externalCacheDir, "image.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()

            null
        }
    }

    private fun changeField(textView: TextView, title: String, saveValue: String) {
        val builder = AlertDialog.Builder(context)

        // Inflar el diseño personalizado
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.custom_dialog_one_field, null)

        // Obtener referencias a las vistas dentro del diseño personalizado
        val dialogTitle = dialogView.findViewById<TextView>(R.id.tv_dialogtitle)
        val editText = dialogView.findViewById<EditText>(R.id.et_dialoginfo)
        val confirmButton = dialogView.findViewById<Button>(R.id.btn_dialogconfirm)
        val cancelButton = dialogView.findViewById<Button>(R.id.btn_dialogcancel)
        val msgHint = if(title == getString(R.string.password)
        ){
            "${getString(R.string.newfieldF)} $title"
        }else{
            "${getString(R.string.newfield)} $title"
        }
        // Configurar el contenido del AlertDialog con el diseño personalizado
        builder.setView(dialogView)

        val titleDialog="${getString(R.string.change)} $title"
        // Configurar propiedades específicas del diseño
        dialogTitle.text = titleDialog
        editText.hint = msgHint
        // Crear el AlertDialog antes de usarlo para poder cerrarlo más adelante
        val dialog = builder.create()
        // Configurar el evento de clic para el botón personalizado de confirmar
        confirmButton.setOnClickListener {
                if(editText.text.toString().isEmpty()) {
                    editText.error = getString(R.string.Noblankallowed)

            }else {
                    textView.text = editText.text.toString()
                    sharedPreferences.edit().putString(
                        saveValue,
                        textView.text.toString()
                    ).apply()
                // Cerrar el AlertDialog
                dialog.dismiss()
                }
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


}
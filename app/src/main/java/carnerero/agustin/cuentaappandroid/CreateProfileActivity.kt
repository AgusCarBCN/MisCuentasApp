package carnerero.agustin.cuentaappandroid

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.TooltipCompat
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.databinding.ActivityCreateProfileBinding
import carnerero.agustin.cuentaappandroid.utils.Utils


class CreateProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateProfileBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var imgPicture: ImageView

    private val pickMedia =registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri->
        if (uri != null) {
            // Guardar la imagen en la memoria externa
            val imagePath = Utils.saveImageToExternalStorage(this,uri)
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        //Acceso a componenentes de la activity
        // Definir listas de elementos de la interfaz de usuario
        val imgIconCamera = binding.imgIconcameraprofile
        val etName = binding.etNameprofile
        val etUserName = binding.etUsernameprofile
        val etPassword = binding.etPasswordprofile
        val etRepeatPassword = binding.etReppasswordprofile
        val btnCreateAccount = binding.btnCreateAccounts
        val btnGoBack = binding.btnBacktoOnBoarding
        val enableDarkTheme = sharedPreferences.getBoolean(getString(R.string.preferences_enable), false)
        val enableEnLang = sharedPreferences.getBoolean(getString(R.string.preferences_enable_lang), Utils.getDefaultLang())
        // Aplicar tema y configuración de idioma según las preferencias
        Utils.applyTheme(enableDarkTheme)
        Utils.applyLanguage(enableEnLang)
        val imgStr=sharedPreferences.getString(getString(R.string.img_photo),"")


        //Cargar y mostrar imagen
        imgPicture=binding.imgProfile
        imgPicture.setImageURI(Uri.parse(imgStr))
        // Definir el tooltip para la cámara
        TooltipCompat.setTooltipText(imgIconCamera, "Cambiar foto de perfil")
        imgIconCamera.setOnClickListener {
            if (ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable(this)) {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }
        btnCreateAccount.setOnClickListener {
            val intent = Intent(this, CreateAccountsActivity::class.java)
            //Verifica si uno de los campos requeridos esta vacio y si lo esta muestra un mensaje de advertencia
            if (etName.text.toString().isEmpty() || etUserName.text.toString()
                    .isEmpty() || etPassword.text.toString()
                    .isEmpty() || etRepeatPassword.text.toString().isEmpty() || etPassword.text.toString()!=etRepeatPassword.text.toString()
            ) {
                if (etName.text.toString().isEmpty())
                    etName.error = getString(R.string.required)
                if (etUserName.text.toString().isEmpty())
                    etUserName.error = getString(R.string.required)
                if (etPassword.text.toString().isEmpty())
                    etPassword.error = getString(R.string.required)
                if (etRepeatPassword.text.toString().isEmpty())
                    etRepeatPassword.error = getString(R.string.required)
                //Verifica si las contraseñas son iguales
                if(etPassword.text.toString()!=etRepeatPassword.text.toString()){
                    etRepeatPassword.error=getString(R.string.reppassword)

                }
                //Si se pasan los criterios de validacion se almacenan los valores en sharepreferences
            } else {
                //Guadar moneda en sharepreferences
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
                sharedPreferences.edit().putString(
                    getString(R.string.username),
                    etName.text.toString()
                ).apply()
                sharedPreferences.edit().putString(
                    getString(R.string.userlogin),
                    etUserName.text.toString()
                ).apply()
                sharedPreferences.edit().putString(
                    getString(R.string.userpass),
                    etPassword.text.toString()
                ).apply()
                // Iniciar la actividad CreateAccountActivity
                startActivity(intent)
            }
        }
       btnGoBack.setOnClickListener {
           val intent = Intent(this, OnBoardingActivity::class.java)
           startActivity(intent)
       }
    }


}
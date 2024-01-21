package carnerero.agustin.cuentaappandroid


import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.dao.CuentaDao
import carnerero.agustin.cuentaappandroid.databinding.ActivityCreateAccountsBinding
import carnerero.agustin.cuentaappandroid.model.Cuenta
import carnerero.agustin.cuentaappandroid.utils.Utils

class CreateAccountsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAccountsBinding

    private val admin = DataBaseAppSingleton.getInstance(this)
    private val cuentaDao=CuentaDao(admin)
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var currency:String
    private lateinit var lang:String
    private lateinit var country:String
    private val currencies = arrayOf(
        "EUR", "USD", "GBP"
    )
    private lateinit var selectedItem:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Acceso a componentes de la interfaz
        val etAccountName=binding.etNameaccount
        val etBalance=binding.etBalance
        val selectCurrencies=binding.spChoosecurreny
        val btnAddAccount=binding.btnAddaccount
        val btnLogin=binding.btnTologin
        val btnGoBack=binding.btnBacktoCreateProfile
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val enableDarkTheme = sharedPreferences.getBoolean(getString(R.string.preferences_enable), false)
        val enableEnLang = sharedPreferences.getBoolean(getString(R.string.preferences_enable_lang), Utils.getDefaultLang())
        // Aplicar tema y configuración de idioma según las preferencias
        Utils.applyTheme(enableDarkTheme)
        Utils.applyLanguage(enableEnLang)
        // Crear adaptadores
        val adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item)
        with(adapter) {
            currencies.forEach { currency ->
                add(currency)
            }
        }
        selectCurrencies.adapter = adapter
        selectedItem = currencies[0]
        // Listener para el spinner que guarda la divisa seleccionada
        selectCurrencies.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedItem = adapter.getItem(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Acciones a realizar cuando no se selecciona nada
            }
        }
        currency = selectedItem

        btnAddAccount.setOnClickListener {
            if(etAccountName.text.isNullOrEmpty()||etBalance.text.isNullOrEmpty()){
                if(etAccountName.text.isNullOrEmpty()) {
                    etAccountName.error = getString(R.string.msgemptyfield)
                }
                if(etBalance.text.isNullOrEmpty()){
                etBalance.error=getString(R.string.msgemptyfield)}
                Toast.makeText(this, getString(carnerero.agustin.cuentaappandroid.R.string.msgemptiesfield), Toast.LENGTH_LONG)
                    .show()
            }else {
                cuentaDao.insertarCuenta(Cuenta(
                    etAccountName.text.toString(),
                    etBalance.text.toString().toDouble()
                ))
                etAccountName.text.clear()
                etBalance.text.clear()
            }
        }
        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            sharedPreferences.edit().putString(
                getString(carnerero.agustin.cuentaappandroid.R.string.basecurrency),
                currency
            ).apply()
           if(enableEnLang){
               lang="en"
           }else{
               lang="es"
           }
            when(selectedItem){
                "EUR"->{
                    //lang="es"
                    country="ES"
                }
                "USD"->{
                    //lang="en"
                    country="US"
                }else->{
                //lang="en"
                country="GB"
            }
            }
            //Guardar configuraciones de idioma y pais en funcion de la moneda seleccionada
            sharedPreferences.edit().putString(
                getString(carnerero.agustin.cuentaappandroid.R.string.lang),
                lang
            ).apply()
            sharedPreferences.edit().putString(
                getString(carnerero.agustin.cuentaappandroid.R.string.country),
                country
            ).apply()
        }
        btnGoBack.setOnClickListener {
            val intent = Intent(this, CreateProfileActivity::class.java)
            startActivity(intent)
        }
    }
}
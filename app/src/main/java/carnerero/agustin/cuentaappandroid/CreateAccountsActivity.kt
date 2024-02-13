package carnerero.agustin.cuentaappandroid


import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.databinding.adapters.AdapterViewBindingAdapter
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.adapter.CustomAdapter
import carnerero.agustin.cuentaappandroid.dao.CuentaDao
import carnerero.agustin.cuentaappandroid.databinding.ActivityCreateAccountsBinding
import carnerero.agustin.cuentaappandroid.model.Cuenta
import carnerero.agustin.cuentaappandroid.model.CurrencyItem
import carnerero.agustin.cuentaappandroid.utils.Utils

class CreateAccountsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAccountsBinding

    private val admin = DataBaseAppSingleton.getInstance(this)
    private val cuentaDao=CuentaDao(admin)
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var lang:String
    private lateinit var country:String
    private val currenciesList=arrayOf(CurrencyItem(R.drawable.ue,"EUR"),
        CurrencyItem(R.drawable.usa,"USD"),
        CurrencyItem(R.drawable.uk,"GBP"),
        CurrencyItem(R.drawable.india,"INR"))


    private lateinit var selectedItem:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Acceso a componentes de la interfaz

        val etAccountName = binding.etNameaccount
        val etBalance = binding.etBalance
        val selectCurrencies = binding.spChoosecurreny
        val btnAddAccount = binding.btnAddaccount
        val btnLogin = binding.btnTologin
        val btnGoBack = binding.btnBacktoCreateProfile
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val enableDarkTheme =
            sharedPreferences.getBoolean(getString(R.string.preferences_enable), false)
        val enableEnLang = sharedPreferences.getBoolean(
            getString(R.string.preferences_enable_lang),
            Utils.getDefaultLang()
        )
        // Aplicar tema y configuración de idioma según las preferencias
        Utils.applyTheme(enableDarkTheme)
        Utils.applyLanguage(enableEnLang)
        val adapter = CustomAdapter(this, currenciesList)
        selectCurrencies.adapter = adapter

        selectCurrencies.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedItem = currenciesList[position].currencySymbol
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedItem=currenciesList[0].currencySymbol
            }
        }




        btnAddAccount.setOnClickListener {
            if(etAccountName.text.isNullOrEmpty()||etBalance.text.isNullOrEmpty()){
                if(etAccountName.text.isNullOrEmpty()) {
                    etAccountName.error = getString(R.string.msgemptyfield)
                }
                if(etBalance.text.isNullOrEmpty()){
                etBalance.error=getString(R.string.msgemptyfield)}

            }else {
                cuentaDao.insertarCuenta(Cuenta(
                    etAccountName.text.toString(),
                    etBalance.text.toString().toDouble()
                ))
                etAccountName.text?.clear()
                etBalance.text?.clear()
            }
        }
        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

             when(selectedItem){
                "EUR"->{
                    lang="es"
                    country="ES"
                }
                "USD"->{
                    lang="en"
                    country="US"
                }
                "INR"->{
                    lang="en"
                    country="IN"
                }
                else->{
                    lang="en"
                    country="GB"
                }
            }
            //Guardar configuraciones de idioma y pais en funcion de la moneda seleccionada
            sharedPreferences.edit().putString(
                getString(carnerero.agustin.cuentaappandroid.R.string.basecurrency),
                selectedItem
            ).apply()
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
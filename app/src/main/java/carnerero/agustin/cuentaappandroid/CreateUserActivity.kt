package carnerero.agustin.cuentaappandroid


import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.dao.CuentaDao
import carnerero.agustin.cuentaappandroid.dao.MovimientoBancarioDAO
import carnerero.agustin.cuentaappandroid.dao.UsuarioDao
import carnerero.agustin.cuentaappandroid.databinding.ActivityCreateUserBinding
import carnerero.agustin.cuentaappandroid.model.Cuenta
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario
import carnerero.agustin.cuentaappandroid.model.Usuario
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.BufferedReader

class CreateUserActivity : AppCompatActivity() {
    private lateinit var usuarioDao: UsuarioDao
    private lateinit var cuentaDao: CuentaDao
    private val admin = DataBaseAppSingleton.getInstance(this)
    private val movDAO = MovimientoBancarioDAO(admin)
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var currency:String
    private lateinit var lang:String
    private lateinit var country:String
    private lateinit var binding: ActivityCreateUserBinding
    private val cuentas:ArrayList<Cuenta> =ArrayList()
    private val currencies = arrayOf(
        "EUR", "USD", "GBP"
    )
    private lateinit var selectedItem:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Acceso a los EditText del Usuario
        val name = binding.etUsername
        val dni = binding.etUserdni
        val address = binding.etUseraddress
        val city = binding.etUsercity
        val zipCode = binding.etUserzip
        val email = binding.etUseremail
        val userpass = binding.etUserpass

        // Acceso a los EditText de las cuentas
        val account = binding.etUseraccount
        val amount = binding.etAmount
        //Acceso a buttonView
        val addAccount = binding.addaccount
        addAccount.tooltipText=getString(R.string.add_an_account)
        val confirm = binding.btnConfirmuser
        val cancel = binding.btnCanceluser
        //Acceso a spinner
        val spCurrency = binding.spSelectcurrency

        // Crear adaptadores
        val adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item)
        with(adapter) {
            currencies.forEach { currency ->
                add(currency)
            }
        }
        spCurrency.adapter = adapter
        selectedItem = currencies[0]
        // Listener para el spinner que guarda la divisa seleccionada
        spCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

        addAccount.setOnClickListener {
            if(account.text.isNullOrEmpty()||amount.text.isNullOrEmpty()){
                account.text.clear()
                amount.text.clear()
            }else {
                cuentas.add(
                    Cuenta(
                        account.text.toString(),
                        amount.text.toString().toDouble(),
                        dni.text.toString()
                    )
                )
            }
            account.text.clear()
            amount.text.clear()
        }
        confirm.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            if (dni.text.toString().isEmpty() || name.text.toString()
                    .isEmpty() || userpass.text.toString().isEmpty()
            ) {
                if (dni.text.toString().isEmpty())
                    dni.error = getString(R.string.required)
                if (name.text.toString().isEmpty())
                    name.error = getString(R.string.required)
                if (userpass.text.toString().isEmpty())
                    userpass.error = getString(R.string.required)
            } else {
                val user = Usuario(
                    dni.text.toString(),
                    name.text.toString(),
                    address.text.toString(),
                    city.text.toString(),
                    zipCode.text.toString(),
                    email.text.toString(),
                    userpass.text.toString()
                )
                usuarioDao = UsuarioDao(admin)
                cuentaDao = CuentaDao(admin)
                // Insertar el Usuario y las Cuentas en la base de datos
                usuarioDao.insertarUsuario(user)
                cuentas.forEach { cuenta ->
                    cuentaDao.insertarCuenta(cuenta)
                }
                currency = selectedItem
                //Guadar moneda en sharepreferences
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

                sharedPreferences.edit().putString(
                    getString(carnerero.agustin.cuentaappandroid.R.string.basecurrency),
                    currency
                ).apply()
                when(selectedItem){
                    "EUR"->{
                    lang="es"
                    country="ES"
                    }
                    "USD"->{
                        lang="en"
                        country="US"
                    }else->{
                    lang="en"
                    country="GB"
                }
                }
                sharedPreferences.edit().putString(
                    getString(carnerero.agustin.cuentaappandroid.R.string.lang),
                    lang
                ).apply()
                sharedPreferences.edit().putString(
                    getString(carnerero.agustin.cuentaappandroid.R.string.country),
                    country
                ).apply()
                sharedPreferences.edit().putString(
                    getString(carnerero.agustin.cuentaappandroid.R.string.username),
                    name.text.toString()
                ).apply()
                // Leer el archivo CSV y agregar los movimientos bancarios a la base de datos
                val listMov = readFileCsv()
            for (element in listMov) {
                movDAO.nuevoImporte(element)
            }

                startActivity(intent)
            }
            cancel.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

        }
    }

    // Método para leer el archivo CSV y devolver una lista de MovimientoBancario
    private fun readFileCsv(): MutableList<MovimientoBancario> {
        val bufferedReader = BufferedReader(assets.open("movimientos.csv").reader())
        val csvParser = CSVParser.parse(bufferedReader, CSVFormat.DEFAULT)
        val list = mutableListOf<MovimientoBancario>()

        for (record in csvParser) {
            try {
                val importe = record.get(1).toDouble()
                val descripcion = record.get(2)
                val iban = record.get(3)
                val fechaImporte = record.get(4)

                // Crear objeto MovimientoBancario y agregarlo a la lista
                val movimientoBancario = MovimientoBancario(importe, descripcion, iban, fechaImporte)
                list.add(movimientoBancario)
            } catch (e: Exception) {
                // Manejar errores al analizar los datos CSV
                e.printStackTrace()
            }
        }

        return list
    }



}

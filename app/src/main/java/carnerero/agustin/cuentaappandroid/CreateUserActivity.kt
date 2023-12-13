package carnerero.agustin.cuentaappandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import carnerero.agustin.cuentaappandroid.dao.CuentaDao
import carnerero.agustin.cuentaappandroid.dao.MovimientoBancarioDAO
import carnerero.agustin.cuentaappandroid.dao.UsuarioDao
import carnerero.agustin.cuentaappandroid.databinding.ActivityCreateUserBinding

import carnerero.agustin.cuentaappandroid.model.Cuenta
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario
import carnerero.agustin.cuentaappandroid.model.Usuario
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.BufferedReader
import java.io.File
import java.io.FileWriter


class CreateUserActivity : AppCompatActivity() {
    private lateinit var usuarioDao: UsuarioDao
    private lateinit var cuentaDao: CuentaDao

    private val admin = DataBaseAppSingleton.getInstance(this)
    private val movDAO = MovimientoBancarioDAO(admin)
    private lateinit var binding: ActivityCreateUserBinding
    private val cuentas:ArrayList<Cuenta> =ArrayList<Cuenta>()
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
        val addAccount=binding.addaccount
        val confirm=binding.btnConfirmuser
        val cancel=binding.btnCanceluser


        addAccount.setOnClickListener {
            cuentas.add(Cuenta(account.text.toString(),amount.text.toString().toDouble(),dni.text.toString()))
            account.text.clear()
            amount.text.clear()
        }
        confirm.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
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
            // Leer el archivo CSV y agregar los movimientos bancarios a la base de datos
            val listMov = readFileCsv()
            for (element in listMov) {
                movDAO.nuevoImporte(element)
            }
            startActivity(intent)
        }
        cancel.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
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
    // Método para escribir una lista de MovimientoBancario a un archivo CSV


}

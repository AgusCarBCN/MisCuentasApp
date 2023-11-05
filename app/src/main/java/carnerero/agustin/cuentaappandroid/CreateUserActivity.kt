package carnerero.agustin.cuentaappandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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



class CreateUserActivity : AppCompatActivity() {
    private lateinit var usuarioDao: UsuarioDao
    private lateinit var cuentaDao: CuentaDao
    private val admin = DataBaseAppSingleton.getInstance(this)
    private val movDAO = MovimientoBancarioDAO(admin)
    private lateinit var binding:ActivityCreateUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCreateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    fun cancelCreateUser(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun confirmCreateUser(view: View) {

        val intent = Intent(this, LoginActivity::class.java)
        //Acceso a los editText de Usuario
        val name=binding.etUsername
        val dni=binding.etUserdni
        val address=binding.etUseraddress
        val city=binding.etUsercity
        val zipCode=binding.etUserzip
        val phone=binding.etUserphone
        val userpass=binding.etUserpass
        //Acceso a los ediText de las cuentas
        val mainAccount=binding.etUseraccmain
        val mainAmount=binding.etAmountaccmain
        val secondaryAccount=binding.etUseraccsecond
        val secondaryAmount=binding.etSecondccamount

        //Creo objetos Usuario y Cuenta
        val user = Usuario(
            dni.text.toString(),
            name.text.toString(),
            address.text.toString(),
            city.text.toString(),
            zipCode.text.toString(),
            phone.text.toString(),
            userpass.text.toString()
        )
        val cuenta1 = Cuenta(
            mainAccount.text.toString(),
            mainAmount.text.toString().toDouble(),
            dni.text.toString()
        )
        val cuenta2 = Cuenta(
            secondaryAccount.text.toString(),
            secondaryAmount.text.toString().toDouble(),
            dni.text.toString()
        )
        usuarioDao = UsuarioDao(admin)
        cuentaDao = CuentaDao(admin)
        usuarioDao.insertarUsuario(user)
        cuentaDao.insertarCuenta(cuenta1)
        cuentaDao.insertarCuenta(cuenta2)
        val listMov=readFileCsv()
        for(element in listMov){
            movDAO.nuevoImporte(element)
        }
        startActivity(intent)
    }

    private fun readFileCsv():MutableList<MovimientoBancario> {
        val bufferedReader = BufferedReader(assets.open("movimientos.csv").reader())
        val csvParser = CSVParser.parse(bufferedReader, CSVFormat.DEFAULT)
        val list = mutableListOf<MovimientoBancario>()
        //val formatoFecha = SimpleDateFormat("dd/MM/yyyy")
        for (record in csvParser) {
            try {
                //val id = record.get(0).toLong()
                val importe = record.get(1).toDouble()
                val descripcion = record.get(2)
                val iban = record.get(3)
                val fechaImporte = record.get(4)
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
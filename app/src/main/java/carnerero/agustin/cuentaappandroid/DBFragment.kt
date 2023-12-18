package carnerero.agustin.cuentaappandroid

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.dao.CuentaDao
import carnerero.agustin.cuentaappandroid.dao.MovimientoBancarioDAO
import carnerero.agustin.cuentaappandroid.databinding.FragmentDbBinding
import carnerero.agustin.cuentaappandroid.model.Cuenta
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario
import carnerero.agustin.cuentaappandroid.utils.Utils
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter



class DBFragment : Fragment(){

    private lateinit var sharedPreferences: SharedPreferences
    private val admin = DataBaseAppSingleton.getInstance(context)
    private val cuentaDao = CuentaDao(admin)
    private val movDAO = MovimientoBancarioDAO(admin)
    private lateinit var pickerExport: ActivityResultLauncher<Intent>
    private lateinit var pickerImport: ActivityResultLauncher<Intent>

    private lateinit var dni: String
    private lateinit var cuentas:ArrayList<Cuenta>

    private var _binding: FragmentDbBinding? = null
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
        _binding = FragmentDbBinding.inflate(inflater, container, false)
        val view = binding.root
        pickerExport = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.also { uri ->
                    val directory = DocumentFile.fromTreeUri(requireContext(), uri)
                    if (directory != null && directory.isDirectory) {
                        val dialog = createAlertDialogOneField(R.string.exportData, R.string.filename) { filename->
                            val records = movDAO.getAll()
                            writeCsvFile(records, requireContext(),"$filename.csv" , directory)
                        }
                        dialog.show()
                    }
                }
            }
        }

        pickerImport = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.also { uri ->
                    val selectedFile = DocumentFile.fromSingleUri(requireContext(), uri)
                    if (selectedFile != null && selectedFile.isFile) {

                        val listMov = readCsvFile(requireContext(),selectedFile.uri)
                        for (element in listMov) {
                            movDAO.nuevoImporte(element)
                        }

                        Toast.makeText(requireContext(),"importando: ${selectedFile.uri}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }


        // Obtener el nombre del usuario almacenado en SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        dni = sharedPreferences.getString(getString(R.string.userdni), null)!!
        cuentas= cuentaDao.listarCuentasPorDNI(dni) as ArrayList<Cuenta>

        val imgList = listOf(
            binding.imgaddaccount, binding.imgrename,
            binding.imgdeletedataaccount, binding.imgdeleteaccount, binding.imgdeleteAll,
            binding.imgexport, binding.imgimportfile
        )
        if (Utils.isDarkTheme) {
            for (img in imgList) {
                changeIconColor(img)
            }
        }
        imgList[0].setOnClickListener {
            insertAccount()
            // Actualizar el fragmento de saldo en la actividad principal
            (activity as MainActivity).actualizarFragmentSaldo()
        }
        imgList[1].setOnClickListener {
            changeIbanAccount()
            // Actualizar el fragmento de saldo en la actividad principal
            (activity as MainActivity).actualizarFragmentSaldo()
        }
        imgList[2].setOnClickListener {
            deleteAllMovInAccount()
            // Actualizar el fragmento de saldo en la actividad principal
            (activity as MainActivity).actualizarFragmentSaldo()
        }

        imgList[3].setOnClickListener {
            deleteAnAccount()
            // Actualizar el fragmento de saldo en la actividad principal
            (activity as MainActivity).actualizarFragmentSaldo()
        }
        imgList[4].setOnClickListener {
            deleteAllAccounts()
            // Actualizar el fragmento de saldo en la actividad principal
            (activity as MainActivity).actualizarFragmentSaldo()
        }
        imgList[5].setOnClickListener {
           exportFileCsv()
            // Actualizar el fragmento de saldo en la actividad principal
            (activity as MainActivity).actualizarFragmentSaldo()
        }
        imgList[6].setOnClickListener {
           importFileCsv()
            // Actualizar el fragmento de saldo en la actividad principal
            (activity as MainActivity).actualizarFragmentSaldo()
        }
        return view
    }

    private fun exportFileCsv() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        pickerExport.launch(intent)
    }
    private fun importFileCsv() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"  // Puedes ajustar el tipo de archivo permitido si es necesario
        pickerImport.launch(intent)
    }


    private fun insertAccount() {
        val dialog = createTwoFieldAlertDialogTwoFields(
            true,
            R.string.add_an_account,
            R.string.iban,
            R.string.amount
        ) { iban, amount ->
            val cuenta = Cuenta(iban, amount.toDouble(), dni)
            cuentaDao.insertarCuenta(cuenta)
            (activity as MainActivity).actualizarFragmentSaldo()
        }
        dialog.show()
    }

    private fun changeIbanAccount() {
        Log.d("Cambiar Iban", "Cambiando Iban")
        val dialog = createTwoFieldAlertDialogTwoFields(
            false,
            R.string.add_an_account,
            R.string.iban,
            R.string.newiban
        ) { iban, newIban ->
            if (!existeAccount(iban)) {
                Toast.makeText(requireActivity().applicationContext, getString(R.string.existsAccount), Toast.LENGTH_LONG).show()

            } else {
                cuentaDao.cambiarIbanCuenta(iban, newIban)
                (activity as MainActivity).actualizarFragmentSaldo()
            }
        }

        dialog.show()
    }

    private fun deleteAnAccount() {
        val dialog = createAlertDialogOneField(R.string.delete_an_account, R.string.hintdeleteaccount) { iban ->
            if (!existeAccount(iban)) {
                Toast.makeText(requireContext(), getString(R.string.existsAccount), Toast.LENGTH_LONG).show()
            } else {
                cuentaDao.borrarCuentaPorIBAN(iban)
                (activity as MainActivity).actualizarFragmentSaldo()
            }
        }
        dialog.show()
    }

    private fun deleteAllMovInAccount() {
        val dialog =
            createAlertDialogOneField(R.string.titledelelemov, R.string.hintdeletemovaccount) { iban ->
                if (!existeAccount(iban)) {
                    Toast.makeText(requireContext(), getString(R.string.existsAccount), Toast.LENGTH_LONG).show()
                } else {
                    movDAO.borrarMovimientosPorIBAN(iban)
                    (activity as MainActivity).actualizarFragmentSaldo()
                }
            }
        dialog.show()
    }

    private fun deleteAllAccounts(){
        val dialog=createAlertDialogSimple(R.string.questiondialog){
            cuentaDao.borrarTodasLasCuentas()
            (activity as MainActivity).actualizarFragmentSaldo()
        }
        dialog.show()
    }

    private fun changeIconColor(img : ImageView){
        img.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white))
    }
    private fun createAlertDialogSimple(question:Int,
                                        confirmAction: () -> Unit):AlertDialog{
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.custom_simple_dialog, null)
        val questiontv=dialogView.findViewById<TextView>(R.id.tv_question)
        val confirmButton = dialogView.findViewById<Button>(R.id.btn_dialogconfirm0)
        val cancelButton = dialogView.findViewById<Button>(R.id.btn_dialogcancel0)

        questiontv.text=getString(question)
        builder.setView(dialogView)
        val dialog = builder.create()

        confirmButton.setOnClickListener {
            confirmAction()
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.cancel()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    private fun createAlertDialogOneField(
        titleResId: Int,
        hintResId: Int,
        confirmAction: (String) -> Unit
    ): AlertDialog {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.custom_dialog_one_field, null)
        val dialogTitle = dialogView.findViewById<TextView>(R.id.tv_dialogtitle)
        val etField = dialogView.findViewById<EditText>(R.id.et_dialoginfo)
        val confirmButton = dialogView.findViewById<Button>(R.id.btn_dialogconfirm)
        val cancelButton = dialogView.findViewById<Button>(R.id.btn_dialogcancel)

        dialogTitle.text = getString(titleResId)
        etField.hint = getString(hintResId)

        builder.setView(dialogView)
        val dialog = builder.create()

        confirmButton.setOnClickListener {
            confirmAction(etField.text.toString())
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.cancel()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }
    private fun createTwoFieldAlertDialogTwoFields(
        isNumber:Boolean,
        titleResId: Int,
        hintField1ResId: Int,
        hintField2ResId: Int,
        confirmAction: (String, String) -> Unit

    ): AlertDialog {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.custom_dialog_two_fields, null)

        val dialogTitle = dialogView.findViewById<TextView>(R.id.tv_dialogtitle2)
        val etField1 = dialogView.findViewById<EditText>(R.id.et_dialogfield1)

        val etField2 = dialogView.findViewById<EditText>(R.id.et_dialogfield2)

            if (isNumber) {
                etField2.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            }


        val confirmButton = dialogView.findViewById<Button>(R.id.btn_dialogconfirm2)
        val cancelButton = dialogView.findViewById<Button>(R.id.btn_dialogcancel2)

        dialogTitle.text = getString(titleResId)
        etField1.hint = getString(hintField1ResId)
        etField2.hint = getString(hintField2ResId)

        builder.setView(dialogView)
        val dialog = builder.create()

        confirmButton.setOnClickListener {
            confirmAction(etField1.text.toString(), etField2.text.toString())
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.cancel()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    private fun existeAccount(iban: String): Boolean {
        return cuentas.any { cuenta -> cuenta.iban == iban }
    }
    private fun writeCsvFile(
        movimientos: MutableList<MovimientoBancario>,
        context: Context,
        fileName: String,
        directory: DocumentFile
    ) {
        try {
            val file = directory.createFile("text/csv", fileName)
            val outputStream = context.contentResolver.openOutputStream(file?.uri!!)

            BufferedWriter(OutputStreamWriter(outputStream)).use { writer ->
                // Escribir el encabezado del CSV
                writer.write("Importe,Descripción,IBAN,FechaImporte\n")

                // Escribir los movimientos bancarios
                for (movimiento in movimientos) {
                    // Asegúrate de que los datos sean válidos antes de escribirlos
                    val csvLine = "${movimiento.importe},${movimiento.descripcion},${movimiento.iban},${movimiento.fechaImporte}\n"
                    writer.write(csvLine)
                }
            }

            // Indicar al usuario que el archivo se ha exportado correctamente
            showToast("Archivo CSV exportado correctamente: $fileName", context)
        } catch (e: Exception) {
            // Manejar errores al escribir el archivo CSV
            e.printStackTrace()
            showToast("Error al exportar el archivo CSV", context)
        }
    }
    private fun readCsvFile(
        context: Context,
        fileUri: Uri
    ): MutableList<MovimientoBancario> {
        val list = mutableListOf<MovimientoBancario>()

        try {
            context.contentResolver.openInputStream(fileUri)?.use { inputStream ->
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                val csvParser = CSVParser.parse(bufferedReader, CSVFormat.DEFAULT)

                for (record in csvParser) {
                    try {
                        val importe = record.get(0).toDouble()
                        val descripcion = record.get(1)
                        val iban = record.get(2)
                        val fechaImporte = record.get(3)

                        // Crear objeto MovimientoBancario y agregarlo a la lista
                        val movimientoBancario = MovimientoBancario(importe, descripcion, iban, fechaImporte)
                        list.add(movimientoBancario)
                    } catch (e: Exception) {
                        // Manejar errores al analizar los datos CSV
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: Exception) {
            // Manejar errores al abrir el archivo CSV
            e.printStackTrace()
            showToast("Error al abrir el archivo CSV", context)
        }

        return list
    }

    // Método para escr
    private fun showToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }




}
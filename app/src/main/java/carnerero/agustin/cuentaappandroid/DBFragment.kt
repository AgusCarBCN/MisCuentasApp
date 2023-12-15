package carnerero.agustin.cuentaappandroid

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
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
import carnerero.agustin.cuentaappandroid.AppConst.CREATE_FILE
import carnerero.agustin.cuentaappandroid.dao.CuentaDao
import carnerero.agustin.cuentaappandroid.dao.MovimientoBancarioDAO
import carnerero.agustin.cuentaappandroid.databinding.FragmentDbBinding
import carnerero.agustin.cuentaappandroid.model.Cuenta
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario
import carnerero.agustin.cuentaappandroid.utils.Utils
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.OutputStreamWriter


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DBFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DBFragment : Fragment(){
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var sharedPreferences: SharedPreferences
    private val admin = DataBaseAppSingleton.getInstance(context)
    private val cuentaDao = CuentaDao(admin)
    private val movDAO = MovimientoBancarioDAO(admin)
    private lateinit var directoryPickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var dni: String
    private lateinit var cuentas:ArrayList<Cuenta>
    private var _binding: FragmentDbBinding? = null
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
        _binding = FragmentDbBinding.inflate(inflater, container, false)
        val view = binding.root
        directoryPickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.also { uri ->
                    val directory = DocumentFile.fromTreeUri(requireContext(), uri)
                    if (directory != null && directory.isDirectory) {
                        // El usuario ha seleccionado un directorio
                        // Puedes guardar la información sobre el directorio o utilizarlo directamente
                        val directoryPath = directory.uri.toString()

                        // Aquí puedes realizar la lógica para exportar datos al directorio seleccionado
                        // Llama a la función writeCsvFile con el directorio obtenido
                        val records = movDAO.getAll() // Obtén tus registros de la base de datos
                        writeCsvFile(records, requireContext(), "movimientos_export.csv", directory)
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
           pickDirectoryAndExport()
            // Actualizar el fragmento de saldo en la actividad principal
            (activity as MainActivity).actualizarFragmentSaldo()
        }
        return view
    }
    private fun pickDirectoryAndExport() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        directoryPickerLauncher.launch(intent)
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
            if (isNumber) {
                etField2.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            }

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

    // Método para escr
    private fun showToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    private fun createFile(pickerInitialUri: Uri) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
            putExtra(Intent.EXTRA_TITLE, "invoice.pdf")

            // Optionally, specify a URI for the directory that should be opened in
            // the system file picker before your app creates the document.
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
        }
        startActivityForResult(intent, CREATE_FILE)

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DBFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}
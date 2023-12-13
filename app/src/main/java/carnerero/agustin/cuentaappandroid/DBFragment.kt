package carnerero.agustin.cuentaappandroid

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.dao.CuentaDao
import carnerero.agustin.cuentaappandroid.dao.MovimientoBancarioDAO
import carnerero.agustin.cuentaappandroid.databinding.FragmentDbBinding
import carnerero.agustin.cuentaappandroid.model.Cuenta
import carnerero.agustin.cuentaappandroid.utils.Utils

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DBFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DBFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var sharedPreferences: SharedPreferences
    private val admin = DataBaseAppSingleton.getInstance(context)
    private val cuentaDao = CuentaDao(admin)
    private val movDAO = MovimientoBancarioDAO(admin)
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
        // Obtener el nombre del usuario almacenado en SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        dni = sharedPreferences.getString(getString(R.string.id), null)!!
        cuentas= cuentaDao.listarCuentasPorDNI(dni) as ArrayList<Cuenta>
        val imgList = listOf(
            binding.imgaddaccount, binding.imgrename,
            binding.imgdeletedataaccount, binding.imgdeleteaccount, binding.imgdeleteAll,
            binding.imgimportfile, binding.imgexport
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
        return view
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
                }
            }
        dialog.show()
    }

    private fun changeIconColor(img : ImageView){
        img.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white))
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
            etField2.inputType = InputType.TYPE_CLASS_NUMBER
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
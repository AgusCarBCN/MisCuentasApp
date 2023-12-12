package carnerero.agustin.cuentaappandroid

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.dao.CuentaDao
import carnerero.agustin.cuentaappandroid.dao.UsuarioDao
import carnerero.agustin.cuentaappandroid.databinding.FragmentDbBinding
import carnerero.agustin.cuentaappandroid.databinding.FragmentInfoBinding
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
    private val cuentaDao= CuentaDao(admin)
    private lateinit var dni:String
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
        val imgList= listOf(binding.imgaddaccount,binding.imgrename,
            binding.imgdeletedataaccount,binding.imgdeleteaccount,binding.imgdeleteAll,
            binding.imgimportfile,binding.imgexport)
       if(Utils.isDarkTheme) {
           for (img in imgList) {
               changeIconColor(img)
           }
       }
        imgList[0].setOnClickListener {
            insertAccount()
            // Actualizar el fragmento de saldo en la actividad principal
            (activity as MainActivity).actualizarFragmentSaldo()
        }
        return view
    }

    private fun insertAccount() {
        val builder = AlertDialog.Builder(context)

        // Inflar el diseño personalizado
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.custom_dialog_two_fields, null)
        val msgHintIban = "Iban"
        val msgHintAmount = "${getString(R.string.amount)}"
        // Obtener referencias a las vistas dentro del diseño personalizado
        val dialogTitle = dialogView.findViewById<TextView>(R.id.tv_dialogtitle2)
        val et_iban = dialogView.findViewById<EditText>(R.id.et_dialogfield1)
        val et_amount = dialogView.findViewById<EditText>(R.id.et_dialogfield2)
        val confirmButton = dialogView.findViewById<Button>(R.id.btn_dialogconfirm2)
        val cancelButton = dialogView.findViewById<Button>(R.id.btn_dialogcancel2)

        //Hint de edit text
        et_iban.hint = msgHintIban
        et_amount.hint = msgHintAmount
        // Configurar el contenido del AlertDialog con el diseño personalizado
        builder.setView(dialogView)
        // Configurar propiedades específicas del diseño
        dialogTitle.text = getString(R.string.add_an_account)

        // Crear el AlertDialog antes de usarlo para poder cerrarlo más adelante
        val dialog = builder.create()

        // Configurar el evento de clic para el botón personalizado de confirmar
        confirmButton.setOnClickListener {
            val iban = et_iban.text.toString()
            val amount = et_amount.text.toString().toDouble()
            val cuenta = Cuenta(iban, amount, dni)
            cuentaDao.insertarCuenta(cuenta)
            // Cerrar el AlertDialog
            dialog.dismiss()

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


    private fun changeIconColor(img : ImageView){
        img.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white))
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
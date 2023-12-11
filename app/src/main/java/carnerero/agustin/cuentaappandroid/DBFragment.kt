package carnerero.agustin.cuentaappandroid

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
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
        }
        return view
    }
    private fun insertAccount() {
        val builder = AlertDialog.Builder(context, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background)
        builder.setTitle("Insertar una Cuenta")

        // Crear los EditText para IBAN y Saldo
        val ibanEditText = EditText(context)
        val saldoEditText = EditText(context)

        // Establecer hints para los EditText
        ibanEditText.hint = "Iban"
        saldoEditText.hint = "Saldo"

        // Configurar InputFilter para permitir números reales en el campo de saldo
        val decimalInputFilter = InputFilter { source, start, end, dest, dstart, dend ->
            val newString = dest.toString().substring(0, dstart) + source.subSequence(start, end) + dest.toString().substring(dend)
            val decimalPattern = Regex("^(\\d*\\.?\\d{0,2})?\$")
            if (decimalPattern.matches(newString)) null else ""
        }

        saldoEditText.filters = arrayOf(decimalInputFilter) // Aplicar el InputFilter al EditText de saldo

        builder.setView(LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            addView(ibanEditText)
            addView(saldoEditText)
        })

        builder.setPositiveButton("Aceptar") { _, _ ->
            // Obtener los nuevos valores desde los EditText
            val iban = ibanEditText.text.toString()
            val saldo = saldoEditText.text.toString()
            val cuenta= Cuenta(iban,saldo.toDouble(),dni)
            // Realizar la lógica para insertar la cuenta con los valores proporcionados
            // Puedes llamar a tu método correspondiente para manejar la inserción
            cuentaDao.insertarCuenta(cuenta)
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun insertAccountToDatabase(iban: String, saldo: String) {
        // Aquí deberías realizar la lógica para insertar la cuenta en tu base de datos
        // Puedes llamar a tu método correspondiente para realizar la inserción
        // userDao.insertAccount(iban, saldo)
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
package carnerero.agustin.cuentaappandroid

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateUserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateUserFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
    ): View? {

        // Inflate the layout for this fragment
        val rootview= inflater.inflate(R.layout.fragment_create_user, container, false)
        val intent=  Intent(context,LoginActivity::class.java)
        //Acceso a botones
        val cancel:Button=rootview.findViewById(R.id.btn_cancel)
        val confirm:Button=rootview.findViewById(R.id.btn_confirm)
        //Acceso a los editText de Usuario
        val name:EditText=rootview.findViewById(R.id.et_name)
        val dni:EditText=rootview.findViewById(R.id.et_dni)
        val address:EditText=rootview.findViewById(R.id.et_address)
        val city:EditText=rootview.findViewById(R.id.et_City)
        val zipCode:EditText=rootview.findViewById(R.id.et_postalAddress)
        val phone:EditText=rootview.findViewById(R.id.et_textphone)
        val userpass:EditText=rootview.findViewById(R.id.et_pass)
        //Acceso a los ediText de las cuentas
        val mainAccount:EditText=rootview.findViewById(R.id.et_mainAccount)
        val mainAmount:EditText=rootview.findViewById(R.id.et_mainamount)
        val secondaryAccount:EditText=rootview.findViewById(R.id.et_secondaryaccount)
        val secondaryAmount:EditText=rootview.findViewById(R.id.et_secondaryamount)

        cancel.setOnClickListener(){
            startActivity(intent)
        }
        confirm.setOnClickListener(){
            //Se crea la base de datos
            val admin=DataBaseApp(context,"cuentaApp",null,1)
            //obtengo una base de datos en la que puedo agregar datos
            val db=admin.writableDatabase
            //Creo registro, a traves de la cual envio datos
            val reg=ContentValues()
            reg.put("dni",dni.text.toString())
            reg.put("nombre",name.text.toString())
            reg.put("domicilio",address.text.toString())
            reg.put("ciudad",city.text.toString())
            reg.put("codigopostal",zipCode.text.toString())
            reg.put("telefono",phone.text.toString())
            reg.put("password",userpass.text.toString())
            //se inserta registro a tabla usuario
            db.insert("USUARIO",null,reg)
            val regAccount1=ContentValues()
            regAccount1.put("iban",mainAccount.text.toString())
            regAccount1.put("saldo",mainAmount.text.toString().toDouble())
            regAccount1.put("dni",dni.text.toString())
            db.insert("CUENTA",null,regAccount1)
            val regAccount2=ContentValues()
            regAccount2.put("iban",secondaryAccount.text.toString())
            regAccount2.put("saldo",secondaryAmount.text.toString().toDouble())
            regAccount2.put("dni",dni.text.toString())
            db.insert("CUENTA",null,regAccount2)
            db.close()
            startActivity(intent)
        }
        return rootview
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreateUserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateUserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
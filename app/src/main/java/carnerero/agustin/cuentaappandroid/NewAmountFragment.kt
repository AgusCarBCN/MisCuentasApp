package carnerero.agustin.cuentaappandroid

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewAmountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewAmountFragment : Fragment() {
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

        val rootview= inflater.inflate(R.layout.fragment_new_amount, container, false)
       //Recupero dni del usuario que inicio sesion
        val sharedPreferences = requireContext().getSharedPreferences("MiPreferencia", Context.MODE_PRIVATE)
        val dni = sharedPreferences.getString("dni", "")
        //Recupero Spinner de la vista y creo adapter para cargar los datos de la tabla cuentas del usuario
        //que inicia sesion.
        val spinnerCuentas = rootview.findViewById<Spinner>(R.id.sp_cuentas)
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item)
        // Conecta a la base de datos y obtén los datos de la tabla "cuentas"
        val admin=DataBaseApp(context,"cuentaApp",null,1)
        val db = admin.readableDatabase
        val cursor = db.rawQuery("SELECT iban FROM CUENTA WHERE dni='${dni}'", null)
        if (cursor.moveToFirst()) {
            do {
                val iban = cursor.getString(cursor.getColumnIndex("iban"))
                adapter.add(iban)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        spinnerCuentas.adapter = adapter

        return rootview
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewAmountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewAmountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
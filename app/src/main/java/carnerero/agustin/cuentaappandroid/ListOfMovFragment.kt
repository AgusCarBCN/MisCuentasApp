package carnerero.agustin.cuentaappandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import carnerero.agustin.cuentaappandroid.controller.AdapterMov
import carnerero.agustin.cuentaappandroid.databinding.FragmentListOfMovBinding
import carnerero.agustin.cuentaappandroid.databinding.FragmentNewAmountBinding
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListOfMovFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListOfMovFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var adaptermovimientos: AdapterMov
    private lateinit var recyclerView: RecyclerView
    private var _binding: FragmentListOfMovBinding?=null
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
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentListOfMovBinding.inflate(inflater,container,false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager=LinearLayoutManager(context)
        val movimientos = arguments?.getParcelableArrayList("clave_movimientos",MovimientoBancario::class.java)
        recyclerView=binding.rvMovimientos
        recyclerView.layoutManager=layoutManager
        recyclerView.setHasFixedSize(false)
        adaptermovimientos= movimientos?.let { AdapterMov(it) }!!
        recyclerView.adapter=adaptermovimientos
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Importante para evitar fugas de memoria
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListOfMovFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListOfMovFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
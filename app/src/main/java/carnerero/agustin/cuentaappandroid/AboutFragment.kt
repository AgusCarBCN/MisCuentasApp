package carnerero.agustin.cuentaappandroid

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import carnerero.agustin.cuentaappandroid.databinding.FragmentAboutBinding
import carnerero.agustin.cuentaappandroid.databinding.FragmentAjustesBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AboutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AboutFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentAboutBinding? = null
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
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val attributionText=getString(R.string.atributionicon)

        // Obtén la referencia al contenedor de atribuciones usando View Binding
        val attributionsContainer = binding.attributionsContainer

        // Lista de atribuciones
        val attributionsList = listOf(

            "<a href=\"https://www.flaticon.es/iconos-gratis/contabilidad\" " +
                    "title=\"contabilidad iconos\">Contabilidad ${attributionText} 2D3ds - Flaticon</a>"

        )

        // Itera sobre la lista de atribuciones y crea TextViews dinámicamente
        for (attributionHtml in attributionsList) {
            val attributionTextView = TextView(requireContext())

            // Establece el movimiento del enlace para que sea clickeable
            attributionTextView.movementMethod = LinkMovementMethod.getInstance()

            // Convierte el HTML a un objeto Spanned
            val spanned = Html.fromHtml(attributionHtml, Html.FROM_HTML_MODE_LEGACY)

            // Establece el texto en el TextView
            attributionTextView.text = spanned

            // Agrega un oyente para el clic en el enlace
            attributionTextView.setOnClickListener {
                // Extrae la URL del enlace y abre en un navegador
                val url = Html.fromHtml(attributionHtml, Html.FROM_HTML_MODE_LEGACY).toString()
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }

            // Agrega el TextView al contenedor de atribuciones
            attributionsContainer.addView(attributionTextView)
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AboutFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AboutFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
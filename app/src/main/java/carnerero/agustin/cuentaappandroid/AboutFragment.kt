package carnerero.agustin.cuentaappandroid

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import carnerero.agustin.cuentaappandroid.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupGitHubLink()
        setupAttributions()
    }

    private fun setupGitHubLink() {
        val githubLinkTextView: TextView = binding.tvgithublink
        val visitGit = getString(R.string.visitGitHub)
        val githubLinkHtml = "<a href=\"https://github.com/AgusCarBCN\" title=\"GitHub\">$visitGit</a>"

        githubLinkTextView.movementMethod = LinkMovementMethod.getInstance()
        githubLinkTextView.text = fromHtml(githubLinkHtml)
        githubLinkTextView.setOnClickListener {
            openUrl(githubLinkHtml)
        }
    }

    private fun setupAttributions() {
        val attributionsContainer = binding.attributionsContainer
        val attributionText = getString(R.string.atributionicon)

        val attributionsList = listOf(
            "<a href=\"https://www.flaticon.es/iconos-gratis/contabilidad\" " +
                    "title=\"contabilidad iconos\">Contabilidad $attributionText 2D3ds - Flaticon</a>",
            "<a href=\"https://www.freepik.es/icono/logotipo-github_25231#fromView=search&term=github&page=1&position=2&track=ais&uuid=bca581ff-3f61-49a9-8010-59c21c4b0f7c\">" +
                    "$attributionText Dave Gandy</a>"
        )

        for (attributionHtml in attributionsList) {
            val attributionTextView = TextView(requireContext())
            attributionTextView.movementMethod = LinkMovementMethod.getInstance()
            attributionTextView.text = fromHtml(attributionHtml)
            attributionTextView.setOnClickListener {
                openUrl(attributionHtml)
            }
            attributionsContainer.addView(attributionTextView)
        }
    }

    private fun openUrl(html: String) {
        try {
            val url = fromHtml(html).toString()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            // Manejo de errores
        }
    }

    private fun fromHtml(html: String): CharSequence {
        return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package carnerero.agustin.cuentaappandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import carnerero.agustin.cuentaappandroid.databinding.FragmentNewAmountBinding
import carnerero.agustin.cuentaappandroid.databinding.FragmentNotificationsBinding


class NotificationsFragment : Fragment() {
    // Variable para manejar el View Binding
    private var _binding: FragmentNotificationsBinding ?= null
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
        // Inflate the layout for this fragment
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

}
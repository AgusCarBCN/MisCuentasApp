package carnerero.agustin.cuentaappandroid


import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.databinding.FragmentNotificationsBinding




    class NotificationsFragment : Fragment() {


        // Variable para manejar el View Binding
        private var _binding: FragmentNotificationsBinding? = null
        private val binding get() = _binding!!
        private lateinit var sharedPreferences: SharedPreferences
        private var savedProgress: Int = 0
        private var savedProgressBal: Int = 0
        private var currencyBase:String?=null

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

            val switchDiaryReport = binding.switchday
            val switchWeeklyReport = binding.switchweek
            val switchMonthlyReport = binding.switchmonth
            val switchAlertBalance = binding.switchalertbalance
            val switchAlertLimit = binding.switchalertlimit
            val seekBar = binding.seekBar
            val seekBarBal = binding.seekBarBalance
            val percentTextView = binding.tvPercent
            val percentTextViewBal = binding.tvPercentbalance


            // Obtener preferencias compartidas
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
            // Obtener el estado de los switchs de las notificaciones
            val isCheckedSwitchDay =
                sharedPreferences.getBoolean(getString(R.string.switchday), false)
            val isCheckedSwitchWeek =
                sharedPreferences.getBoolean(getString(R.string.switchweek), false)
            val isCheckedSwitchMonth =
                sharedPreferences.getBoolean(getString(R.string.switchmonth), false)
            val isCheckedSwitchAlertBalance =
                sharedPreferences.getBoolean(getString(R.string.switchbalance), false)
            val isCheckedSwitchAlertLimit =
                sharedPreferences.getBoolean(getString(R.string.switchlimit), false)
            //Recupera el progreso de SharedPreferences cuando se inicia tu actividad o fragmento

            savedProgressBal = sharedPreferences.getInt("progressValueBal", 0)
            savedProgress = sharedPreferences.getInt("progressValue", 0)
            currencyBase=sharedPreferences.getString(getString(R.string.basecurrency),"EUR").toString()

            if(currencyBase=="INR"){
                seekBar.max=1000000
                seekBarBal.max=100000
            }else{
                seekBar.max=5000
                seekBarBal.max=5000
            }

            //Asigno el estado de los switchs
            switchDiaryReport.isChecked = isCheckedSwitchDay
            switchAlertLimit.isChecked = isCheckedSwitchAlertLimit
            switchAlertBalance.isChecked = isCheckedSwitchAlertBalance
            switchWeeklyReport.isChecked = isCheckedSwitchWeek
            switchMonthlyReport.isChecked = isCheckedSwitchMonth

            // Aplica la visibilidad de la barra de progreso y el TextView
            seekBar.visibility = if (isCheckedSwitchAlertLimit) View.VISIBLE else View.GONE
            percentTextView.visibility = if (isCheckedSwitchAlertLimit) View.VISIBLE else View.GONE
            seekBarBal.visibility = if (isCheckedSwitchAlertBalance) View.VISIBLE else View.GONE
            percentTextViewBal.visibility =
                if (isCheckedSwitchAlertBalance) View.VISIBLE else View.GONE

            //switch de alerta de gastos permisibles
            switchAlertLimit.setOnCheckedChangeListener { _, isChecked ->
                //Guardo configuracion en sharedPreferences
                sharedPreferences.edit().putBoolean(getString(R.string.switchlimit), isChecked)
                    .apply()
                // Muestra u oculta la barra de progreso según el estado del interruptor
                seekBar.visibility = if (isChecked) View.VISIBLE else View.GONE
                // Muestra u oculta el TextView según el estado del interruptor
                percentTextView.visibility = if (isChecked) View.VISIBLE else View.GONE
                //Obtengo el valor del porcentaje seleccionado del seekbar

            }

            switchAlertBalance.setOnCheckedChangeListener { _, isChecked ->
                //Guardo configuracion en sharedPreferences
                sharedPreferences.edit().putBoolean(getString(R.string.switchbalance), isChecked)
                    .apply()

                // Muestra u oculta la barra de progreso según el estado del interruptor
                seekBarBal.visibility = if (isChecked) View.VISIBLE else View.GONE
                // Muestra u oculta el TextView según el estado del interruptor
                percentTextViewBal.visibility = if (isChecked) View.VISIBLE else View.GONE

            }
            switchDiaryReport.setOnCheckedChangeListener { _, isChecked ->

                sharedPreferences.edit().putBoolean(getString(R.string.switchday), isChecked)
                    .apply()
            }

            switchWeeklyReport.setOnCheckedChangeListener { _, isChecked ->

                sharedPreferences.edit().putBoolean(getString(R.string.switchweek), isChecked)
                    .apply()
            }
            switchMonthlyReport.setOnCheckedChangeListener { _, isChecked ->

                sharedPreferences.edit().putBoolean(getString(R.string.switchmonth), isChecked)
                    .apply()
            }


            // Asigna un listener de cambio de progreso a la barra de progreso
            // Dentro de tu clase o función donde estás manejando el SeekBar y el TextView
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    // Actualiza el valor del progreso y muestra el porcentaje en el TextView
                    percentTextView.text = "$progress"
                    // Guarda el progreso en SharedPreferences
                    val editor = sharedPreferences.edit()
                    editor.putInt("progressValue", progress)
                    editor.apply()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    // No es necesario implementar esto, pero puedes hacerlo si lo necesitas
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    // No es necesario implementar esto, pero puedes hacerlo si lo necesitas
                }
            })

            seekBarBal.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    // Actualiza el valor del progreso y muestra el porcentaje en el TextView
                    percentTextViewBal.text = "$progress"
                    // Guarda el progreso en SharedPreferences
                    val editor = sharedPreferences.edit()
                    editor.putInt("progressValueBal", progress)
                    editor.apply()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    // No es necesario implementar esto, pero puedes hacerlo si lo necesitas
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    // No es necesario implementar esto, pero puedes hacerlo si lo necesitas
                }
            })

            //Recupera el progreso de SharedPreferences cuando se inicia tu actividad o fragmento
            savedProgress = sharedPreferences.getInt("progressValue", 0)
            savedProgressBal = sharedPreferences.getInt("progressValueBal", 0)
            seekBar.progress = savedProgress
            seekBarBal.progress = savedProgressBal
            percentTextView.text = "$savedProgress"
            percentTextViewBal.text = "$savedProgressBal"



            return binding.root
        }
    }




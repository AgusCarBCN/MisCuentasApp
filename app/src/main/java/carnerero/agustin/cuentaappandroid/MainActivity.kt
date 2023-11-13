package carnerero.agustin.cuentaappandroid
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.preference.PreferenceManager
import carnerero.agustin.cuentaappandroid.databinding.ActivityMainBinding
import carnerero.agustin.cuentaappandroid.utils.Utils
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding:ActivityMainBinding
    private lateinit var fragment:Fragment
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        //Acceso a drawerlayout
        drawer=binding.draverLayout
        drawer = findViewById(R.id.draver_layout)
        toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        val navigationView=binding.navView
        navigationView.setNavigationItemSelectedListener(this)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)


    }

    override fun onDestroy() {
        Utils.releaseSound()
        super.onDestroy()
    }

    override fun onResume() {


        super.onResume()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragmentContainer: FragmentContainerView = findViewById(R.id.fcv_main_container)

        when (item.itemId) {

            R.id.consulta -> fragment = ConsultaFragment()
            R.id.nuevoImporte -> fragment = NewAmountFragment()
            R.id.estadistica -> fragment = BarChartFragment()
            R.id.transferencia -> fragment = TransaccionFragment()
            R.id.configuracion -> fragment = AjustesFragment()
            R.id.salir -> {
                finish()
                return true
            }
            else -> fragment = SaldoFragment()
        }
        changeFragmentMain(fragment)
        fragment.setMenuVisibility(true)
        fragmentContainer.visibility = View.VISIBLE
        drawer.closeDrawer(GravityCompat.START)
        return true
    }



    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    private fun changeFragmentMain(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fcv_main_container, fragment)
            .commit()
    }

    private fun showSaldo() {
        val fragmentSaldo = SaldoFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.info_container, fragmentSaldo)
            .commit()
    }


    fun actualizarFragmentSaldo() {
        val fragmentSaldo = SaldoFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.info_container, fragmentSaldo)
        transaction.commit()
    }

    fun inicio() {
        val fragment = LogoFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fcv_main_container, fragment)
        transaction.commit()
    }
}
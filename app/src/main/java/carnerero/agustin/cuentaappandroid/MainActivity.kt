package carnerero.agustin.cuentaappandroid
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import carnerero.agustin.cuentaappandroid.databinding.ActivityMainBinding
import carnerero.agustin.cuentaappandroid.utils.Utils
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding:ActivityMainBinding
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
    }
    override fun onDestroy() {
        Utils.releaseSound()
        super.onDestroy()
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragmentContainer: FragmentContainerView = findViewById(R.id.fcv_main_container)
        val fragment: Fragment

        when (item.itemId) {
            R.id.consulta, R.id.nuevoImporte, R.id.estadistica, R.id.transferencia -> {
                showSaldo()
                fragment = when (item.itemId) {
                    R.id.consulta -> ConsultaFragment()
                    R.id.nuevoImporte -> NewAmountFragment()
                    R.id.estadistica -> BarChartFragment()
                    R.id.transferencia -> TransaccionFragment()
                    else -> SaldoFragment()
                }
            }

            R.id.configuracion -> {
                hideSaldo()
                fragment = SettingsFragment()
            }

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

    private fun hideSaldo() {
        val fragmentManager = supportFragmentManager
        val fragmentSaldo = fragmentManager.findFragmentById(R.id.info_container)

        // Verifica si el fragmento ya está en el contenedor
        if (fragmentSaldo != null) {
            val transaction = fragmentManager.beginTransaction()
            transaction.hide(fragmentSaldo)
            transaction.commit()
        }
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
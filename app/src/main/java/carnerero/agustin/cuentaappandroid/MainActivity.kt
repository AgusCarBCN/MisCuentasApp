package carnerero.agustin.cuentaappandroid
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import carnerero.agustin.cuentaappandroid.databinding.ActivityMainBinding
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragmentContainer:FragmentContainerView=findViewById(R.id.fcv_main_container)
        val fragment= SaldoFragment()
        changeFragmentMain(fragment)
        fragment.setMenuVisibility(true)
        fragmentContainer.visibility=View.VISIBLE
        when (item.itemId) {
            R.id.consulta -> {
                val fragmentSearch = ConsultaFragment ()
                changeFragmentMain(fragmentSearch)
                showSaldo()
                fragmentSearch.setMenuVisibility(true)
                fragmentContainer.visibility=View.VISIBLE
            }
            R.id.nuevoImporte -> {
                val nuevoImporteFragment = NewAmountFragment()
                changeFragmentMain(nuevoImporteFragment)
                showSaldo()
                nuevoImporteFragment.setMenuVisibility(true)
                fragmentContainer.visibility=View.VISIBLE            }
            R.id.estadistica -> {
                val barChartFragment =  BarChartFragment()
                changeFragmentMain(barChartFragment)
                showSaldo()
                barChartFragment.setMenuVisibility(true)
                fragmentContainer.visibility=View.VISIBLE
            }
            R.id.transferencia -> {
               val transferenciaFragment =  TransaccionFragment()
                changeFragmentMain(transferenciaFragment)
                showSaldo()
                transferenciaFragment.setMenuVisibility(true)
                fragmentContainer.visibility=View.VISIBLE
            }
            R.id.salir ->finish()
            R.id.configuracion ->{
                val intent = Intent(this, CreateUserActivity::class.java)
                startActivity(intent)
            }
        }
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
        val fragmentSaldo=SaldoFragment()
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
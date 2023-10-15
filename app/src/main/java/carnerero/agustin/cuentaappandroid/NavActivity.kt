package carnerero.agustin.cuentaappandroid
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.navigation.NavigationView

class NavActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)
        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
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
        val navigationView: NavigationView = findViewById(R.id.nav_view)
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
                Toast.makeText(this, "Consulta", Toast.LENGTH_SHORT).show()
                val fragmentSearch = ConsultaFragment ()
                //val fragmentResul=ResultFragment()
                changeFragmentMain(fragmentSearch)
                //changeFragmentInfo(fragmentResul)
                fragmentSearch.setMenuVisibility(true)
                fragmentContainer.visibility=View.VISIBLE

            }
            R.id.nuevoImporte -> {

                Toast.makeText(this, "Nuevo Importe", Toast.LENGTH_SHORT).show()
                // Aquí puedes agregar el fragmento que deseas mostrar
                val nuevoImporteFragment = NewAmountFragment() // Reemplaza con el nombre de tu fragmento
                changeFragmentMain(nuevoImporteFragment)
                nuevoImporteFragment.setMenuVisibility(true)
                fragmentContainer.visibility=View.VISIBLE
            }
            R.id.estadistica -> {
                Toast.makeText(this, "estadistica", Toast.LENGTH_SHORT).show()


            }
            R.id.transferencia -> {
                Toast.makeText(this, "trasnferencia", Toast.LENGTH_SHORT).show()
               val transferenciaFragment =  TransaccionFragment()// Reemplaza con el nombre de tu fragmento

                changeFragmentMain(transferenciaFragment)
                transferenciaFragment.setMenuVisibility(true)
                fragmentContainer.visibility=View.VISIBLE

            }
            R.id.salir ->{Toast.makeText(this, "Saliendo de la aplicación", Toast.LENGTH_LONG).show()
            finish()}
            R.id.configuracion -> Toast.makeText(this, "configuracion", Toast.LENGTH_SHORT).show()

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)

    }
    private fun changeFragmentMain(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fcv_main_container, fragment)
            .commit()
    }
    private fun changeFragmentInfo(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.info_container, fragment)
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
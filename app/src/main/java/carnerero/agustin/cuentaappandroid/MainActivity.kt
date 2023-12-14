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
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding
    private lateinit var fragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflar el diseño de la actividad utilizando View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar la barra de herramientas (toolbar)
        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        // Acceso al DrawerLayout
        drawer = binding.draverLayout
        // Configurar ActionBarDrawerToggle para manejar la apertura y cierre del DrawerLayout
        toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        // Habilitar el botón de inicio en la barra de herramientas
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        // Configurar el NavigationView y su escucha de eventos de selección
        val navigationView = binding.navView
        navigationView.setNavigationItemSelectedListener(this)


    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Obtener el contenedor de fragmentos
        val fragmentContainer: FragmentContainerView = findViewById(R.id.fcv_main_container)

        // Determinar el fragmento a mostrar según la selección del usuario en el NavigationView
        when (item.itemId) {
            R.id.home -> fragment = LogoFragment()
            R.id.consulta -> fragment = ConsultaFragment()
            R.id.nuevoImporte -> fragment = NewAmountFragment()
            R.id.estadistica -> fragment = BarChartFragment()
            R.id.transferencia ->fragment = TransaccionFragment()
            R.id.db->fragment=DBFragment()
            R.id.configuracion -> fragment = AjustesFragment()
            R.id.about->fragment=AboutFragment()
            R.id.calculator->fragment = CalculatorFragment()
            R.id.profile->fragment=InfoFragment()
            R.id.salir -> {
                // Finalizar la actividad si se selecciona "salir"
                finish()
                return true
            }
            else -> fragment = ListOfAccountsFragment()
        }

        // Mostrar el fragmento de saldo en el contenedor de información
        showSaldo()
        // Cambiar el fragmento principal en el contenedor principal
        changeFragmentMain(fragment)
        // Establecer la visibilidad del contenedor de fragmentos
        fragmentContainer.visibility = View.VISIBLE
        // Cerrar el DrawerLayout después de la selección
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        // Sincronizar el estado de ActionBarDrawerToggle después de que se ha restaurado la instancia
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        // Manejar cambios en la configuración, por ejemplo, rotaciones de pantalla
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    private fun changeFragmentMain(fragment: Fragment) {
        // Cambiar el fragmento principal en el contenedor principal
        supportFragmentManager.beginTransaction()
            .replace(R.id.fcv_main_container, fragment)
            .commit()
    }

    private fun showSaldo() {
        // Mostrar el fragmento de saldo en el contenedor de información
        val fragmentSaldo = ListOfAccountsFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.info_container, fragmentSaldo)
            .commit()
    }

    fun actualizarFragmentSaldo() {
        // Actualizar el fragmento de saldo en el contenedor de información
        val fragmentSaldo = ListOfAccountsFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.info_container, fragmentSaldo)
        transaction.commit()
    }

    fun inicio() {
        // Mostrar el fragmento de inicio en el contenedor principal
        val fragment = LogoFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fcv_main_container, fragment)
        transaction.commit()
    }
}

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
        val fragmentContainer:FragmentContainerView=findViewById(R.id.fcv_main_container)
        fragmentContainer.visibility=View.INVISIBLE
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragmentContainer:FragmentContainerView=findViewById(R.id.fcv_main_container)
        fragmentContainer.visibility=View.INVISIBLE
        //Administracion de  fragments en activity
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        //val newamountlayout: LinearLayout = findViewById(R.id.newamountlayout)
        //newamountlayout.visibility = View.INVISIBLE
        when (item.itemId) {
            R.id.consulta -> {
                Toast.makeText(this, "Consulta", Toast.LENGTH_SHORT).show()
                val fragmentSearch = ConsultaFragment () // Reemplaza con el nombre de tu fragmento
                fragmentTransaction.replace(R.id.fcv_main_container, fragmentSearch                )
                fragmentSearch.setMenuVisibility(true)
                fragmentContainer.visibility=View.VISIBLE

            }
            R.id.nuevoImporte -> {
                //Toast.makeText(this, "nuevoImporte", Toast.LENGTH_SHORT).show()
                //val newamountlayout: Fragment = findViewById(R.id.fragmentContainerView)
                //newamountlayout.visibility = View.VISIBLE
                Toast.makeText(this, "Nuevo Importe", Toast.LENGTH_SHORT).show()
                // Aquí puedes agregar el fragmento que deseas mostrar
                val nuevoImporteFragment = NewAmountFragment() // Reemplaza con el nombre de tu fragmento
                fragmentTransaction.replace(R.id.fcv_main_container, nuevoImporteFragment)
                nuevoImporteFragment.setMenuVisibility(true)
                fragmentContainer.visibility=View.VISIBLE
            }
            R.id.estadistica -> {
                Toast.makeText(this, "estadistica", Toast.LENGTH_SHORT).show()


            }
            R.id.transferencia -> {
                Toast.makeText(this, "trasnferencia", Toast.LENGTH_SHORT).show()
                val transferenciaFragment =  TransaccionFragment()// Reemplaza con el nombre de tu fragmento
                fragmentTransaction.replace(R.id.fcv_main_container, transferenciaFragment)
                transferenciaFragment.setMenuVisibility(true)
                fragmentContainer.visibility=View.VISIBLE

            }
            R.id.salir ->{Toast.makeText(this, "Saliendo de la aplicación", Toast.LENGTH_LONG).show()
            finish()}
            R.id.configuracion -> Toast.makeText(this, "configuracion", Toast.LENGTH_SHORT).show()
        }
        fragmentTransaction.commit()
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
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }


}
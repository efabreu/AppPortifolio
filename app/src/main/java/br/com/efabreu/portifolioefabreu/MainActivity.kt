package br.com.efabreu.portifolioefabreu

import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import br.com.efabreu.portifolioefabreu.fragments.GithubFragment
import br.com.efabreu.portifolioefabreu.fragments.HomeFragment
import br.com.efabreu.portifolioefabreu.fragments.LinkedInFragment
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar :MaterialToolbar
    private lateinit var collapseTb :CollapsingToolbarLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setStatusbarColor()
        setupDrawer(savedInstanceState)

    }

    private fun setupDrawer(savedInstanceState: Bundle?) {
        drawerLayout = findViewById(R.id.drawer_layout)
        toolbar = findViewById(R.id.toolbar)
        collapseTb = findViewById(R.id.collapsing_tb)
        setSupportActionBar(toolbar)
        collapseTb.title = "Home"
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()).commit()
            navigationView.setCheckedItem(R.id.nav_home)
        }
    }

    private fun setStatusbarColor() {
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        val color = ContextCompat.getColor(
            this,
            R.color.md_theme_light_onSurface
        )
        window.statusBarColor = color
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                collapseTb.title = "Home"
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment()).commit()
            }
            R.id.nav_linkedin -> {
                collapseTb.title = "LinkedIn"
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, LinkedInFragment()).commit()
            }
            R.id.nav_github -> {
                collapseTb.title = "Github"
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, GithubFragment()).commit()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
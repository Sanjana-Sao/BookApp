package com.sanjana.bookapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.navigation.NavigationView
//import android.support.v4.view.GravityCompat
import androidx.core.view.GravityCompat
//import android.support.v4.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import com.sanjana.bookapp.*
import com.sanjana.bookapp.fragment.AboutAppFragment
import com.sanjana.bookapp.fragment.DashboardFragment
import com.sanjana.bookapp.fragment.FavouritesFragment

class MainActivity : AppCompatActivity() {

// xmlns:tools="http://schemas.android.com/tools"
    // tools:replace="android:appComponentFactory">
        lateinit var drawerLayout: DrawerLayout
        lateinit var coordinatorLayout: CoordinatorLayout
        lateinit var toolbar: Toolbar
        lateinit var frameLayout: FrameLayout
        lateinit var navigationView: NavigationView

        var previousMenuItem: MenuItem? = null


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            drawerLayout = findViewById(R.id.drawerLayout)
            coordinatorLayout = findViewById(R.id.coordinatorLayout)
            toolbar = findViewById(R.id.toolbar)
            frameLayout = findViewById(R.id.frame)
            navigationView = findViewById(R.id.navigationView)
            setUpToolbar()


            openDashboard()

            //adding the hamburger icon
            val actionBarDrawerToggle = ActionBarDrawerToggle(
                this@MainActivity,
                drawerLayout,
                R.string.open_drawer,
                R.string.close_drawer
            )

            //this will set the click listner on the hamburger icon(we are enabling the drawer layout to listening the actionBarDraweToggle)
            //this line of code which is making the hamburger icon functional
            drawerLayout.addDrawerListener(actionBarDrawerToggle)
            //this line concerting the backarrowicon<- to hamburger icon and vice versa
            actionBarDrawerToggle.syncState()
            navigationView.setNavigationItemSelectedListener {

                if (previousMenuItem != null){
                    previousMenuItem?.isChecked = false
                }

                it.isCheckable = true
                it.isChecked = true
                previousMenuItem = it

                when(it.itemId){
                    R.id.dashboard -> {
                        openDashboard()
                        drawerLayout.closeDrawers()

                    }
                    R.id.favourites -> {
                        supportFragmentManager.beginTransaction()
                            .replace(
                                R.id.frame,
                                FavouritesFragment()
                            )
                            .commit()

                        supportActionBar?.title = "Favourites"
                        drawerLayout.closeDrawers()
                    }
                    R.id.aboutApp -> {
                        supportFragmentManager.beginTransaction()
                            .replace(
                                R.id.frame,
                                AboutAppFragment()
                            )
                            .commit()

                        supportActionBar?.title = "About App"
                        drawerLayout.closeDrawers()
                    }
                }
                return@setNavigationItemSelectedListener true
            }
        }

    fun setUpToolbar() {
            setSupportActionBar(toolbar)
            supportActionBar?.title = "Toolbar Title"
        //this will enable the home(<-) button and make it active
            supportActionBar?.setHomeButtonEnabled(true)
        //this will display the home(<-) button on the toolbar
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

        }

    //the home button on the action bar is also known as menu item
        override fun onOptionsItemSelected(item: MenuItem): Boolean {

            val id = item.itemId

        //id.home is the id of the hamburger icon
            if (id == android.R.id.home){
                drawerLayout.openDrawer(GravityCompat.START)
            }

            return super.onOptionsItemSelected(item)
        }

        fun openDashboard(){
            val fragment = DashboardFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame, fragment)
            transaction.commit()
            supportActionBar?.title = "Dashboard"
            navigationView.setCheckedItem(R.id.dashboard)
        }


        override fun onBackPressed() {
            val frag = supportFragmentManager.findFragmentById(R.id.frame)

            when(frag){
                !is DashboardFragment -> openDashboard()

                else -> super.onBackPressed()
            }

        }
}
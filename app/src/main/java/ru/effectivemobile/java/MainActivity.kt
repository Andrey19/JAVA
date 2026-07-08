package ru.effectivemobile.java


import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView


class MainActivity : AppCompatActivity() {
    private var bottomNav: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById<BottomNavigationView?>(R.id.bottomNav)

        // Загружаем первый фрагмент при старте (Сортировка)
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, SortFragment())
                .commit()
        }

        // Слушатель нажатий на меню
        bottomNav!!.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                var selectedFragment: Fragment? = null

                val id = item.getItemId()
                if (id == R.id.nav_sort) {
                    selectedFragment = SortFragment()
                } else if (id == R.id.nav_worker) {
                    selectedFragment = WorkerFragment()
                } else if (id == R.id.nav_huffman) {
                    selectedFragment = HuffmanFragment()
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit()
                }
                return true
            }
        })
    }
}
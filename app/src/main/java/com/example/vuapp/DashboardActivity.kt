package com.example.vuapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vuapp.viewmodel.DashboardAdapter
import com.example.vuapp.viewmodel.DashboardViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var adapter: DashboardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Get the keypass from LoginActivity
        val keypass = intent.getStringExtra("keypass") ?: ""
        findViewById<TextView>(R.id.titleTextView).text = "Topic: $keypass"

        // Setup RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = DashboardAdapter(emptyList()) { selectedItem ->
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("entity", DashboardItemWrapper(selectedItem))
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Setup Bottom Navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_logout -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // Collect and observe data
        lifecycleScope.launch {
            dashboardViewModel.items.collectLatest { dashboardItems ->
                adapter.updateData(dashboardItems)
            }
        }

        // Fetch dashboard data
        dashboardViewModel.loadDashboardData(keypass)
    }
}

package com.example.vuapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        // Enable ActionBar back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Details"

        // Get passed entity data
        val wrapper = intent.getSerializableExtra("entity") as? DashboardItemWrapper
        val data = wrapper?.data

        // UI references
        val titleView = findViewById<TextView>(R.id.detailsTitle)
        val contentView = findViewById<TextView>(R.id.detailsContent)

        // Show content
        if (data != null) {
            val title = data.entries.firstOrNull { it.key.lowercase() != "description" }?.value?.toString() ?: "Detail"
            val content = data.entries.joinToString("\n\n") { (key, value) ->
                "${key.replaceFirstChar { it.uppercase() }}: $value"
            }

            titleView.text = title
            contentView.text = content
        } else {
            titleView.text = "No data"
            contentView.text = "This entity is empty or failed to load."
        }
    }

    // Handle ActionBar back button click
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}


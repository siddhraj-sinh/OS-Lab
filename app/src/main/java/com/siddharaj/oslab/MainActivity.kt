package com.siddharaj.oslab

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
  private lateinit var cardView: CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //init card view
        cardView=findViewById(R.id.cardview)

        //onclick listener
        cardView.setOnClickListener {
              showDialog()
        }

    }

    //show dialog
    private fun showDialog() {
        val dialog = Dialog(this, R.style.Theme_Dialog)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_main)
        val tvTheory: TextView = dialog.findViewById(R.id.tv_theory)
        val tvSimulation: TextView = dialog.findViewById(R.id.tv_simulation)
        tvTheory.setOnClickListener {
            //start theory activity
           val intent = Intent(this,TheoryActivity::class.java)
            startActivity(intent)
            dialog.cancel()
        }
        tvSimulation.setOnClickListener {
            //start simulation activity
            val intent = Intent(this,SimulationActivity::class.java)
            startActivity(intent)
            dialog.cancel()
        }
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.about->{
                //start about activity
                val intent = Intent(this,AboutActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }


}

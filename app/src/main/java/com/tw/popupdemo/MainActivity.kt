package com.tw.popupdemo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tw.popupdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val TAG:String = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.tvOne.setOnClickListener {
            showPopUpMenu(binding.tvOne, 0)
        }

        binding.tvTwo.setOnClickListener {
            showPopUpMenu(binding.tvTwo, 1)
        }
    }


    private fun showPopUpMenu(anchor: View?, position: Int) {
        val popup = PopupMenu(anchor!!.context, anchor)
        //Inflating the Popup using xml file
        popup.menuInflater.inflate(R.menu.product_popup_menu, popup.menu)
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener { item ->
            if (item.itemId==R.id.addRating){
                Toast.makeText(anchor.context, "You Clicked ${item.itemId}: " + item.title, Toast.LENGTH_SHORT).show()
            }
            else if (item.itemId==R.id.viewDetails){
                Toast.makeText(anchor.context, "You Clicked ${item.itemId}: " + item.title, Toast.LENGTH_SHORT).show()
            }
            true
        }
        popup.show() //showing popup menu
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        menuInflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu.findItem(R.id.action_search).setVisible(false)
        val shareItem = menu.findItem(R.id.action_share).setVisible(false)

//        val searchView = searchItem.actionView as SearchView
//        searchView.visibility = View.GONE

        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home){
            finish()
        }else if (item.itemId == R.id.action_logout){
            Toast.makeText(this, "You Clicked ${item.itemId}: " + item.title, Toast.LENGTH_SHORT).show()
        }else if (item.itemId == R.id.action_share){
            shareMyApp()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareMyApp() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.app_name))
            var shareMessage = "\nHey!! Let me recommend you this application\n\n"
            shareMessage =
                """
                ${shareMessage}https://play.google.com/store/apps/details?id=${"com.tw.popupdemo"}
                """.trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "choose one"))
        } catch (e: java.lang.Exception) {
            //e.toString();
        }
    }

}
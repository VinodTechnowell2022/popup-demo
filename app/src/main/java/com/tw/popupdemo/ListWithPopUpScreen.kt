package com.tw.popupdemo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tw.popupdemo.databinding.ListWithPopupScreenBinding
import java.util.Locale
import java.util.Objects

class ListWithPopUpScreen : AppCompatActivity() {

    lateinit var binding: ListWithPopupScreenBinding
    val TAG:String = this.javaClass.simpleName

    var dataList: MutableList<StateTable> = mutableListOf()

    private val REQUEST_CODE_SPEECH_INPUT = 11

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ListWithPopupScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData(dataList)
        clicklisteners()

        binding.stoRefresh.setColorSchemeColors(Color.GREEN, Color.RED, Color.YELLOW)
        binding.stoRefresh.setOnRefreshListener {

            Handler(Looper.getMainLooper()).postDelayed({
                // on below line we are setting is refreshing to false.
                binding.stoRefresh.isRefreshing = false
                getData(dataList)
            }, 600)

        }
    }
    private fun getData(dataList: MutableList<StateTable>) {
        dataList.clear()
        dataList.add(StateTable(1, "Haryana", "HR"))
        dataList.add(StateTable(2, "Rajasthan", "RJ"))
        dataList.add(StateTable(3, "Punjab", "PN"))
        dataList.add(StateTable(4, "Gujrat", "Gj"))
        dataList.add(StateTable(5, "Uttar Pradesh", "UP"))
        dataList.add(StateTable(6, "Madhya Pradesh", "MP"))

        resetAdapter( dataList )

        if (dataList.size>0){
            textChangeListener(dataList)
        }
    }

    private fun textChangeListener(dataList: MutableList<StateTable>) {

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                filter(s.toString())
            }
        })

    }

    private fun filter(text: String) {
        val filteredList: ArrayList<StateTable> = ArrayList() // temp list
        if (dataList.size>0)
            for (item in dataList) {
                if (item.name.lowercase().contains(text.lowercase())) {
                    filteredList.add(item)
                }else if (item.code.lowercase().contains(text.lowercase())) {
                    filteredList.add(item)
                }
            }
        //resetting data to adapter
        resetAdapter( filteredList )
    }

    //here we are supply data to adapter
    private fun resetAdapter(dataList: MutableList<StateTable>) {
        binding.homeItemsGrid.removeAllViews()
        val adapter = StatesListAdapter(dataList)
        val layoutManager = LinearLayoutManager(this@ListWithPopUpScreen, LinearLayoutManager.VERTICAL, false )
        binding.homeItemsGrid.layoutManager = layoutManager
        binding.homeItemsGrid.adapter = adapter
    }


    private fun clicklisteners() {
        binding.ivVoice.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")
            voiceActivityResultLauncher.launch(intent)

            try {
                startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
            } catch (e: Exception) {
                // on below line we are displaying error message in toast
                Toast.makeText(this@ListWithPopUpScreen, " " + e.message, Toast.LENGTH_SHORT).show()
            }
        }

        binding.flSort.setOnClickListener {
            val sortedList = dataList.sortedBy { it.code }  //this is kotlin sorting feature on collection list
            resetAdapter(sortedList as MutableList<StateTable> )
            Toast.makeText(this@ListWithPopUpScreen, "List Sorted on Code field", Toast.LENGTH_LONG).show()
        }
    }

    private var voiceActivityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(), ActivityResultCallback {
            if (it.resultCode === RESULT_OK) {
                val res: ArrayList<String> = it.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
                if (dataList.size>0){
                    filter(Objects.requireNonNull(res)[0])
                }
                Toast.makeText(this@ListWithPopUpScreen, "${ Objects.requireNonNull(res)[0] }", Toast.LENGTH_SHORT).show()
            }
        }
    )

}

data class StateTable( var id:Int, var name: String, var code:String)
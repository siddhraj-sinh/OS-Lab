package com.siddharaj.oslab

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import java.util.*
import kotlin.collections.ArrayList


class SimulationActivity : AppCompatActivity() {
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var btnGraphOne: Button
    private lateinit var etReferenceNumbers: EditText
    private lateinit var etPageFrame: EditText
    private lateinit var btnSubmit: Button
    private lateinit var btnCompare:Button
    private lateinit var tvHit:TextView
    private lateinit var tvFaults:TextView
    private lateinit var tvHitRatio:TextView
    private lateinit var tvMissRatio: TextView
    private lateinit var table:TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simulation)

        //init
        autoCompleteTextView = findViewById(R.id.autoCompleteText)
        btnGraphOne = findViewById(R.id.btn_show_graph)
        etReferenceNumbers = findViewById(R.id.et_reference_number)
        etPageFrame = findViewById(R.id.et_page_frames)
        btnSubmit = findViewById(R.id.btn_submit)
        btnCompare = findViewById(R.id.btn_compare)
        tvFaults = findViewById(R.id.tvFaults)
        tvHit = findViewById(R.id.tvHits)
        tvMissRatio=findViewById(R.id.tvMissRatio)
        tvHitRatio = findViewById(R.id.tvHitRatio)
        table = findViewById(R.id.table)

       //making spinner
        val algorithms = resources.getStringArray(R.array.algorithms)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, algorithms)
        autoCompleteTextView.setAdapter(arrayAdapter)


        //submit button onclick
        btnSubmit.setOnClickListener {

            val refNumbers: Array<String> = etReferenceNumbers.getText().toString().split(" ").toTypedArray()
            val pageFrames = etPageFrame.text.toString()



            if(etReferenceNumbers.getText().toString().isNotEmpty() && etPageFrame.text.toString().isNotEmpty()){
                val reference = IntArray(refNumbers.size)

                for (i in refNumbers.indices) {


                        reference[i] = refNumbers[i].toInt()


                }

            when (autoCompleteTextView.text.toString()) {
                "FIFO" -> {
                      FIFO(reference,pageFrames.toInt(),true)
                }
                "LRU" -> {
                   LRU(reference,pageFrames.toInt(),true)

                }
                "OPTIMAL"->{
                    OPTIMAL(reference,pageFrames.toInt(),true)
                }
                "LIFO"->{
                    LIFO(reference,pageFrames.toInt(),true)
                }

            }
            }
            else{
                Toast.makeText(this,"Please fill given fields",Toast.LENGTH_SHORT).show()
            }


        }

        btnGraphOne.setOnClickListener {
            showGraph(autoCompleteTextView.text.toString())
        }

        btnCompare.setOnClickListener {
            val pageFrames = etPageFrame.text.toString()
            if(etReferenceNumbers.text.toString().isNotEmpty() && etPageFrame.text.toString().isNotEmpty()) {

                // navigate to compare activity
                val intent = Intent(this,CompareActivity::class.java)
                intent.putExtra("reference",etReferenceNumbers.getText().toString())
                intent.putExtra("frame",pageFrames)
                startActivity(intent)
            }

        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.simulation_screen_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.help->{
                showHelpDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun showHelpDialog() {
        val dialog = Dialog(this, R.style.Theme_Dialog)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_simulation_screen)

        dialog.show()
    }

    fun FIFO(pageReference: IntArray, pageFrame: Int,showResult:Boolean): Int {
        var pageFaults = 0
        var pageHits = 0
        val tempArray = ArrayList<Int>()
        val queue: Queue<Int> = LinkedList<Int>()

       
        for (i in pageReference) {
            if (tempArray.size < pageFrame) {
                if (!tempArray.contains(i)) {
                    tempArray.add(i)
                    queue.add(i)

                    pageFaults++
                } else {
                    pageHits++
                }
            } else {
                if (!tempArray.contains(i)) {
                    tempArray.removeAt(0)
                    queue.poll()
                    tempArray.add(i)
                    queue.add(i)
                    pageFaults++
                } else {
                    pageHits++
                }

            }
        }

        val hitRatio: Float = pageHits.toFloat() / pageReference.size.toFloat()
        val missRatio: Float = pageFaults.toFloat() / pageReference.size.toFloat()
        if(showResult) {
            displayResult(pageFaults, pageHits, hitRatio, missRatio,pageFrame,pageReference)
        }
        return pageFaults
    }

    private fun LIFO(pageReference: IntArray, pageFrame: Int, b: Boolean) : Int {
        var pageFaults = 0
        var pageHits = 0
        val tempArray = ArrayList<Int>()
        val stack: Stack<Int> = Stack()
        for (i in pageReference) {
            if (tempArray.size < pageFrame) {
                if (!tempArray.contains(i)) {
                    tempArray.add(i)
                    stack.add(i)

                    pageFaults++
                } else {
                    pageHits++
                }
            } else {
                if (!tempArray.contains(i)) {
                    tempArray.removeAt(0)
                    stack.pop()
                    tempArray.add(i)
                    stack.add(i)
                    pageFaults++
                } else {
                    pageHits++
                }

            }
        }

        val hitRatio: Float = pageHits.toFloat() / pageReference.size.toFloat()
        val missRatio: Float = pageFaults.toFloat() / pageReference.size.toFloat()
        if(b) {
            displayResult(pageFaults, pageHits, hitRatio, missRatio,pageFrame,pageReference)
        }
        return pageFaults

    }

    fun LRU(pageReference: IntArray, pageFrame: Int,showResult:Boolean): Int {
        var pageFaults = 0
        var pageHits = 0
        val s: HashSet<Int> = HashSet(pageFrame)

        // To store least recently used indexes
        // of pages.

        // To store least recently used indexes
        // of pages.
        val indexes: HashMap<Int, Int> = HashMap()

        // Start from initial page

        // Start from initial page

        for (i in 0 until pageReference.size) {
            // Check if the set can hold more pages
            if (s.size < pageFrame) {
                // Insert it into set if not present
                // already which represents page fault
                if (!s.contains(pageReference.get(i))) {
                    s.add(pageReference.get(i))

                    // increment page fault
                    pageFaults++
                }

                // Store the recently used index of
                // each page
                indexes[pageReference.get(i)] = i
            } else {
                // Check if current page is not already
                // present in the set
                if (!s.contains(pageReference.get(i))) {
                    // Find the least recently used pages
                    // that is present in the set
                    var lru = Int.MAX_VALUE
                    var num = Int.MIN_VALUE
                    val itr: Iterator<Int> = s.iterator()
                    while (itr.hasNext()) {
                        val temp = itr.next()
                        if (indexes[temp]!! < lru) {
                            lru = indexes[temp]!!
                            num = temp
                        }
                    }

                    // Remove the indexes page
                    s.remove(num)
                    //remove lru from hashmap
                    indexes.remove(num)
                    // insert the current page
                    s.add(pageReference.get(i))

                    // Increment page faults
                    pageFaults++
                }

                // Update the current page index
                indexes[pageReference.get(i)] = i
            }
        }
        pageHits = pageReference.size-pageFaults
        val hitRatio: Float = pageHits.toFloat() / pageReference.size.toFloat()
        val missRatio: Float = pageFaults.toFloat() / pageReference.size.toFloat()

        if(showResult) {
            displayResult(pageFaults, pageHits, hitRatio, missRatio,pageFrame,pageReference)
        }
        return pageFaults
    }

    private fun OPTIMAL(pageReference: IntArray, pageFrame: Int, b: Boolean):Int {
        var pageFaults = 0
        // Set to store the elements present in queue, used to check if a page is present or not
        val set: HashSet<Int> = HashSet()
        // Queue to maintain the order of insertion
        val queue: Queue<Int> = LinkedList()

        // traverse the page string
        for (i in 0 until pageReference.size) {
            // if there are some empty slots
            if (queue.size < pageFrame) {
                if (!set.contains(pageReference[i])) {
                    // and the current page is missing
                    // add the page to set

                    set.add(pageReference.get(i))
                    // add the page to queue
                    queue.add(pageReference.get(i))
                    // it is a page fault, increment faults
                    pageFaults++
                }
            } else {
                // there are no empty slots and if current page is absent
                if (!set.contains(pageReference[i])) {
                    // remove the first page in queue
                    val removedPage: Int = queue.poll()
                    queue.poll()
                    // remove the page from set
                    set.remove(removedPage)

                    // add the new page to set
                    set.add(pageReference.get(i))
                    // add the new page to queue
                    queue.add(pageReference.get(i))

                    // it is page fault, increment faults
                    pageFaults++
                }
            }
        }
        val pageHits:Int = pageReference.size-pageFaults
        val hitRatio: Float = pageHits.toFloat() / pageReference.size.toFloat()
        val missRatio: Float = pageFaults.toFloat() / pageReference.size.toFloat()

        if(b) {
            displayResult(pageFaults, pageHits, hitRatio, missRatio,pageFrame,pageReference)
        }
        return pageFaults
    }

    private fun showGraph(str:String) {
        if(etReferenceNumbers.getText().toString().isNotEmpty() && etPageFrame.text.toString().isNotEmpty()){
            val dialog = Dialog(this, R.style.Theme_Dialog)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.graph_dialog)
            val barChart: BarChart = dialog.findViewById(R.id.barChart)
            val refNumbers: Array<String> = etReferenceNumbers.text.toString().split(" ").toTypedArray()
            val frames = etPageFrame.text.toString().toInt()
            val reference = IntArray(refNumbers.size)

            for (i in refNumbers.indices) {
                reference[i] = refNumbers[i].toInt()
            }

            val x1 = frames
            val x2 = frames+1
            val x3 = frames+2
            val x4 =frames+3
            val x5 = frames+4
            val x6 = frames+5
            var y1 = 0
            var y2 = 0
            var y3 = 0
            var y4 = 0
            var y5 = 0
            var y6 = 0
   when(str){
       "FIFO"->{
            y1 = FIFO(reference,frames,false)
            y2 = FIFO(reference,frames+1,false)
            y3 = FIFO(reference,frames+2,false)
            y4 = FIFO(reference,frames+3,false)
            y5 = FIFO(reference,frames+4,false)
            y6 = FIFO(reference,frames+5,false)
       }
       "LIFO"->{
           y1 = LIFO(reference,frames,false)
           y2 = LIFO(reference,frames+1,false)
           y3 = LIFO(reference,frames+2,false)
           y4 = LIFO(reference,frames+3,false)
           y5 = LIFO(reference,frames+4,false)
           y6 = LIFO(reference,frames+5,false)
       }
       "LRU"->{
           y1 = LRU(reference,frames,false)
           y2 = LRU(reference,frames+1,false)
           y3 = LRU(reference,frames+2,false)
           y4 = LRU(reference,frames+3,false)
           y5 = LRU(reference,frames+4,false)
           y6 = LRU(reference,frames+5,false)
       }
       "OPTIMAL"->{
           y1 = OPTIMAL(reference,frames,false)
           y2 = OPTIMAL(reference,frames+1,false)
           y3 = OPTIMAL(reference,frames+2,false)
           y4 = OPTIMAL(reference,frames+3,false)
           y5 = OPTIMAL(reference,frames+4,false)
           y6 = OPTIMAL(reference,frames+5,false)
       }

   }


            val values = ArrayList<BarEntry>()

            values.add(BarEntry(x1.toFloat(), y1.toFloat()))
            values.add(BarEntry(x2.toFloat(), y2.toFloat()))
            values.add(BarEntry(x3.toFloat(), y3.toFloat()))
            values.add(BarEntry(x4.toFloat(), y4.toFloat()))
            values.add(BarEntry(x5.toFloat(), y5.toFloat()))
            values.add(BarEntry(x6.toFloat(), y6.toFloat()))

            val dataSet: BarDataSet = BarDataSet(values, "Page faults")
            dataSet.color = Color.parseColor("#FF03DAC5")
            dataSet.valueTextColor = Color.BLACK
            dataSet.valueTextSize = 16f

            val barData = BarData(dataSet)
            barChart.setFitBars(true)
            barChart.data = barData
            barChart.description.text = "Bar Chart"
            barChart.animateY(2000)

            dialog.show()
        }
        else{
            Toast.makeText(this,"Please fill given fields",Toast.LENGTH_SHORT).show()
        }

    }


    private fun displayResult(pageFaults: Int, pageHits: Int, hitRatio: Float, missRatio: Float, frames: Int, pageReference: IntArray) {


        //set visibility visible for result
        tvFaults.visibility= View.VISIBLE
        tvHit.visibility=View.VISIBLE
        tvHitRatio.visibility=View.VISIBLE
        tvMissRatio.visibility=View.VISIBLE

        //set result text
        tvFaults.text = getString(R.string.text_fault,pageFaults)
        tvHit.text = getString(R.string.text_hit,pageHits)
        tvHitRatio.text = getString(R.string.text_hit_ratio,hitRatio)
        tvMissRatio.text = getString(R.string.text_miss_ratio,missRatio)

    }


}

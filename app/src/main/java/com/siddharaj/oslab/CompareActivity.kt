package com.siddharaj.oslab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.siddharaj.oslab.databinding.ActivityCompareBinding
import java.util.*
import kotlin.collections.ArrayList

class CompareActivity : AppCompatActivity() {

    private lateinit var binding:ActivityCompareBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pageFrame = intent.getStringExtra("frame")
        val pageReference = intent.getStringExtra("reference")
        val refNumbers: Array<String> = pageReference?.split(" ")!!.toTypedArray()


        if (pageReference != null && pageFrame != null) {
            val reference = IntArray(refNumbers.size)

            for (i in refNumbers.indices) {
                reference[i] = refNumbers[i].toInt()
            }
            FIFO(reference, pageFrame.toInt())
            LIFO(reference, pageFrame.toInt())
            LRU(reference, pageFrame.toInt())
            OPTIMAL(reference, pageFrame.toInt())
        }


    }

    private fun FIFO(pageReference: IntArray, pageFrame: Int) {
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

       binding.fFifo.text="Faults :$pageFaults"
        binding.hFifo.text="Hits :$pageHits"
        binding.mrFifo.text="MissRatio :$missRatio"
        binding.hrFifo.text="HitRatio :$hitRatio"
    }

    private fun LIFO(pageReference: IntArray, pageFrame: Int) {
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

        binding.fLifo.text="Faults :$pageFaults"
        binding.hLifo.text="Hits :$pageHits"
        binding.mrLifo.text="MissRatio :$missRatio"
        binding.hrLifo.text="HitRatio :$hitRatio"
    }

    private fun LRU(pageReference: IntArray, pageFrame: Int) {
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
        pageHits = pageReference.size - pageFaults
        val hitRatio: Float = pageHits.toFloat() / pageReference.size.toFloat()
        val missRatio: Float = pageFaults.toFloat() / pageReference.size.toFloat()

        binding.fLru.text="Faults :$pageFaults"
        binding.hLru.text="Hits :$pageHits"
        binding.mrLru.text="MissRatio :$missRatio"
        binding.hrLru.text="HitRatio :$hitRatio"
    }

    private fun OPTIMAL(pageReference: IntArray, pageFrame: Int) {
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
        val pageHits: Int = pageReference.size - pageFaults
        val hitRatio: Float = pageHits.toFloat() / pageReference.size.toFloat()
        val missRatio: Float = pageFaults.toFloat() / pageReference.size.toFloat()

        binding.fOptimal.text="Faults :$pageFaults"
        binding.hOptimal.text="Hits :$pageHits"
        binding.mrOptimal.text="MissRatio :$missRatio"
        binding.hrOptimal.text="HitRatio :$hitRatio"
    }
}

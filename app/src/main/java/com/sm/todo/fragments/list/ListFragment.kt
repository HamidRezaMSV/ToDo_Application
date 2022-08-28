package com.sm.todo.fragments.list

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sm.todo.R
import com.sm.todo.data.models.ToDoData
import com.sm.todo.data.viewmodel.ToDoViewModel
import com.sm.todo.databinding.FragmentListBinding
import com.sm.todo.fragments.SharedViewModel
import com.sm.todo.fragments.list.adapter.ListAdapter
import com.sm.todo.utils.hideKeyboard
import com.sm.todo.utils.observeOnce
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

// Remember to import : androidx.appcompat.widget.SearchView

class ListFragment : Fragment() , SearchView.OnQueryTextListener {

    private var _binding : FragmentListBinding? = null
    private val binding get() = _binding!!

    private val myAdapter : ListAdapter by lazy { ListAdapter() }
    private val mToDoViewModel : ToDoViewModel by viewModels()
    private val mSharedViewModel : SharedViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentListBinding.inflate(inflater , container , false)
        binding.lifecycleOwner = this // Because we have liveData in sharedViewModel and want to observe from bindingAdapter
        binding.mSharedViewModel = mSharedViewModel

        // Set Menu :
        setHasOptionsMenu(true)

        // Get ToDoData List :
        mToDoViewModel.getAllData.observe(viewLifecycleOwner) { data ->
            mSharedViewModel.checkIfDatabaseEmpty(data)
            myAdapter.setData(data)
        }

        // Init RecyclerView :
        setupRecyclerView()

        // Hide soft keyboard :
        hideKeyboard(requireActivity())

        return binding.root
    }

    private fun setupRecyclerView(){
        binding.recyclerView.adapter = myAdapter
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2 , StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.itemAnimator = SlideInUpAnimator().apply { addDuration = 300 }

        // Swipe to delete :
        swipeToDelete(binding.recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDeleteCallback = object : SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = myAdapter.dataList[viewHolder.adapterPosition]
                // Delete item :
                mToDoViewModel.deleteItem(itemToDelete)
                myAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                // Restore deleted item :
                restoreDeletedData(viewHolder.itemView , itemToDelete )
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view: View , deletedItem:ToDoData){
        val snackBar = Snackbar.make(view , "'${deletedItem.title}' Deleted !" , Snackbar.LENGTH_LONG)
        snackBar.setAction("Undo"){
            mToDoViewModel.insertData(deletedItem)
        }
        snackBar.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu , menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_delete_all -> { deleteAll() }
            R.id.menu_priority_high -> { mToDoViewModel.sortByHighPriority.observe(viewLifecycleOwner){ myAdapter.setData(it)} }
            R.id.menu_priority_low -> { mToDoViewModel.sortByLowPriority.observe(viewLifecycleOwner){ myAdapter.setData(it)} }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAll() {
        val builder = AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setPositiveButton("Yes"){ _,_ ->
                mToDoViewModel.deleteAll()
                Toast.makeText(requireContext(), "All items deleted successfully", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No"){ _,_ -> }
            .setTitle("Delete")
            .setMessage("Are you sure to delete all items ?")
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null){ searchThroughDatabase(query) }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null){ searchThroughDatabase(newText) }
        return true
    }

    private fun searchThroughDatabase(query:String) {
        val searchQuery = "%$query%"

        mToDoViewModel.searchDatabase(searchQuery).observeOnce(viewLifecycleOwner){ data ->
            data?.let { myAdapter.setData(it) }
        }
    }
}
package com.sm.todo.fragments.update

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sm.todo.R
import com.sm.todo.data.models.ToDoData
import com.sm.todo.data.viewmodel.ToDoViewModel
import com.sm.todo.databinding.FragmentUpdateBinding
import com.sm.todo.fragments.SharedViewModel

class UpdateFragment : Fragment() {

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<UpdateFragmentArgs>()
    private val mSharedViewModel : SharedViewModel by viewModels()
    private val mToDoViewModel : ToDoViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentUpdateBinding.inflate(inflater , container ,false)

        // Set Menu :
        setHasOptionsMenu(true)

        // Get data from args and Show Current ToDoData :
        binding.toDoData = args.currentItem

        binding.currentPrioritiesSpinner.onItemSelectedListener = mSharedViewModel.listener

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu , menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_save -> { updateItem() }
            R.id.menu_delete -> { deleteItem() }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateItem(){
        val newTitle = binding.currentTitleEt.text.toString()
        val newDescription = binding.currentDescriptionEt.text.toString()
        val newPriority = binding.currentPrioritiesSpinner.selectedItem.toString()

        val validation = mSharedViewModel.verifyDataFromUser(newTitle,newDescription)
        if (validation){
            // Update Current Item :
            val updatedItem = ToDoData(
                id = args.currentItem.id ,
                title = newTitle ,
                priority = mSharedViewModel.parsePriority(newPriority) ,
                description = newDescription
            )
            mToDoViewModel.updateData(updatedItem)
            Toast.makeText(requireContext(), "Successfully Updated!", Toast.LENGTH_SHORT).show()
            // Navigate back to list fragment :
            navigateBackToList()
        }else{
            mSharedViewModel.notifyToCompleteData(newTitle,newDescription,requireContext())
        }
    }

    private fun deleteItem(){
        val builder = AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setPositiveButton("Yes"){ _,_ ->
            mToDoViewModel.deleteItem(args.currentItem)
            Toast.makeText(requireContext(), "'${args.currentItem.title}' deleted successfully", Toast.LENGTH_SHORT).show()
            navigateBackToList()
        }
            .setNegativeButton("No"){ _,_ -> }
            .setTitle("Delete")
            .setMessage("Are you sure to delete '${args.currentItem.title}' ?")
        builder.create().show()
    }

    private fun navigateBackToList(){
        findNavController().navigate(UpdateFragmentDirections.actionUpdateFragmentToListFragment())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
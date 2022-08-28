package com.sm.todo.fragments.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sm.todo.R
import com.sm.todo.data.models.ToDoData
import com.sm.todo.data.viewmodel.ToDoViewModel
import com.sm.todo.databinding.FragmentAddBinding
import com.sm.todo.fragments.SharedViewModel

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val mToDoViewModel : ToDoViewModel by viewModels()
    private val mSharedViewModel : SharedViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        // Set Menu
        setHasOptionsMenu(true)

        binding.prioritiesSpinner.onItemSelectedListener = mSharedViewModel.listener

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            insertDataToDatabase()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDatabase() {
        val mTitle = binding.titleEt.text.toString()
        val mDescription = binding.descriptionEt.text.toString()
        val mPriority = binding.prioritiesSpinner.selectedItem.toString()

        val validation = mSharedViewModel.verifyDataFromUser(mTitle,mDescription)
        if (validation){
            // insert data to database
            val newData = ToDoData(
                id = 0 ,
                title = mTitle ,
                priority = mSharedViewModel.parsePriority(mPriority) ,
                description = mDescription
            )
            mToDoViewModel.insertData(newData)
            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()
            navigateBackToList()
        }else{
            mSharedViewModel.notifyToCompleteData(mTitle,mDescription,requireContext())
        }
    }

    private fun navigateBackToList() {
        findNavController().navigate(AddFragmentDirections.actionAddFragmentToListFragment())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.udacity.asteroidradar.main

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    //Add an adapter to update the list later, using the menu options
    private lateinit var asteroidAdapter: AsteroidAdapter

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        //Set the AsteroidAdapter to the RecyclerView in Main Fragment.
        // Add click listener which takes the Asteroid selected and passes it to the
        // viewModel function that will set the variable to enact navigation to the Detail Fragment
        binding.asteroidRecycler.adapter = AsteroidAdapter(AsteroidAdapter.AsteroidClickListener {
            viewModel.displaySelectedAsteroidDetails(it)
        }).apply { asteroidAdapter=this }


        //Set observer to look for a change in the showSelectedAsteroid variable
        //If this is non-null, then action navigation
        viewModel.showSelectedAsteroid.observe(viewLifecycleOwner, Observer {
            Log.i("Check", "Nav called")
            if (null != it) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.doneDisplayingAsteroidDetails()
            }

        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_all_menu -> viewModel.menuShowAll()
            R.id.show_today_menu -> viewModel.menuShowToday()
            R.id.show_week_menu -> viewModel.menuShowWeek()
        }
        //Set an observer on the asteroid list
        observeAsteroids()
        return true
    }

    //Observes list and calls submitList with new data when changed
    private fun observeAsteroids() {
        viewModel.showAsteroidList.observe(viewLifecycleOwner) {
            asteroidAdapter.submitList(it)
        }
    }
}

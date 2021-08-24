package com.udacity.asteroidradar.main

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setImageOfTheDay(binding)

        //Set the AsteroidAdapter to the RecyclerView in Main Fragment.
        // Add click listener which takes the Asteroid selected and passes it to the
        // viewModel function that will set the variable to enact navigation to the Detail Fragment
        binding.asteroidRecycler.adapter = AsteroidAdapter(AsteroidAdapter.AsteroidClickListener {
            viewModel.displaySelectedAsteroidDetails(it)
        })

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

    private fun setImageOfTheDay(binding: FragmentMainBinding) {
            //Use Picasso to download the Image of the day, and set it to the imageView
            Picasso.with(context).load(viewModel.dailyImage.value?.url)
                .into(binding.activityMainImageOfTheDay)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}

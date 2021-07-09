package com.example.apitest

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apitest.databinding.ActivityMainBinding
import retrofit2.HttpException
import java.io.IOException

const val TAG = "MainActivity"

    class MainActivity : AppCompatActivity() {
        private lateinit var binding: ActivityMainBinding

        private var people: ArrayList<User> = arrayListOf()
        private var matchedPeople: ArrayList<User> = arrayListOf()
        private var userAdapter: UserAdapter = UserAdapter(people)

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            lifecycleScope.launchWhenCreated {
                binding.progressBar.isVisible = true
                val response = try {
                    RetrofitInstance.API.getTodos()

                } catch(e: IOException) {
                    Log.e(TAG, "IOException, you might not have internet connection")
                    binding.progressBar.isVisible = false
                    return@launchWhenCreated
                } catch (e: HttpException) {
                    Log.e(TAG, "HttpException, unexpected response")
                    binding.progressBar.isVisible = false
                    return@launchWhenCreated
                }
                if(response.isSuccessful && response.body() != null) {
                    userAdapter.users = response.body()!!
                    people = response.body()!!
                } else {
                    Log.e(TAG, "Response not successful")
                }
                binding.progressBar.isVisible = false
            }
            setupRecyclerView()
            performSearch()

        }


        private fun setupRecyclerView() = binding.rvTodos.apply {
            userAdapter = UserAdapter(people).also {
                binding.rvTodos.adapter = it
                binding.rvTodos.adapter!!.notifyDataSetChanged()
            }
            binding.searchView.isSubmitButtonEnabled = true
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        private fun performSearch() {
            binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    search(query)
                    return true
            }
                override fun onQueryTextChange(newText: String?): Boolean {
                    search(newText)
                    return true

                }
            })
        }

        private fun search(text: String?) {
            matchedPeople = arrayListOf()

            text?.let {
                people.forEach { person ->
                    if (person.username.contains(text, true) || person.email.contains(text, true) ) {
                        matchedPeople.add(person)
                    }
                }
                updateRecyclerView()
                if (matchedPeople.isEmpty()) {
                    Toast.makeText(this, "No match found!", Toast.LENGTH_SHORT).show()
                }
                updateRecyclerView()
            }
        }
        private fun updateRecyclerView() {
            binding.rvTodos.apply {
                userAdapter.users = matchedPeople
                userAdapter.notifyDataSetChanged()
            }
        }

    }
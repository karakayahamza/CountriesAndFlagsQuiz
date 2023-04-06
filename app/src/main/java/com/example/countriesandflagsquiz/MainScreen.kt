package com.example.countriesandflagsquiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.work.*
import java.util.concurrent.TimeUnit

class MainScreen : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val data = Data.Builder().putInt("intKey",1).build()

        val constraints = Constraints.Builder()
            //.setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(false)
            .build()

        val myWorkRequest : PeriodicWorkRequest = PeriodicWorkRequestBuilder<WorkManagerRefreshData>(15,TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        WorkManager.getInstance(requireContext()).enqueue(myWorkRequest)

        WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(myWorkRequest.id).observe(viewLifecycleOwner,
            Observer {
                if (it.state == WorkInfo.State.RUNNING){
                    println("Running...")
                }
                else if (it.state == WorkInfo.State.FAILED){
                    println("Failed...")
                }
                else if (it.state == WorkInfo.State.SUCCEEDED){
                    println("Succeesed.")
                }
            })

        //WorkManager.getInstance(this.requireContext()).cancelAllWork()

        //Chaining
        val oneTimeWorkRequest : OneTimeWorkRequest = OneTimeWorkRequestBuilder<WorkManagerRefreshData>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()
        WorkManager.getInstance(requireContext()).beginWith(oneTimeWorkRequest)
            .then(oneTimeWorkRequest)
            .then(oneTimeWorkRequest)
            .enqueue()
        return inflater.inflate(R.layout.fragment_main_screen, container, false)
    }
}
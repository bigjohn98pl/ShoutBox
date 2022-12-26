package com.example.lab9.ui.shoutbox

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab9.JsonPlaceholderAPI
import com.example.lab9.Message
import com.example.lab9.MessageAdapter
import com.example.lab9.R
import com.example.lab9.databinding.FragmentShoutboxBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ShoutBoxFragment : Fragment() {

private var _binding: FragmentShoutboxBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!
  private var list = ArrayList<Message>()
  @SuppressLint("ResourceType")
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val shoutBoxViewModel =
            ViewModelProvider(this).get(ShoutBoxViewModel::class.java)

    _binding = FragmentShoutboxBinding.inflate(inflater, container, false)
    val root: View = binding.root
      val item = Message("Lolo","Janek","33:55:22","fwfsdfsdfs")
      val item2 = Message("Lolo","Janek","33:55:22","fwfsdfsdfs")
      //list.add(item)
      //list.add(item2)
    //val textView: TextView = binding.textHome
  if(isOnline(this.requireContext(),true)){
    val recyclerView: RecyclerView = binding.recyclerViewMessages
    getMessages()
    //Log.e("onResponse", favData)
    Log.e("onResponse mess global3", list.toString())
    recyclerView.adapter = MessageAdapter(list)
    recyclerView.layoutManager = LinearLayoutManager(this.context)
    recyclerView.setHasFixedSize(true)

  }

//    shoutBoxViewModel.text.observe(viewLifecycleOwner) {
//      textView.text = it
//    }

    return root
  }
  @SuppressLint("SuspiciousIndentation")
  private fun getMessages(){
    val myAPI = Retrofit.Builder()
      .baseUrl(JsonPlaceholderAPI.BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .build()
      .create(JsonPlaceholderAPI::class.java)

      myAPI.getMessages().enqueue(object : Callback<ArrayList<Message>> {
        override fun onResponse(call: Call<ArrayList<Message>>, response: Response<ArrayList<Message>>) {
          if (response.isSuccessful) {
            val gson = Gson()
            val favData = gson.toJson(response.body())
            Log.e("onResponse", favData)
            Log.e("onResponse mess global1", list.toString())
            list = response.body()!!
            val recyclerView: RecyclerView = binding.recyclerViewMessages
            recyclerView.adapter = MessageAdapter(list)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.setHasFixedSize(true)
            Log.e("onResponse mess global2", list.toString())
          }
        }
        override fun onFailure(call: Call<ArrayList<Message>>, t: Throwable) {
          Log.e("ShoutBox", "ERROR: $t")
          //Toast.makeText(this.Con,"ERROR: $t", Toast.LENGTH_LONG).show()
        }

      })
  }
  private fun isOnline(context: Context, toast: Boolean): Boolean {
    val connectivityManager =
      context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
      connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
      if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
        //Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
        if(toast){
          Toast.makeText(this.context,"There is CELLULAR connection!",Toast.LENGTH_SHORT).show()
        }
        return true
      } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
        //Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
        if(toast){
          Toast.makeText(this.context,"There is WIFI connection!",Toast.LENGTH_SHORT).show()
        }
        return true
      } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
        //Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
        if(toast){
          Toast.makeText(this.context,"There is ETHERNET connection!",Toast.LENGTH_SHORT).show()
        }
        return true
      }
    }
    if(toast){
      Toast.makeText(this.context,"There is no connection!",Toast.LENGTH_SHORT).show()
    }
    return false
  }
override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
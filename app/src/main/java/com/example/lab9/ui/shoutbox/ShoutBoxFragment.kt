package com.example.lab9.ui.shoutbox

import android.annotation.SuppressLint
import android.app.ActionBar
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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab9.*
import com.example.lab9.databinding.FragmentShoutboxBinding
import com.google.android.material.snackbar.Snackbar
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
  private var myLogin = "DefaultMan"
  private var list = ArrayList<Message>()
  private val myAPI = Retrofit.Builder()
    .baseUrl(JsonPlaceholderAPI.BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(JsonPlaceholderAPI::class.java)
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
    myLogin = loadData("KEY_LOGIN","DefaultMan")
    binding.button2
      .setOnClickListener{
        addMessage()
        getMessages()
      }
  if(isOnline(this.requireContext(),true)){
    getMessages()
  }
  binding.swipelayout.setOnRefreshListener {
    if(isOnline(this.requireContext(),true)){
      getMessages()
    }
    binding.swipelayout.isRefreshing = false
  }
    val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
      override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //if (myLogin == viewHolder.itemView.findViewById<TextView>(R.id.loginValue).text.toString()) {
          val position = viewHolder.adapterPosition
          list.removeAt(position)
          binding.recyclerViewMessages.adapter?.notifyItemRemoved(position)
          deleteComment(viewHolder.itemView.findViewById<TextView>(R.id.idValue).text.toString())
        //}
        getMessages()
      }
    }
    val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
    itemTouchHelper.attachToRecyclerView(binding.recyclerViewMessages)
//    shoutBoxViewModel.text.observe(viewLifecycleOwner) {
//      textView.text = it
//    }

    return root
  }
  @SuppressLint("SuspiciousIndentation")
  private fun getMessages(){
    binding.swipelayout.isRefreshing = true

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
            recyclerView.scrollToPosition(list.size-1)
            binding.swipelayout.isRefreshing = false
          }
        }
        override fun onFailure(call: Call<ArrayList<Message>>, t: Throwable) {
          Log.e("ShoutBox", "ERROR: $t")
          //Toast.makeText(this.Con,"ERROR: $t", Toast.LENGTH_LONG).show()
        }

      })
  }

  private fun addMessage() {

    myAPI.postMessage(AddMessage( myLogin,getMessageContent())).enqueue(object : Callback<Message> {
      override fun onFailure(call: Call<Message>, t: Throwable) {
        Log.e("ShoutBox", "ERROR: $t")
        Snackbar.make(
          binding.root,
          "Something goes wrong :(",
          Snackbar.LENGTH_SHORT
        ).show()
      }

      override fun onResponse(call: Call<Message>, response: Response<Message>) {
        if (response.isSuccessful) {
          Snackbar.make(
            binding.root,
            "Message sent ".plus(myLogin).plus("!"),
            Snackbar.LENGTH_SHORT
          ).show()
        }

        val recycler_view = binding.recyclerViewMessages
        if(recycler_view != null) {
          recycler_view.scrollToPosition(list.size - 1)
        }
      }
    })
  }

  private fun deleteComment(id: String) {

    myAPI.deleteMessage(id).enqueue(object : Callback<Void> {
      override fun onFailure(call: Call<Void>, t: Throwable) {
        Snackbar.make(
          binding.root,
          "Something goes wrong :(",
          Snackbar.LENGTH_SHORT
        ).show()
      }

      override fun onResponse(call: Call<Void>, response: Response<Void>) {
        Snackbar.make(
          binding.root,
          "Message is deleted!",
          Snackbar.LENGTH_SHORT
        ).show()
      }
    })
  }

  private fun editComment(id: String, login: String, content: String) {

    myAPI.editMessage(id, login, content).enqueue(object : Callback<Void> {
      override fun onFailure(call: Call<Void>, t: Throwable) {
        Log.e("EditPostActivity", "ERROR: $t")
      }

      override fun onResponse(call: Call<Void>, response: Response<Void>) {

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
        if(toast){
          Toast.makeText(this.context,"There is CELLULAR connection!",Toast.LENGTH_SHORT).show()
        }
        return true
      } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
        if(toast){
          Toast.makeText(this.context,"There is WIFI connection!",Toast.LENGTH_SHORT).show()
        }
        return true
      } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
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
  private fun loadData(KEY: String, default: String): String {
    val pref = this.activity?.getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
    return pref?.getString(KEY, default)!!
  }
  private fun getMessageContent(): String{
    return binding.messageToSend.editableText.toString()
  }
override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
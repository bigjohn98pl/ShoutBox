package com.example.lab9.ui.shoutbox

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab9.Message
import com.example.lab9.MessageAdapter
import com.example.lab9.R
import com.example.lab9.databinding.FragmentShoutboxBinding

class ShoutBoxFragment : Fragment() {

private var _binding: FragmentShoutboxBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

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
    var list = ArrayList<Message>()
      val item = Message("Lolo","Janek","33:55:22","fwfsdfsdfs")
      val item2 = Message("Lolo","Janek","33:55:22","fwfsdfsdfs")
      list.add(item)
      list.add(item2)
    val textView: TextView = binding.textHome
    val recyclerView: RecyclerView = binding.recyclerViewMessages
      recyclerView.adapter = MessageAdapter(list)
      recyclerView.layoutManager = LinearLayoutManager(this.context)
      recyclerView.setHasFixedSize(true)

    shoutBoxViewModel.text.observe(viewLifecycleOwner) {
      textView.text = it
    }
    return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
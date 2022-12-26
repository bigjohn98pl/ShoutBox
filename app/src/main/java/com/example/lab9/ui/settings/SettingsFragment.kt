package com.example.lab9.ui.settings

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lab9.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

private var _binding: FragmentSettingsBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  @SuppressLint("CommitPrefEdits")
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {

    val settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

    _binding = FragmentSettingsBinding.inflate(inflater, container, false)
    val root: View = binding.root
    val editView: EditText = binding.putLogin
      val textView = editView as TextView
    settingsViewModel.text.observe(viewLifecycleOwner) {
        //textView.text = it
    }

      val setButton: Button = binding.button
      setButton.setOnClickListener{
          saveData("KEY_LOGIN",binding.putLogin.editableText.toString())
          //binding.textView2.text = loadData( "KEY_LOGIN","Maniur")
      }
    return root
  }
     fun saveData(KEY: String,data: String){
        val pref = this.activity?.getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        val editor = pref?.edit()
        editor?.putString(KEY,data)
        editor?.apply()
    }
     fun loadData(KEY: String,default: String): String? {
        val pref = this.activity?.getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        return pref!!.getString(KEY, default)
    }
override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
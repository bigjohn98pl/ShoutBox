package com.example.lab9

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MessageAdapter(private val messageList: List<Message>): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>(){

    var myParent: ViewGroup? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_message,
            parent, false
        )
        myParent = parent
        return MessageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val currentMessage = messageList[position]

        holder.msgID.text = currentMessage.id
        holder.login.text = currentMessage.login
        holder.date.text = currentMessage.date
        holder.messText.text = currentMessage.content
    }

    override fun getItemCount() = messageList.size

    inner class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),View.OnLongClickListener {
        init {
            itemView.setOnLongClickListener(this)
        }
        val thisContext = this.itemView.context
        val msgID: TextView = itemView.findViewById(R.id.idValue)
        var login: TextView = itemView.findViewById(R.id.loginValue)
        val date: TextView = itemView.findViewById(R.id.dateValue)
        val messText: TextView = itemView.findViewById(R.id.messageValue)

        override fun onLongClick(view: View): Boolean {
            Toast.makeText(
                view.context,
                "long click ".plus(login.text.toString()),
                Toast.LENGTH_SHORT
            ).show()

            val dialog = AlertDialog.Builder(this.itemView.rootView.context)
            val dialogView = LayoutInflater.from(myParent?.context).inflate(R.layout.edit_dialog_layout, myParent, false)

            val editLogin: EditText = dialogView.findViewById(R.id.editLogin)
            val textView: TextView = editLogin as TextView
            val editMessage: EditText = dialogView.findViewById(R.id.editMessage)
            val textView2: TextView = editMessage as TextView

            textView2.text = messText.text.toString()
            textView.text = login.text.toString()

            dialog.setView(dialogView)
                .setPositiveButton("Set",
                    DialogInterface.OnClickListener { dialog, id ->
                            val api = Retrofit.Builder()
                                .baseUrl(JsonPlaceholderAPI.BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()
                                .create(JsonPlaceholderAPI::class.java)

                            api.editMessage(msgID.text.toString(), login.text.toString(), messText.text.toString()).enqueue(object : Callback<Void> {
                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    Log.e("EditPostActivity", "ERROR: $t")
                                    Toast.makeText(thisContext,"Not gut :( ",Toast.LENGTH_SHORT).show()
                                }

                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    Toast.makeText(thisContext,"You edited message!",Toast.LENGTH_SHORT).show()
                                }
                            })
                    })
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->

                    })
            dialog.show()

            return true
        }
    }


}
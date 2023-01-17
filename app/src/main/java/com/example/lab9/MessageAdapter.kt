package com.example.lab9

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab9.ui.shoutbox.ShoutBoxFragment
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MessageAdapter(private val shoutBoxFragment: ShoutBoxFragment, private val messageList: List<Message>): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>(){

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
        val pref = this.itemView.context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        val theLogin = pref!!.getString("KEY_LOGIN", "DefaultMan")
        val myAPI = Retrofit.Builder()
            .baseUrl(JsonPlaceholderAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JsonPlaceholderAPI::class.java)


        override fun onLongClick(view: View): Boolean {

            val dialog = AlertDialog.Builder(this.itemView.rootView.context)
            val dialogView = LayoutInflater.from(myParent?.context).inflate(R.layout.edit_dialog_layout, myParent, false)

            val editLogin: TextView = dialogView.findViewById(R.id.editLogin)
            val editMessage: EditText = dialogView.findViewById(R.id.editMessage)
            val textId: TextView = dialogView.findViewById(R.id.id_Message)

            textId.text = msgID.text.toString()
            editMessage.setText(messText.text.toString())
            editLogin.text = login.text.toString()

            Toast.makeText(view.context, "long click, your login:".plus(theLogin), Toast.LENGTH_SHORT).show()

            dialog.setView(dialogView)
                .setPositiveButton("Edit",
                    DialogInterface.OnClickListener { dialog, id ->

                        if (this.login.text == theLogin) { myAPI.editMessage(msgID.text.toString(), editLogin.text.toString(), editMessage.editableText.toString()
                            ).enqueue(object : Callback<Void> {

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    Toast.makeText(thisContext, "Not gut :( ", Toast.LENGTH_SHORT).show()
                                    shoutBoxFragment.getMessages()
                                }
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    Toast.makeText(thisContext, "You edited message!", Toast.LENGTH_SHORT).show()
                                    shoutBoxFragment.getMessages()
                                }
                            })
                        }else{
                            Snackbar.make(itemView, "This is not you!", Snackbar.LENGTH_SHORT).show()
                        }
                    })
                .setNegativeButton("Delete",
                    DialogInterface.OnClickListener { dialog, id ->
                        if (this.login.text == theLogin) {
                            shoutBoxFragment.deleteComment(textId.text.toString())
                            shoutBoxFragment.getMessages()
                        }else{
                            Snackbar.make(itemView, "This is not you!", Snackbar.LENGTH_SHORT).show()
                            shoutBoxFragment.getMessages()
                        }
                    })
            dialog.show()

            return true
        }
    }

}
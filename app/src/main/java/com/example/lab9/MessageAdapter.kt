package com.example.lab9

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter(private val messageList: List<Message>): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_message,
            parent, false)

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

    class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val msgID: TextView = itemView.findViewById(R.id.idValue)
        val login: TextView = itemView.findViewById(R.id.loginValue)
        val date: TextView = itemView.findViewById(R.id.dateValue)
        val messText: TextView = itemView.findViewById(R.id.messageValue)
    }
}
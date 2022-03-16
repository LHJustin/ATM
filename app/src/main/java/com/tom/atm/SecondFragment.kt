package com.tom.atm

import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.tom.atm.databinding.FragmentSecondBinding
import com.tom.atm.databinding.RowChatroomBinding
import okhttp3.*
import okio.ByteString
import java.net.URL
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {
    val chatRooms = listOf<ChatRoom>(
        ChatRoom("101101","Miau", "Welcome"),
        ChatRoom("101102","Miau2", "Welcome2"),
        ChatRoom("101103","Miau3", "Welcome3"),
        ChatRoom("101104","Miau4", "Welcome4")
    )
    //test chatroom list裡的變數
    var rooms = mutableListOf<Lightyear>()
    private val TAG = MainActivity ::class.java.simpleName
    private var _binding: FragmentSecondBinding? = null
    lateinit var websocket : WebSocket

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        //Web socket
        val client = OkHttpClient.Builder()
            .readTimeout(3, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder()
            .url("wss://lott-dev.lottcube.asia/ws/chat/chat:app_test?nickname=Justin")
            .build()
        websocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                Log.d(TAG, "onClosed: ")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                Log.d(TAG, "onClosing: ")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Log.d(TAG, "onFailure: ")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Log.d(TAG, "onMessage: $text")
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
                Log.d(TAG, "onMessage: ${bytes.hex()}")
            }

            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.d(TAG, "onOpen: ")
//                webSocket.send("Hello,I am Hank")
            }
        })
        //按下按鈕讓edMessange裡的文字按照json的規格傳上json上
        binding.bSend.setOnClickListener(){
            val message = binding.edMessage.text.toString()
            val json = "{\"action\":\"N\", \"content\": \"$message\"}"
//            websocket.send(json)
            val j = Gson().toJson(Messagej("N", message))
            websocket.send(j)
        }
        //Recycler's Adapter
        binding.recycler.setHasFixedSize(true)
        binding.recycler.layoutManager = GridLayoutManager(requireContext(),2)
        var adapter = ChatRoomAdapter()
        binding.recycler.adapter = adapter
        //test message
        thread {
            val json = URL("https://api.jsonserve.com/hQAtNk").readText()
            val msg = Gson().fromJson(json, Message::class.java)
            Log.d(TAG, "msg: ${msg.body.text}")

        }
        //test chatroom list
        thread {
            val json = URL("https://api.jsonserve.com/qHsaqy").readText()
            val chatRooms = Gson().fromJson(json, Chatrooms::class.java)
            Log.d(TAG, "rooms: ${chatRooms.result.lightyear_list.size}")
            //fill list with new coming data
            rooms.clear()
            rooms.addAll(chatRooms.result.lightyear_list)
            //List<LightYear>
            activity?.runOnUiThread{
                adapter.notifyDataSetChanged()
            }
        }
    }

    inner class ChatRoomAdapter : RecyclerView.Adapter<BindingViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {

            /*val view = layoutInflater.inflate(R.layout.row_chatroom, parent, false)
            return ChatRoomViewHolder(view)*/
            val binding = RowChatroomBinding.inflate(layoutInflater, parent, false)
            return BindingViewHolder(binding)
        }

        override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
            val lightyear = rooms[position]
            holder.host.setText(lightyear.stream_title)
            holder.title.setText(lightyear.nickname)
            holder.tag.setText(lightyear.tags)
            Glide.with(this@SecondFragment).load(lightyear.head_photo)
                .into(holder.headShot)
            //itemView包括整個
            holder.itemView.setOnClickListener {
                chatRoomClicked(lightyear)
            }
        }

        override fun getItemCount(): Int {
            return rooms.size
        }

    }

    inner class  ChatRoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val host = view.findViewById<TextView>(R.id.chatroom_host_name)
        val title = view.findViewById<TextView>(R.id.chatroom_title)
        val tag = view.findViewById<TextView>(R.id.chatroom_tag)
        val headShot = view.findViewById<ImageView>(R.id.head_shot)
    }

    inner class  BindingViewHolder(val binding: RowChatroomBinding): RecyclerView.ViewHolder(binding.root) {
        val host = binding.chatroomHostName
        val title = binding.chatroomTitle
        val headShot = binding.headShot
        val tag = binding.chatroomTag
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //SecondFragment
    fun chatRoomClicked(lightyear: Lightyear){

    }
}
//ChatClasses會用到先註解
data class Messagej(val action:String, val content:String)
package com.example.simple_tcp_server

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.net.ServerSocket
import java.net.Socket

class MainActivity : AppCompatActivity() {
    // our tcp server, it will run and wait for connections
    private lateinit var tcpServer: ServerSocket
    // this is the client that will connect to the server
    private lateinit var client: Socket
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // we need a thread because we can't run network operations
        // on the main UI thread.
        Thread {
            // we put our code in a try catch block to crashes
            // when something unexpected happens
            try {
                /* we start our sever on port 12345
                   can be changed to any available port number */
                tcpServer = ServerSocket(12345)
                /* now we tell the server to accept clients
                   the server will keep waiting untill a client
                   finally connects */
                client = tcpServer.accept()
                /* after the client connected we need to read from the
                * input stream so we create an instance of inputstream */
                val reader = client.getInputStream()
                /* data read from inputstream are in the form of a byte
                * array, so we need to create a byte array */
                val message = ByteArray(255)
                /* as long as we can read from the client, which means
                 it's still connected we are going to update the TextView*/
                while(reader.read(message) != -1){
                    /* to update the UI we need to do that from the UI thread*/
                    runOnUiThread(){
                        in_message.text = String(message)
                    }
                }
            }
            catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this,
                        "Error",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
        }.start()
        send_button.setOnClickListener(){
            val msg = out_message.text.toString()
            /* network operation must be done on a thread*/
            Thread {
                try {
                    /* we write the message to the output stream */
                    client.getOutputStream().write(msg.toByteArray())
                } catch (e: java.lang.Exception) {
                }
            }.start()
        }
    }
}

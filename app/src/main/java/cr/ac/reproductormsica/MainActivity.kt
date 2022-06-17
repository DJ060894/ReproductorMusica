package cr.ac.reproductormsica

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile

class MainActivity : AppCompatActivity() {


    private lateinit var buttonPlay: Button
    private lateinit var buttonVolver: Button
    private lateinit var buttonStop: Button
    private lateinit var buttonAdelantar: Button
    private var contador: Int = 1
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var textView: TextView
    private var documentsFiles: ArrayList<DocumentFile> = arrayListOf()
    companion object{
        var OPEN_DIRECTORY_REQUEST_CODE =  1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonPlay = findViewById(R.id.buttonPlay)
        buttonVolver = findViewById(R.id.buttonAtras)
        buttonAdelantar = findViewById(R.id.buttonNext)
        buttonStop = findViewById(R.id.buttonStop)

        mediaPlayer = MediaPlayer()
        //setOnClickListeners(this)

        var intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
       startActivityForResult(intent, OPEN_DIRECTORY_REQUEST_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== OPEN_DIRECTORY_REQUEST_CODE){
            if(resultCode== Activity.RESULT_OK){
                var directoryUri = data?.data ?: return
                Log.e("Directorio: ", directoryUri.toString())
                val rootTree = DocumentFile.fromTreeUri(this, directoryUri)

                for(file in rootTree!!.listFiles()){
                    try {
                        file.name?.let { Log.e("Archivo", it) }
                        var mediaPlayer = MediaPlayer()
                        mediaPlayer.setDataSource(this, file.uri)
                        mediaPlayer.prepare()
                        mediaPlayer.start()
                    }catch (e: Exception){
                        Log.e("Error", "No pude ejecutar el archivo" + file.uri)
                    }
                }
            }
        }
    }

    private fun setOnClickListeners(context: Context) {
        buttonPlay.setOnClickListener {
            mediaPlayer.stop()
            mediaPlayer = MediaPlayer()
            textView.setText(documentsFiles[contador].name)
            mediaPlayer.setDataSource(this, documentsFiles[contador].uri)
            mediaPlayer.prepare()
            mediaPlayer.start()
            Toast.makeText(context, "Reproduciendo...", Toast.LENGTH_SHORT).show()
        }

        buttonStop.setOnClickListener {
            mediaPlayer.stop()
            mediaPlayer = MediaPlayer()
            Toast.makeText(context, "Parando...", Toast.LENGTH_SHORT).show()
        }

        buttonAdelantar.setOnClickListener{
            textView.setText(documentsFiles[contador++].name)
            mediaPlayer.stop()
            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(this, documentsFiles[contador++].uri)
            mediaPlayer.prepare()
            mediaPlayer.start()
        }

        buttonVolver.setOnClickListener {
            textView.setText(documentsFiles[contador--].name)
            mediaPlayer.stop()
            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(this, documentsFiles[contador--].uri)
            mediaPlayer.prepare()
            mediaPlayer.start()
        }
    }

}
package com.example.wherenow

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import com.example.wherenow.navigation.NavHost
import com.example.wherenow.repository.file.models.FileData
import com.example.wherenow.ui.theme.WhereNowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(scrim = Color.Transparent.toArgb()),
            navigationBarStyle = SystemBarStyle.light(
                scrim = Color.Transparent.toArgb(),
                darkScrim = Color.Transparent.toArgb()
            )
        )
        setContent {
            WhereNowTheme {
                NavHost(
                    onCloseApp = { finish() },
                    openFile = { openFile(it) }
                )
            }
        }
    }

    private fun openFile(id: FileData) {
        try {
            val uri = id.url.toUri()

            applicationContext.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            val pdfOpenIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            startActivity(Intent.createChooser(pdfOpenIntent, "Otwórz plik PDF"))
        } catch (e: Exception) {
            Toast.makeText(this, "Nie można otworzyć pliku PDF", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }
}
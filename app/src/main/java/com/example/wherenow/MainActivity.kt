package com.example.wherenow

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.FileProvider
import androidx.documentfile.provider.DocumentFile
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import com.example.wherenow.navigation.NavHost
import com.example.wherenow.repository.file.models.FileData
import com.example.wherenow.ui.theme.WhereNowTheme
import java.io.File

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
        val file = File(Environment.getExternalStorageDirectory(), id.url.replace("%3A", ":"))
        val path: Uri = FileProvider.getUriForFile(applicationContext, this.applicationContext.packageName + ".provider", file)
        val pdfOpenIntent = Intent(Intent.ACTION_VIEW).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
//            setDataAndType(Uri.fromFile(file), "application/pdf")


            intent.type = "application/pdf"
            DocumentFile.isDocumentUri(applicationContext, Uri.fromFile(file))
//            getFileStreamPath(id.url.replace("%3A", ":"))
//            getDatabasePath(id.url.replace("%3A", ":"))
        }
        startActivity(pdfOpenIntent)
    }
}
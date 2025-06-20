package com.example.wherenow

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import com.example.wherenow.navigation.NavHost
import com.example.wherenow.notification.prefs.NotificationPermissionHelper
import com.example.wherenow.repository.file.models.FileData
import com.example.wherenow.ui.app.settingsmenu.appTheme.AppThemeViewModel
import com.example.wherenow.ui.components.WhereNowPermissionDialog
import com.example.wherenow.ui.theme.WhereNowTheme
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : ComponentActivity() {
    private lateinit var permissionHelper: NotificationPermissionHelper
    private var showDialog by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionHelper = NotificationPermissionHelper(this)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(scrim = Color.Transparent.toArgb()),
            navigationBarStyle = SystemBarStyle.light(
                scrim = Color.Transparent.toArgb(),
                darkScrim = Color.Transparent.toArgb()
            )
        )
        setContent {
            val themeViewModel: AppThemeViewModel = getViewModel()
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

            WhereNowTheme(useDarkTheme = isDarkTheme) {
                NavHost(
                    onCloseApp = { finish() },
                    openFile = { openFile(it) }
                )
            }
            if (showDialog) {
                WhereNowPermissionDialog(
                    onDismiss = { showDialog = false },
                    confirmButton = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", applicationContext.packageName, null)
                        }
                        this.startActivity(intent)
                        showDialog = false
                    }
                )
            }
        }
        requestNotificationPermissionIfNeeded()
    }

    //FILE
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

            startActivity(Intent.createChooser(pdfOpenIntent, "Open PDF file"))
        } catch (e: Exception) {
            Toast.makeText(this, "Cannot open the PDF file", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    //PERMISSION
    // Launcher for requesting permission
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            sendTestNotification()
        } else {
            if (!permissionHelper.hasSeenNotificationDialog()) {
                showDialog = true
                permissionHelper.setSeenNotificationDialog()
            }
        }
    }

    fun sendTestNotification() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            "test_channel",
            "Test Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(this, "test_channel")
            .setContentTitle("Notification test")
            .setContentText("The push is displayed correctly!")
            .setSmallIcon(R.drawable.push_test_icon)
            .build()

        notificationManager.notify(0, notification)
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.d("MainActivity", "Notification permission already granted")
            } else {
                Log.d("MainActivity", "Requesting POST_NOTIFICATIONS permission")
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            Log.d("MainActivity", "No need to request notification permission on this API")
        }
    }
}
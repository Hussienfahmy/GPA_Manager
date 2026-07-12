package com.hussienfahmy.myGpaManager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hussienfahmy.core.util.AppPermission
import com.hussienfahmy.core.util.PermissionController

class MainActivity : ComponentActivity() {

    private lateinit var permissionController: PermissionController

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        permissionController = PermissionController(this)

        setContent {
            GpaManagerApp(
                onRequestNotificationPermission = {
                    permissionController.requestPermission(AppPermission.Notifications)
                },
            )
        }
    }
}

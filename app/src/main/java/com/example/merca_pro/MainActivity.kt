package com.example.merca_pro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.merca_pro.ui.theme.MercaProTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import androidx.compose.ui.Modifier
import com.example.merca_pro.navigation.NavigationWrapper


//import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var navHostController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContent {
            navHostController = rememberNavController()
            MercaProTheme {
                Surface(modifier = Modifier.fillMaxSize(),color = MaterialTheme.colorScheme.background) {

                   NavigationWrapper(navHostController, auth)

                }


                class MainActivity : ComponentActivity() {
                    override fun onCreate(savedInstanceState: Bundle?) {
                        super.onCreate(savedInstanceState)
                        setContent {

                        }
                    }
                }

            }
        }
    }



}

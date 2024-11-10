package com.example.mobileairportapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobileairportapp.pages.FlightScreen
import com.example.mobileairportapp.pages.SearchAirportScreen
import com.example.mobileairportapp.pages.WelcomeScreen
import com.example.mobileairportapp.ui.theme.MobileairportappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileairportappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    //val navController = rememberNavController()
                    //WelcomeScreen().Welcome()
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "welcome_screen", builder = {
                        composable("welcome_screen"){
                            WelcomeScreen().Welcome(navController)
                        }
                        composable("searchAirport_screen"){
                            SearchAirportScreen().SearchAirport(navController)
                        }
                        composable("Flight_Screen/{iataCode}"){
                            backStackEntry -> val iataCode = backStackEntry.arguments?.getString("iataCode")
                            FlightScreen().Flight(navController, iataCode)
                        }
                        composable("Airports"){
                            com.example.mobileairportapp.pages.AirportsScreen().Airport(navController)
                        }
                        composable("AddNewTravel"){
                            com.example.mobileairportapp.pages.CreateNewTravelScreen().CreateNewTravel(navController)
                        }
                        composable("AddNewAirport"){
                            com.example.mobileairportapp.pages.CreateNewAirportScreen().CreateAirport(navController)
                        }
                    })
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MobileairportappTheme {
       // WelcomeScreen().Welcome(navController)
    }
}
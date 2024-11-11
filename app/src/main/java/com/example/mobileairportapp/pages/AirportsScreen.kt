package com.example.mobileairportapp.pages

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobileairportapp.Helpers.Helper
import com.example.mobileairportapp.R
import com.example.mobileairportapp.database.Flight
import com.example.mobileairportapp.database.Repository

class AirportsScreen {

    @Composable
    fun Airport(navController: NavController){

        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        val scrollState = rememberScrollState()
        val screenWidth = configuration.screenWidthDp.dp

        val context = LocalContext.current
        val airportRepo = Repository(context)

        var searchBoxInput by remember {
            mutableStateOf("")
        }

        @Composable
        fun getAirports() {
            val airports = airportRepo.getAirport()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState) // Activer le défilement vertical
                    .padding(horizontal = 10.dp)
            )
            {
                airports.forEach { airport -> // Utilisez forEach pour itérer
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                                // passer l'identifiant ici pour les actions dans la bd
                            .clickable {
                                navController.navigate("Flight_Screen/${airport.iataCode}"){
                                    popUpTo("searchAirport_screen")
                                }
                            }
                    ) {
                        Text(
                            text = airport.iataCode, // Utilisez le nom correct ici
                            modifier = Modifier.padding(bottom = 5.dp),
                            style = TextStyle(
                                color = Color.DarkGray,
                                fontSize = 16.sp
                            )
                        )
                        Text(
                            text = airport.name,
                            style = TextStyle(
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        )
                        Divider(
                            color = Color.Black,
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 16.dp), // Couleur de fond blanche
        ) {
            // Première colonne avec fond cyan
            Column() {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically, // Aligner verticalement au centre
                    modifier = Modifier // Définir la hauteur
                        .fillMaxWidth() // Remplir la largeur
                        .height((screenHeight * 0.07f))
                ) {
                    // Image à gauche
                    Image(
                        painter = painterResource(id = R.drawable.arrow_left_solid),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .height(30.dp)
                            .width(30.dp)
                            .clickable {
                                navController.popBackStack()
                                navController.navigate("searchAirport_screen")
                                       },
                        colorFilter = ColorFilter.tint(Color.Black) // Appliquer la couleur blanche
                    )

                    // Champ de texte à droite
                    Spacer(modifier = Modifier.width(10.dp)) // Ajouter un espace entre l'image et le champ de texte

                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()) {
                         Column() {
                             Text(text = "Airports", style = TextStyle(fontSize = 16.sp, color = Color.DarkGray), modifier = Modifier.padding(start = 8.dp))
                             Text(text = "613 airports",
                                 style = TextStyle(fontSize = 10.sp, color = Color.DarkGray), modifier = Modifier.padding(start = 8.dp))
                         }

                        Icon(painter = painterResource(id = R.drawable.magnifying_glass_solid), contentDescription = null,
                            modifier = Modifier
                                .size(16.dp)
                                .clickable {
                                    navController.navigate("searchAirport_screen") {
                                        popUpTo("Airports") { inclusive = true }
                                    }
                                           }, tint = Color.DarkGray)
                    }
                }

                Divider(
                    color = Color.Black,
                    thickness = 1.dp, // Épaisseur de la bordure
                    modifier = Modifier.padding(horizontal = 8.dp) // Espacement entre le texte et la bordure
                )
            }

            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .clickable {
                    navController.navigate("AddNewAirport"){
                        popUpTo("Airports") { inclusive = true }
                    }
                }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(contentAlignment = Alignment.Center ,
                        modifier = Modifier
                            .border(1.dp, Color.Transparent)
                            .size(40.dp)
                            .clip(RoundedCornerShape(32.dp))
                            .background(Color(0xFF00BFFF))){
                       Icon(painter = painterResource(id = R.drawable.plus_solid), contentDescription = null,
                       modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "Add New Airport",
                        style = TextStyle(fontSize = 16.sp, color = Color.DarkGray), modifier = Modifier.padding(start = 8.dp))
                }
            }
            
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
                .clickable {
                    navController.navigate("AddNewTravel"){
                        popUpTo("Airports") { inclusive = true }
                    }
                }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(contentAlignment = Alignment.Center ,
                        modifier = Modifier
                            .border(1.dp, Color.Transparent)
                            .size(40.dp)
                            .clip(RoundedCornerShape(32.dp))
                            .background(Color(0xFF00BFFF))){
                        Icon(painter = painterResource(id = R.drawable.plus_solid), contentDescription = null,
                            modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "Add New Flight",
                        style = TextStyle(fontSize = 16.sp, color = Color.DarkGray), modifier = Modifier.padding(start = 8.dp))
                }
            }
            // Deuxième colonne
            // Conteneur parent pour superposer les éléments
            Box(modifier = Modifier
                .height((screenHeight * 0.9f)) // Hauteur de la boîte
                .fillMaxWidth()
            ) {

                // Column avec défilement vertical
                ///////////////////***********************************
                getAirports()

            }
        }


    }
}
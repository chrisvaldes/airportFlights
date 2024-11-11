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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobileairportapp.Helpers.Helper
import com.example.mobileairportapp.R
import com.example.mobileairportapp.database.Repository

class FlightScreen {
    @Composable
    fun Flight (navController: NavController, iataCode: String?){
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        val scrollState = rememberScrollState()
        val screenWidth = configuration.screenWidthDp.dp

        val context = LocalContext.current
        val airportRepo = Repository(context)

        @Composable
        fun FlightList() {
            // Vérifiez si le code IATA est non nul
            if (iataCode != null) {
                // Obtenez les vols à partir du dépôt
                val flights = airportRepo.getAirportsFlighDesservetByIataCode(iataCode)

                // Utilisez un MutableStateList pour suivre l'état des vols
                val flightStates = remember { mutableStateListOf(*flights.toTypedArray()) }

                Column(modifier = Modifier.fillMaxSize()) {

                    Column() {
                        Row(
                            verticalAlignment = Alignment.CenterVertically, // Aligner verticalement au centre
                            modifier = Modifier // Définir la hauteur
                                .fillMaxWidth() // Remplir la largeur
                                .height((screenHeight * 0.1f))
                                .padding(horizontal = 10.dp)
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
                                        navController.navigate("Airports"){
                                            popUpTo("searchAirport_screen")
                                        } },
                                colorFilter = ColorFilter.tint(Color.Black) // Appliquer la couleur blanche
                            )

                            // Champ de texte à droite
                            Spacer(modifier = Modifier.width(10.dp)) // Ajouter un espace entre l'image et le champ de texte

                            Column(verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Flight Search",
                                    style = TextStyle(fontSize = 28.sp, color = Color.DarkGray)
                                )
                            }

                        }

                        Text(text = "Flights from $iataCode", style = TextStyle(color = Color.DarkGray, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp))
                        Divider(
                            color = Color.Black,
                            thickness = 2.dp, // Épaisseur de la bordure
                            modifier = Modifier.padding(vertical = 4.dp) // Espacement entre le texte et la bordure
                        )
                    }
                    
                    // Défilement vertical
                    flightStates.forEach { flight ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState()) // Activez le défilement vertical
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .height((screenHeight * 0.15f))
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.LightGray)
                            ) {
                                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                                    // Détails de départ
                                    Column(modifier = Modifier.clickable { println(flight.id) }) {
                                        Text(
                                            text = "Départ",
                                            modifier = Modifier.padding(bottom = 5.dp),
                                            style = TextStyle(
                                                color = Color.DarkGray,
                                                fontSize = 16.sp
                                            )
                                        )
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = flight.departure_iata,
                                                style = TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(
                                                text = flight.departure_name,
                                                style = TextStyle(
                                                    color = Color.Gray,
                                                    fontSize = 12.sp
                                                )
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                    // Détails d'arrivée
                                    Column {
                                        Text(
                                            text = "Arrivée",
                                            modifier = Modifier.padding(bottom = 5.dp),
                                            style = TextStyle(
                                                color = Color.DarkGray,
                                                fontSize = 16.sp
                                            )
                                        )
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = flight.destination_iata,
                                                style = TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(
                                                text = flight.destination_name,
                                                style = TextStyle(
                                                    color = Color.Gray,
                                                    fontSize = 12.sp
                                                )
                                            )
                                        }
                                    }
                                }
                                // Icône favori
                                Column(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .clickable {
                                            // Inverser l'état
                                            val newStatus = !flight.isFavorite.value
                                            flight.isFavorite.value = newStatus

                                            // Appeler la fonction de mise à jour
                                            airportRepo.updateFavoriteStatus(flight.id.toLong(), newStatus)

                                            println("mes vols *--------------------------------${newStatus}")
                                        }
                                ) {
                                    if (flight.isFavorite.value) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.icons_full),
                                            contentDescription = null,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    } else {
                                        Icon(
                                            painter = painterResource(id = R.drawable.icons_empty),
                                            contentDescription = null,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                }
                            }
                        }
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
            //********----------------------
            // Deuxième colonne
            // Conteneur parent pour superposer les éléments
            Box(modifier = Modifier
                .padding(bottom = 32.dp)
                .height((screenHeight * 0.9f)) // Hauteur de la boîte
                .fillMaxWidth()
            ) {
                // Column avec défilement vertical

                FlightList()

            }
        }

    }
}
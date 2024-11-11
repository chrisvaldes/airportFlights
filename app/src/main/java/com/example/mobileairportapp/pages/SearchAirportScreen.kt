package com.example.mobileairportapp.pages

import android.graphics.BlendModeColorFilter
import androidx.annotation.VisibleForTesting
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
import com.example.mobileairportapp.database.Airport
import com.example.mobileairportapp.database.Repository

class SearchAirportScreen {

    @VisibleForTesting
    internal  fun SearchText(value : String): String {
        var airport = value
        return airport;
    }

    @Composable
    fun SearchAirport(navController: NavController) {
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        val scrollState = rememberScrollState()
        val screenWidth = configuration.screenWidthDp.dp

        val context = LocalContext.current
        val airportRepo = Repository(context)

        var isFavorite by remember { mutableStateOf(false) }

        var searchBoxInput by remember {
            mutableStateOf("")
        }
        val airports = remember {
            mutableStateListOf<Airport>()
        }

        val airport = searchBoxInput.toString()
        val searchValue = SearchText(airport)

        val favoriteFlight = airportRepo.getFavoriteFlights()

        LaunchedEffect(searchValue){
            if (searchValue.isNotBlank()){
                // vider la liste avant d'ajouter de nouveaux résultats
                airports.clear()
                airports.addAll(airportRepo.searchAirports(searchValue))
            }else{
                airports.clear()
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
                       .height((screenHeight * 0.1f))
                       .padding(horizontal = 10.dp)
               ) {

                   Row(
                       verticalAlignment = Alignment.CenterVertically,
                       horizontalArrangement = Arrangement.Center,
                       modifier = Modifier
                           .border(
                               BorderStroke(1.dp, Color.Black),
                               shape = RoundedCornerShape(32.dp)
                           )
                           .height(40.dp)
                   ) {
                       Box(contentAlignment = Alignment.Center,
                           modifier = Modifier
                               .padding(
                                   start = 20.dp,
                                   top = 10.dp,
                                   bottom = 10.dp,
                                   end = 10.dp
                               ) // Spécifiez la taille de la Box
                               .height(30.dp)
                       ) {
                           Image(
                               painter = painterResource(id = R.drawable.magnifying_glass_solid),
                               contentDescription = null,
                               contentScale = ContentScale.Crop,
                               modifier = Modifier
                                   .width(20.dp)
                                   .height(20.dp)
                           )

                       }
                       Helper().EditTextField(
                           value = searchBoxInput,
                           onValueChange = { searchBoxInput = it },
                           placeholder = "Search for and airport here...",
                       )
                   }
               }

               Text(
                   text = "$searchValue",
                   modifier = Modifier.padding(start = 16.dp, top = 8.dp), // Espacement au-dessus
                   style = TextStyle(fontSize = 18.sp, color = Color.Black,)
               )
               Divider(
                   color = Color.Black,
                   thickness = 2.dp, // Épaisseur de la bordure
                   modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp) // Espacement entre le texte et la bordure
               )
           }
            // Deuxième colonne
            // Conteneur parent pour superposer les éléments
            Box(modifier = Modifier
                .padding(bottom = 32.dp)
                .height((screenHeight * 0.9f)) // Hauteur de la boîte
                .fillMaxWidth()
            ) {
                // Column avec défilement vertical
                if(airports.isNotEmpty()){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(scrollState) // Activer le défilement vertical
                    ) {  // Remplacer par le nombre d'éléments souhaité
                        airports.forEach{ airport ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    // passer l'identifiant ici pour les actions dans la bd
                                    .clickable {
                                        navController.navigate("Flight_Screen/${airport.iataCode}")
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
                            /*Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .height((screenHeight * 0.15f))
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.LightGray)

                            ) {
                                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                                    Column() {
                                        Text(
                                            text = "Depart",
                                            modifier = Modifier.padding(bottom = 5.dp),
                                            style = TextStyle(
                                                color = Color.DarkGray,
                                                fontSize = 16.sp
                                            )
                                        )
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "MUC",
                                                style = TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(
                                                text = "Munich International airport",
                                                style = TextStyle(
                                                    color = Color.Gray,
                                                    fontSize = 12.sp
                                                )
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Column() {
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
                                                text = "FCO",
                                                style = TextStyle(
                                                    color = Color.Black,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(
                                                text = "Munich International airport",
                                                style = TextStyle(
                                                    color = Color.Gray,
                                                    fontSize = 12.sp
                                                )
                                            )
                                        }
                                    }
                                }
                                Column(modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .clickable { isFavorite = !isFavorite }) {
                                    if (isFavorite) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.icons_full),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(32.dp)
                                        )
                                    } else {
                                        Icon(
                                            painter = painterResource(id = R.drawable.icons_empty),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(32.dp)
                                        )
                                    }
                                }

                            }*/
                        }
                    }
                }
                else if(favoriteFlight.isNotEmpty()){

                    // Défilement vertical
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(scrollState) // Activer le défilement vertical
                    ) {
                        favoriteFlight.forEach { flight ->
                            println("flights : $flight")
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
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
                                                airportRepo.updateFavoriteStatus(
                                                    flight.id.toLong(),
                                                    newStatus
                                                )

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
                }else{
                    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier.fillMaxSize()) {
                        Text(text = "No Favorite airport added yet!!!", style = TextStyle(color = Color.DarkGray))
                    }
                }

                // Box superposée
                Box(
                    modifier = Modifier
                        .fillMaxSize() // Remplit la Box parent
                        .padding(16.dp) // Ajuster le padding si nécessaire
                ) {
                    Button(
                        onClick = {
                            navController.navigate("Airports") {
                                popUpTo("searchAirport_screen") { inclusive = true }
                            }
                                  },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF00BFFF)),
                        modifier = Modifier
                            .border(
                                BorderStroke(0.dp, Color.Transparent), // Bordure transparente
                                shape = RoundedCornerShape(32.dp)
                            )
                            .clip(RoundedCornerShape(64.dp))
                            .size(60.dp) // Taille de la box
                            .align(Alignment.BottomEnd) // Positionne en bas à droite
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.plane),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp), // Taille de l'icône
                            tint = Color.White // Couleur de l'icône
                        )
                    }
                }
            }
        }

    }
}
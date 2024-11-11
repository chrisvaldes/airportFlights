package com.example.mobileairportapp.pages

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobileairportapp.Helpers.Helper
import com.example.mobileairportapp.R
import com.example.mobileairportapp.database.Airport
import com.example.mobileairportapp.database.Repository
import kotlinx.coroutines.delay


@VisibleForTesting
internal  fun getText(value : String): String {
    var stringValue = value
    return stringValue;
}

class CreateNewTravelScreen {
    @Composable
    fun CreateNewTravel(navController : NavController){
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        val screenWidth = configuration.screenWidthDp.dp
        val scrollState = rememberScrollState()
        val context = LocalContext.current
        val airportRepo = Repository(context)


        var showSnackbar by remember { mutableStateOf(false) }
        var snackbarMessage by remember { mutableStateOf("") }

        // État pour gérer l'affichage du menu déroulant et la sélection
        var expandedDeparture by remember { mutableStateOf(false) }
        var expandedDestination by remember { mutableStateOf(false) }
        var selectedOptionDeparture by remember { mutableStateOf("") }
        var selectedOptionDestination by remember { mutableStateOf("") }

        val departure_iata = selectedOptionDeparture.toString()
        val destination_iata = selectedOptionDestination.toString()

        fun saveFlight() {
            // Logique pour obtenir les codes IATA
            val departure_iata_code = airportRepo.getFlightByIata(departure_iata)
            val destination_iata_code = airportRepo.getFlightByIata(destination_iata)

            if (departure_iata_code != null && destination_iata_code != null) {

                airportRepo.addFlight(departure_iata, departure_iata_code.name, destination_iata, destination_iata_code.name)

                // Afficher le snackbar avant de naviguer
                snackbarMessage = "Flight added successfully!!!"
                showSnackbar = true

            } else {
                snackbarMessage = "Please fill all fields!!!"
                showSnackbar = true
            }
        }


        @Composable
        fun getDepartureIata(){
            val iata_code = airportRepo.getAirport()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(BorderStroke(1.dp, Color.Black), RoundedCornerShape(16.dp)) // Bordure
                    .padding(vertical = 10.dp, horizontal = 16.dp)
            ) {
                // Affichage de l'option sélectionnée
                Text(
                    text = selectedOptionDeparture,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandedDeparture = true } // Ouvrir le menu déroulant au clic
                        .padding(8.dp),
                    fontSize = 16.sp,
                    color = Color.Black
                )

                // Menu déroulant
                DropdownMenu(
                    expanded = expandedDeparture,
                    modifier = Modifier.width((screenWidth * 0.8f)),
                    onDismissRequest = { expandedDeparture = false } // Fermer le menu déroulant
                ) {
                    // Liste des options

                    iata_code.forEach { option ->
                        DropdownMenuItem(onClick = {
                            selectedOptionDeparture = option.iataCode // Mettre à jour l'option sélectionnée
                            expandedDeparture = false // Fermer le menu déroulant
                        }) {
                            Text(text = option.iataCode)
                        }
                    }
                }
            }

        }

        @Composable
        fun getDestinationIata(){
            val iata_code = airportRepo.getAirport()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(BorderStroke(1.dp, Color.Black), RoundedCornerShape(16.dp)) // Bordure
                    .padding(vertical = 10.dp, horizontal = 16.dp)
            ) {
                // Affichage de l'option sélectionnée
                Text(
                    text = selectedOptionDestination,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandedDestination = true } // Ouvrir le menu déroulant au clic
                        .padding(8.dp),
                    fontSize = 16.sp,
                    color = Color.Black
                )

                // Menu déroulant
                DropdownMenu(
                    expanded = expandedDestination,
                    modifier = Modifier.width((screenWidth * 0.8f)),
                    onDismissRequest = { expandedDestination = false } // Fermer le menu déroulant
                ) {
                    // Liste des options

                    iata_code.forEach { option ->
                        DropdownMenuItem(onClick = {
                            selectedOptionDestination = option.iataCode // Mettre à jour l'option sélectionnée
                            expandedDestination = false // Fermer le menu déroulant
                        }) {
                            Text(text = option.iataCode)
                        }
                    }
                }
            }

        }


        Column(
            modifier = Modifier
                .background(Color.White)
                .verticalScroll(scrollState), // Couleur de fond blanche
        ) {
            // Première colonne avec fond cyan
            Column() {
                Row(
                    verticalAlignment = Alignment.CenterVertically, // Aligner verticalement au centre
                    modifier = Modifier // Définir la hauteur
                        .fillMaxWidth() // Remplir la largeur
                        .height((screenHeight * 0.1f))
                        .padding(horizontal = 20.dp)
                ) {
                    // Image à gauche
                    Image(
                        painter = painterResource(id = R.drawable.arrow_left_solid),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .height(30.dp)
                            .width(30.dp)
                            .clickable { navController.navigate("Airports") },
                        colorFilter = ColorFilter.tint(Color.Black) // Appliquer la couleur blanche
                    )

                    // Champ de texte à droite
                    Spacer(modifier = Modifier.width(10.dp)) // Ajouter un espace entre l'image et le champ de texte

                    Column(verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Create New Travel",
                            style = TextStyle(fontSize = 20.sp, color = Color.DarkGray)
                        )
                    }

                }

                Spacer(modifier = Modifier.height((screenHeight * 0.10f)))
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                    ,modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxSize()) {

                    Column() {
                        Column() {
                            Text(text = "Departure",
                                style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray), modifier = Modifier.padding(start = 8.dp))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Iata_code",
                                style = TextStyle(fontSize = 16.sp, color = Color.DarkGray), modifier = Modifier.padding(start = 8.dp))
                            Spacer(modifier = Modifier.height(8.dp))


                            // Conteneur principal
                            getDepartureIata()


                        }

                        Spacer(modifier = Modifier.height(40.dp))

                        Column() {
                            Text(text = "Destination",
                                style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray), modifier = Modifier.padding(start = 8.dp))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Iata_code",
                                style = TextStyle(fontSize = 16.sp, color = Color.DarkGray), modifier = Modifier.padding(start = 8.dp))
                            Spacer(modifier = Modifier.height(8.dp))

                            getDestinationIata()

                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Button(
                                modifier = Modifier
                                    .border(
                                        BorderStroke(2.dp, Color(0xFF00BFFF)),
                                        shape = RoundedCornerShape(16.dp)
                                    ) // Bordure
                                    .clip(RoundedCornerShape(16.dp))
                                    .fillMaxWidth()
                                    .height(
                                        (screenHeight * 0.06f),
                                    ),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF00BFFF)),
                                onClick = {
                                    println("IATA Code: $departure_iata")
                                    println("Name Airport: $destination_iata")

                                    saveFlight()
                                }
                            ) {
                                Text(
                                    text = "Add Travel",
                                    fontSize = 18.sp,
                                    color = Color.White,
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height((screenHeight * 0.050f)))
                        if (showSnackbar) {
                            Snackbar(
                                action = {
                                    Button(onClick = { showSnackbar = false }) {
                                        Text("Fermer")
                                    }
                                }
                            ) {
                                Text(snackbarMessage)
                            }
                            // Coroutine pour masquer le Snackbar après un délai
                            // Utiliser LaunchedEffect pour gérer la navigation après le délai
                            LaunchedEffect(Unit) {
                                delay(3000) // Attendre 20 secondes
                                navController.navigate("AddNewTravel") {
                                    popUpTo("searchAirport_screen") { inclusive = true }
                                }
                                showSnackbar = false // Masquer le snackbar après la navigation
                            }
                        }
                    }

                }
            }
        }
    }

}
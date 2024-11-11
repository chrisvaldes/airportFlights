package com.example.mobileairportapp.pages

import android.util.Log
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobileairportapp.Helpers.Helper
import com.example.mobileairportapp.R
import com.example.mobileairportapp.database.Repository
import kotlinx.coroutines.delay

class CreateNewAirportScreen {

    @VisibleForTesting
    internal  fun getText(value : String): String {
        var stringValue = value
        return stringValue;
    }
    internal  fun SearchText(value : String): String {
        var airport = value
        return airport;
    }

    @Composable
    fun CreateAirport(navController: NavController){

        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        val scrollState = rememberScrollState()
        val context = LocalContext.current
        val airportRepo = Repository(context)

        var showSnackbar by remember { mutableStateOf(false) }
        var snackbarMessage by remember { mutableStateOf("") }

        var iata_code by remember {
            mutableStateOf("")
        }
        var name_airport by remember {
            mutableStateOf("")
        }
        var errorMessage by remember {
            mutableStateOf("")
        }
        val iata_data = iata_code.toString()
        val name_data = name_airport.toString()

        fun addAirport(){

            val iata = getText(iata_data)
            val name = getText(name_data)

            if(iata.isNotEmpty() && name.isNotEmpty()){
                /*Log.d("CreateNewAirportScreen", "*************************")
                Log.d("CreateNewAirportScreen", "iata : $iata")
                Log.d("CreateNewAirportScreen", "name : $name")
                Log.d("CreateNewAirportScreen", "*************************")*/

                airportRepo.addAirport(iata, name)

                showSnackbar = true
                snackbarMessage = "Airport added successfully!!!"

            }else{
                snackbarMessage = "Please fill all Fields!!!"
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
                            text = "Create New Airport",
                            style = TextStyle(fontSize = 20.sp, color = Color.DarkGray)
                        )
                    }

                }

                Spacer(modifier = Modifier.height((screenHeight * 0.15f)))
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                    ,modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxSize()) {
                    
                    Column() {
                        Column() {
                            Text(text = "Iata_code",
                                style = TextStyle(fontSize = 16.sp, color = Color.DarkGray), modifier = Modifier.padding(start = 8.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Column(modifier = Modifier
                                .border(
                                    1.dp,
                                    Color.Black,
                                    RoundedCornerShape(16.dp)
                                ) // Puis la bordure
                                .padding(vertical = 10.dp, horizontal = 16.dp)) {
                                Helper().EditTextField(
                                    value = iata_code,
                                    onValueChange = { iata_code = it},
                                    placeholder = "Exemple : MUC",
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Column() {
                            Text(text = "Name", style = TextStyle(fontSize = 16.sp, color = Color.DarkGray), modifier = Modifier.padding(start = 8.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Column(modifier = Modifier
                                .border(
                                    1.dp,
                                    Color.Black,
                                    RoundedCornerShape(16.dp)
                                ) // Puis la bordure
                                .padding(vertical = 10.dp, horizontal = 16.dp)) {
                                Helper().EditTextField(
                                    value = name_airport,
                                    onValueChange = { name_airport = it},
                                    placeholder = "Exemple : Munich International airport",
                                )
                            }
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
                                    addAirport()
                                }
                            ) {
                                Text(
                                    text = "Add Airport",
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
                                Text(text=snackbarMessage,
                                    color = if(snackbarMessage.startsWith("Please fill")) Color.Red else Color.Unspecified
                                    )
                            }
                            // Coroutine pour masquer le Snackbar après un délai
                            // Utiliser LaunchedEffect pour gérer la navigation après le délai
                            LaunchedEffect(Unit) {
                                delay(3000) // Attendre 20 secondes
                                navController.navigate("AddNewAirport") {
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
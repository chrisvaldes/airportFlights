package com.example.mobileairportapp.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.mobileairportapp.R
import kotlinx.coroutines.delay

class WelcomeScreen {
    @Composable
    fun Welcome(navController: NavController) {

        val images = listOf(
            R.drawable.ai_generated,
            R.drawable.airbus, // Remplacez par vos ressources d'images
            R.drawable.airbus_2,
            R.drawable.airport,
            R.drawable.airplane,
            R.drawable.jet,
            R.drawable.galleon,
        )

        // État pour garder la trace de l'index de l'image actuellement affichée
        var currentIndex by remember { mutableStateOf(0) }
        val listState = rememberLazyListState()

        // Effet pour faire défiler les images toutes les 2 secondes
        LaunchedEffect(Unit) {
            while (true) {
                delay(2000) // Attendre 2 secondes
                currentIndex = (currentIndex + 1) % images.size // Passer à l'image suivante
                listState.animateScrollToItem(currentIndex) // Faire défiler vers l'élément
            }
        }

        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp // Largeur en dp
        val screenHeight = configuration.screenHeightDp.dp

        Image(
            painter = painterResource(images[currentIndex]),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )


        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(R.drawable.airbus),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(Color.Transparent) // Utiliser une couleur transparente pour voir l'image derrière
            ) {
                Row(
                    modifier = Modifier
                        .height(screenHeight * 0.8f) // Utilisation de la hauteur relative
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(id = R.string.welcomeTitle),
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif,
                            color = Color.White
                        )
                        Text(
                            text = stringResource(id = R.string.welcomeTitleAirport),
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(60.dp))

                        LazyRow(
                            state = listState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)

                        ) {
                            items(images) { imageRes ->
                                Box(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .width(150.dp)
                                        .height(150.dp) // Taille de l'image
                                        .clip(RoundedCornerShape(20.dp))
                                ) {
                                    Image(
                                        painter = painterResource(imageRes),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        modifier = Modifier
                            .border(
                                BorderStroke(2.dp, Color(0xFF00BFFF)),
                                shape = RoundedCornerShape(32.dp)
                            ) // Bordure
                            .clip(RoundedCornerShape(16.dp))
                            .width(screenWidth * 0.9f) // Largeur relative pour le bouton
                            .height(
                                (screenHeight * 0.07f),
                            ),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF00BFFF)),
                        onClick = {navController.navigate("searchAirport_screen")}
                    ) {
                        Text(
                            text = "Let's get Start",
                            fontSize = 18.sp,
                            color = Color.White,
                        )
                    }
                }
            }
        }

    }
}

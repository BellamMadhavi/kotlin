package com.example.bottomnavigationkotlin
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.AccountBox
//import androidx.compose.material.icons.filled.Home
//import androidx.compose.material.icons.filled.Menu
//import androidx.compose.material3.BottomAppBar
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Surface
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.example.bottomnavigationkotlin.ui.theme.BottomNavigationKotlinTheme
//import com.example.bottomnavigationkotlin.ui.theme.GreenJC
//import androidx.compose.ui.unit.dp
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            BottomNavigationKotlinTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    MyBottomAppBar()
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun MyBottomAppBar(){
//    val NavController = rememberNavController()
//    val context = LocalContext.current.applicationContext
//    val selected = remember {
//        mutableStateOf(Icons.Default.Home)
//    }
//    Scaffold (
//        bottomBar = {
//            BottomAppBar(
//                containerColor = GreenJC
//            ) {
//                IconButton(
//                    onClick = {
//                        selected.value = Icons.Default.Home
//                        NavController.navigate(Screens.Home.screen){
//                            popUpTo(0)
//                        }
//                    },
//                    modifier = Modifier.weight(1f)) {
//                    Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(26.dp)
//                            tint = if(selected.value == Icons.Default.Home) Color.White else Color.DarkGray )
//                }
//
//
//                IconButton(
//                    onClick = {
//                        selected.value = Icons.Default.Menu
//                        NavController.navigate(Screens.Dashboard.screen){
//                            popUpTo(0)
//                        }
//                    },
//                    modifier = Modifier.weight(1f)) {
//                    Icon(Icons.Default.Menu, contentDescription = null, modifier = Modifier.size(26.dp)
//                            tint = if(selected.value == Icons.Default.Menu) Color.White else Color.DarkGray )
//                }
//
//                IconButton(
//                    onClick = {
//                        selected.value = Icons.Default.AccountBox
//                        NavController.navigate(Screens.Account.screen){
//                            popUpTo(0)
//                        }
//                    },
//                    modifier = Modifier.weight(1f)) {
//                    Icon(Icons.Default.AccountBox, contentDescription = null, modifier = Modifier.size(26.dp)
//                            tint = if(selected.value == Icons.Default.AccountBox) Color.White else Color.DarkGray )
//                }
//
//            }
//        }
//    ){paddingValues ->  NavHost(navController = NavController, startDestination = Screens.Home.screen, modifier = Modifier.padding(paddingValues)){
//        composable(Screens.Home.screen){ Home()}
//        composable(Screens.Dashboard.screen){ Dashboard()}
//        composable(Screens.Account.screen){ Account()}
//    }
//
//    }
//}



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp // Import for dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bottomnavigationkotlin.ui.theme.BottomNavigationKotlinTheme
import com.example.bottomnavigationkotlin.ui.theme.GreenJC

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BottomNavigationKotlinTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyBottomAppBar()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class) // Function-level opt-in
@Composable
fun MyBottomAppBar() {
    val navController = rememberNavController()
    val selected = remember { mutableStateOf(Icons.Default.Home) }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = GreenJC
            ) {
                IconButton(
                    onClick = {
                        selected.value = Icons.Default.Home
                        navController.navigate(Screens.Home.screen) {
                            popUpTo(navController.graph.startDestinationId)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = if (selected.value == Icons.Default.Home) Color.White else Color.DarkGray
                    )
                }

                IconButton(
                    onClick = {
                        selected.value = Icons.Default.Menu
                        navController.navigate(Screens.Dashboard.screen) {
                            popUpTo(navController.graph.startDestinationId)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = if (selected.value == Icons.Default.Menu) Color.White else Color.DarkGray
                    )
                }

                IconButton(
                    onClick = {
                        selected.value = Icons.Default.AccountBox
                        navController.navigate(Screens.Account.screen) {
                            popUpTo(navController.graph.startDestinationId)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBox,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = if (selected.value == Icons.Default.AccountBox) Color.White else Color.DarkGray
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screens.Home.screen,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screens.Home.screen) { Home() }
            composable(Screens.Dashboard.screen) { Dashboard() }
            composable(Screens.Account.screen) { Account() }
        }
    }
}

@Preview
@Composable
fun MyBottomBarPreview() {
    BottomNavigationKotlinTheme {
        MyBottomAppBar()
    }
}

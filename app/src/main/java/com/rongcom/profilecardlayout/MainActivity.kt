package com.rongcom.profilecardlayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.rongcom.profilecardlayout.ui.theme.ProfileCardLayoutTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileCardLayoutTheme{
                UsersApplication()
            }
        }
    }
}

@Composable
fun UsersApplication(userProfiles: List<UserProfile> = userProfileList){
   val navController = rememberNavController()
   NavHost(navController = navController, startDestination = "user_list") {
      composable("user_list") {
          UserProfileList(userProfiles, navController)
      }
       composable(route = "user_details/{userId}",
           arguments = listOf(navArgument("userId"){
               type = NavType.IntType
           })
           ){navBackStackEntry ->
           UserProfileDetailsScreen(navBackStackEntry.arguments!!.getInt("userId"), navController)
       }
   }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileList(userProfiles: List<UserProfile>, navController: NavHostController) {
    Scaffold(
        topBar = {
            AppBar(title = "User List", icon = Icons.Default.Home){}
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
//            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            LazyColumn {
                items(userProfiles) {userProfile->
                    ProfileCard(userProfile = userProfile){
                        navController.navigate("user_details/${userProfile.id}")
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileCard(userProfile: UserProfile, clickAction: () -> Unit){
   Card(
       elevation = CardDefaults.cardElevation(
           defaultElevation = 8.dp
       ),
       modifier = Modifier
           .padding(16.dp)
           .fillMaxWidth()
           .wrapContentHeight(align = Alignment.Top)
           .clickable { clickAction.invoke() }
       ) {
       Row (
           modifier = Modifier.fillMaxWidth(),
           verticalAlignment = Alignment.CenterVertically,
           horizontalArrangement = Arrangement.Start
       ){
           ProfilePicture(userProfile.pictureUrl,userProfile.status)
           ProfileContent(userProfile.name, userProfile.status)
       }
   }
}

@Composable
fun ProfilePicture(pictureUrl: String, onlineStatus: Boolean, imageSize: Dp = 70.dp){
    Card(
        shape = CircleShape,
        border = BorderStroke(width = 2.dp, color = if(onlineStatus) Color.Green else Color.Red),
        modifier = Modifier.padding(16.dp),
        elevation =  CardDefaults.cardElevation(
            defaultElevation = 8.dp
            )
    ){
        AsyncImage(
            model = pictureUrl,
            contentDescription = null,
            modifier = Modifier.size(size = imageSize),
            contentScale = ContentScale.Crop
        )
    }
}


@Composable fun ProfileContent(userName: String, onlineStatus: Boolean){
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(text = userName, style = MaterialTheme.typography.headlineSmall)
        Text(text = if(onlineStatus) "Active Now" else "Offline", style = MaterialTheme.typography.bodySmall)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileDetailsScreen(userId: Int, navController: NavHostController) {
    val userProfile = userProfileList.first{userProfile -> userId == userProfile.id
    }
    Scaffold(
        topBar = {
            AppBar(title = "User Profile Details", icon = Icons.Default.ArrowBack){
               navController.popBackStack()
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
        ) {
            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ){
                ProfilePicture(userProfile.pictureUrl,userProfile.status, imageSize = 240.dp)
                ProfileContent(userProfile.name, userProfile.status)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(title: String, icon: ImageVector, iconClickable: () -> Unit){
    TopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(title)
        },
        navigationIcon = {
            Icon(imageVector = icon, contentDescription = "",
                modifier = Modifier.padding(horizontal = 12.dp).clickable { iconClickable.invoke() })
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
//    UserProfileDetailsScreen()
}
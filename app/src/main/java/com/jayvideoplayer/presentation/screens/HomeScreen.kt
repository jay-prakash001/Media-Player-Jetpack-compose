package com.jayvideoplayer.presentation.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.jayvideoplayer.R
import com.jayvideoplayer.presentation.navigation.NavItems
import com.jayvideoplayer.presentation.utils.BottomPlayerController
import com.jayvideoplayer.presentation.utils.Categories
import com.jayvideoplayer.presentation.utils.CategoriesRow
import com.jayvideoplayer.presentation.utils.Category
import com.jayvideoplayer.presentation.utils.Directory
import com.jayvideoplayer.presentation.viewModel.MyViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: MyViewModel = hiltViewModel()) {
    val showMenu = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "jpPlayer", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.app),
                        contentDescription = "app icon", modifier = Modifier
                            .padding(10.dp)
                            .size(50.dp)
                    )
                }, actions = {
                    IconButton(onClick = {
                        showMenu.value = !showMenu.value

                    }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "menu")

                    }
                    DropdownMenu(
                        expanded = showMenu.value,
                        onDismissRequest = {
                            showMenu.value = false
                        }) {


                        DropdownMenuItem(
                            text = { Text(text = "More apps from us") },
                            onClick = {
                                val url = "https://github.com/jay-prakash001"
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)

                            })
                        Divider()


                    }
                })


        }
    ) { it ->

        var showCategories by remember { mutableStateOf(true) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {

            val parentDirectories = viewModel.parentDirectory.collectAsState().value
            val context = LocalContext.current
            val categories = listOf(
                Category("recent", 10, painterResource(id = R.drawable.history)),
                Category("favorite", 10, painterResource(id = R.drawable.circle)),
                Category(
                    "songs",
                    viewModel.audioList.collectAsState().value.size,
                    painterResource(id = R.drawable.music)
                ),
                Category(
                    "video",
                    viewModel.videoList.collectAsState().value.size,
                    painterResource(id = R.drawable.youtube)
                ),
            )
            AnimatedVisibility(visible = showCategories) {
                Categories(context, categories, navController)
            }
            AnimatedVisibility(visible = !showCategories) {
                CategoriesRow(context, categories, navController)

            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Icon(
                        imageVector = if (showCategories) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                        contentDescription = "",
                        modifier = Modifier.clickable {
                            showCategories = !showCategories
                        }, tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                items(parentDirectories) { dir ->
                    Directory(dir, navController = navController) {
                        Toast
                            .makeText(context, dir, Toast.LENGTH_SHORT)
                            .show()
                        navController.navigate(NavItems.DirFileListScreen(dir))
                    }
                }


            }


        }

    }

}


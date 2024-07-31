package com.jayvideoplayer.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jayvideoplayer.presentation.navigation.NavItems
import com.jayvideoplayer.presentation.utils.getUniqueImmediateParentNames
import com.jayvideoplayer.presentation.viewModel.MyViewModel

@Composable
fun VideoFilesScreen(modifier: Modifier = Modifier, viewModel: MyViewModel = hiltViewModel(), navController: NavController) {
   Column {
       Button(onClick = { viewModel.loadAllVideos() }) {
           Text(text = "Load")
       }

       val list0 = viewModel.audioList.collectAsState().value
      val paths = arrayListOf<String>()
       list0.forEach {
           paths.add(it.path)
       }
       val list = getUniqueImmediateParentNames(paths)
       LazyColumn {
           items(list){
//               Text(text = it.toString() + "  ||| " + it.duration, modifier = Modifier.clickable {
//                   navController.navigate(NavItems.PlayerScreen(it.path))
//
//               })
               Text(text = it)
               Spacer(modifier = Modifier.height(10.dp))
           }
       }
   }
}
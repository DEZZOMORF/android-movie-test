package com.dezzomorf.movietest.presentation.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dezzomorf.movietest.R
import com.dezzomorf.movietest.presentation.ui.all.AllMoviesScreen
import com.dezzomorf.movietest.presentation.ui.favorites.FavoritesScreen

@Composable
fun MainScreen() {
    val selectedTab = remember { mutableStateOf(MainTab.FILMS) }

    Scaffold(
        topBar = {
            MovieTestTopBar()
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            TabRowItem(
                selectedTab = selectedTab
            )
            when (selectedTab.value) {
                MainTab.FILMS -> AllMoviesScreen()
                MainTab.FAVOURITE -> FavoritesScreen()
            }
        }
    }
}

@Composable
private fun MovieTestTopBar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.CenterEnd
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://i.redd.it/gd7suscsqp781.jpg") // User image
                .error(R.drawable.ic_no_profile_picture)
                .placeholder(R.drawable.ic_no_profile_picture)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .statusBarsPadding()
                .padding(16.dp)
                .size(60.dp)
                .clip(CircleShape)
        )
    }
}

@Composable
private fun TabRowItem(
    modifier: Modifier = Modifier,
    selectedTab: MutableState<MainTab>
) {
    val selectedTabIndex = MainTab.entries.indexOf(selectedTab.value)
    TabRow(
        modifier = modifier,
        selectedTabIndex = selectedTabIndex,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        indicator = { tabPositions ->
            if (selectedTabIndex < tabPositions.size) {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = MaterialTheme.colorScheme.background
                )
            }
        },
        divider = {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.primary
            )
        },
    ) {
        MainTab.entries.forEach { tab ->
            val isSelected = selectedTab.value == tab
            val fontWeight = if (isSelected) {
                FontWeight.SemiBold
            } else {
                FontWeight.Normal
            }
            Tab(
                text = {
                    Text(
                        text = stringResource(id = tab.title),
                        fontWeight = fontWeight
                    )
                },
                selected = isSelected,
                onClick = { selectedTab.value = tab },
            )
        }
    }
}

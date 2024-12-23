package com.dezzomorf.movietest.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dezzomorf.movietest.R
import com.dezzomorf.movietest.domain.model.Movie
import com.dezzomorf.movietest.presentation.ui.theme.MovieTestTheme
import org.koin.compose.koinInject

@Composable
fun MovieItem(
    modifier: Modifier = Modifier,
    movie: Movie,
    onToggleFavorite: () -> Unit
) {
    val (favouriteButtonIcon, favouriteButtonTint) =
        if (movie.isFavorite) {
            Pair(
                Icons.Default.Favorite,
                MaterialTheme.colorScheme.primary
            )
        } else {
            Pair(
                Icons.Outlined.FavoriteBorder,
                Color.LightGray
            )
        }
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.onSecondary
        ),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(movie.posterPath)
                        .error(R.drawable.ic_no_image)
                        .placeholder(R.drawable.ic_no_image)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth(0.33f)
                        .clip(MaterialTheme.shapes.small)
                )
                Text(
                    text = "${movie.voteAverage}",
                    fontWeight = FontWeight.SemiBold
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = movie.title,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = movie.overview,
                )
            }
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = favouriteButtonIcon,
                    tint = favouriteButtonTint,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun MovieDateItem(
    modifier: Modifier = Modifier,
    date: String
) {
    Text(
        modifier = modifier,
        text = date,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.SemiBold
    )
}

@Preview
@Composable
private fun MovieItemPreview() {
    MovieTestTheme {
        MovieItem(
            movie = Movie.mock,
            onToggleFavorite = {}
        )
    }
}

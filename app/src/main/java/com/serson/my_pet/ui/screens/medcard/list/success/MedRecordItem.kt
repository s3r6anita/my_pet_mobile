package com.serson.my_pet.ui.screens.medcard.list.success

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.serson.my_pet.R
import com.serson.my_pet.data.db.entities.MedRecord
import com.serson.my_pet.navigation.Routes
import com.serson.my_pet.ui.theme.LightBlueBackground
import com.serson.my_pet.ui.theme.LightGrayTint
import com.serson.my_pet.util.PetDateTimeFormatter

@Composable
fun MedRecordItem(
    medRecord: MedRecord,
    navController: NavHostController,
    ) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSecondary,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clickable {
                navController.navigate("${Routes.MedRecord.route}/${medRecord.id}") {
                    launchSingleTop = true
                }
            }
    ) {
        Row(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Image(
                    painter = painterResource(id = R.drawable.medrecord_icon),
                    contentDescription = stringResource(id = R.string.pet_photo_description),
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(LightBlueBackground)
                        .size(50.dp),
                )
                Column(
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .height(50.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = medRecord.title,
                        style = MaterialTheme.typography.titleLarge,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier = Modifier.width(200.dp),
                    )
                    Text(
                        text = medRecord.date.format(PetDateTimeFormatter.dateTime),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(id = R.string.arrow_right_description),
                modifier = Modifier.height(50.dp),
                tint = LightGrayTint
            )
        }
    }
}

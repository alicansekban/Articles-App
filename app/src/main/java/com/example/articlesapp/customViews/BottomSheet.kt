package com.example.articlesapp.customViews

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.articlesapp.utils.CountrySelectModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismissRequest: () -> Unit,
    items: List<CountrySelectModel>,
    countrySelected: (CountrySelectModel) -> Unit
) {

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        sheetState = sheetState,
    ) {
        CountrySelectSheet(
            countrySelected = {
                countrySelected(it)
            },
            items,
            sheetState
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountrySelectSheet(
    countrySelected: (CountrySelectModel) -> Unit = {},
    items: List<CountrySelectModel>,
    bottomSheetState: SheetState
) {
    val coroutineScope = rememberCoroutineScope()


    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(horizontal = 7.dp)
            .padding(bottom = 12.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            elevation = CardDefaults.cardElevation(0.dp),
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            LazyColumn {
                items(
                    items = items
                ) { value ->
                    CountrySelectRowView(item = value, modifier = Modifier, itemClicked = {
                        coroutineScope.launch {
                            bottomSheetState.hide()
                            countrySelected(value)
                        }
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

    }
}

@Composable
fun CountrySelectRowView(item: CountrySelectModel, modifier: Modifier, itemClicked: () -> Unit) {
    val countryName = item.name
    var sortTextColor = MaterialTheme.colorScheme.error

    item.selected.takeIf { it }?.apply {
        sortTextColor = MaterialTheme.colorScheme.error.copy(alpha = 0.3F)
    }

    Column(modifier = modifier) {
        Text(
            text = countryName,
            color = sortTextColor,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 18.dp)
                .clickable {
                    itemClicked()
                },
            textAlign = TextAlign.Center
        )
        Divider(thickness = 1.dp, color = Color.Black.copy(alpha = 0.05F))
    }
}
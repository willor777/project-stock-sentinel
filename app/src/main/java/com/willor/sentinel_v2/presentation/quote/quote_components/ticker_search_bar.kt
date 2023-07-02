package com.willor.sentinel_v2.presentation.common

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.willor.sentinel_v2.presentation.quote.quote_components.QuoteUiState


/*
* TODO
*   Idea when the search icon is clicked, just expand the search results. Then when the user
*   selects a drop down item, the quote screen loads.
* */

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TickerSearchBar(
    quoteUiStateProvider: () -> QuoteUiState,
    onTextChange: (usrTxt: String) -> Unit,
    onSearchResultClicked: (ticker: String) -> Unit
) {

    val quoteUiState = quoteUiStateProvider()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {

        SearchBar(
            txt = quoteUiState.currentSearchText,
            focusManager = focusManager,
            keyboardController = keyboardController,
            onTextChange = onTextChange,
            onSearchClick = {
                // TODO Expand search results
            }
        )

        if (quoteUiState.currentSearchResults.isNotEmpty()) {
            SearchResults(
                resultsList = quoteUiState.currentSearchResults,
                onOptionClicked = {
                    onSearchResultClicked(it)
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            )
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchBar(
    txt: String,
    focusManager: FocusManager,
    keyboardController: SoftwareKeyboardController?,
    onTextChange: (userText: String) -> Unit,
    onSearchClick: (userText: String) -> Unit
) {

    // Row contains SearchBar + SearchIcon
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center
    ) {

        // SearchBar
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth(.9f)
                .height(30.dp)
                .background(Color.LightGray),
            value = txt,
            onValueChange = { userTxt ->
                if (userTxt.isNotBlank() && userTxt[userTxt.lastIndex].isLetter()) {
                    onTextChange(userTxt.uppercase())
                }
                if (userTxt.isBlank()){
                    onTextChange("")
                }
            },
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                fontFamily = MaterialTheme.typography.titleMedium.fontFamily
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Characters,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClick(txt)
                    focusManager.clearFocus()
                }
            )
        )

        // SearchIcon
        Icon(
            Icons.Filled.Search,
            "search",
            modifier = Modifier
                .background(Color.DarkGray)
                .height(30.dp)
                .clickable {
                    onSearchClick(txt)
                    focusManager.clearFocus()
                    keyboardController?.hide()
                },
            tint = Color.White
        )
    }
}


/**
 * This will act as a drop down list that shows the different search results which are updated as the user types
 */
@Composable
private fun SearchResults(
    resultsList: List<List<String>>,
    onOptionClicked: (ticker: String) -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .height(200.dp)
    ) {
        items(resultsList.size) { i ->
            DropDownSearchResult(
                data = resultsList[i],
                onOptionClicked = onOptionClicked
            )
        }
    }

}


@Composable
private fun DropDownSearchResult(
    data: List<String>,
    onOptionClicked: (ticker: String) -> Unit
) {
    // ["A","AGILENT TECHNOLOGIES INC","EQUITY"]

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .clickable { onOptionClicked(data[0]) },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            modifier = Modifier
                .fillMaxHeight()
                .width(100.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Symbol
            Text(
                text = data[0],
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
                fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
                fontWeight = FontWeight.Bold
            )

            // Type
            Text(
                text = data[2],
                fontSize = 9.sp,
                fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
                fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
                modifier = Modifier.padding(start = 3.dp)
            )
        }


        // Company Name
        Text(
            text = data[1],
            fontSize = MaterialTheme.typography.titleSmall.fontSize,
            fontStyle = MaterialTheme.typography.titleSmall.fontStyle,
            fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}



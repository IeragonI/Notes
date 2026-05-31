package com.example.myapplication

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    viewModel: EditViewModel,
    noteId: Int?,
    onBack: () -> Unit
) {
    LaunchedEffect(noteId) {
        viewModel.loadNote(noteId)
    }

    val title by viewModel.title.collectAsState()
    val body by viewModel.body.collectAsState()
    val tags by viewModel.tags.collectAsState()

    var tagInput by remember { mutableStateOf("") }

    BackHandler {
        viewModel.saveAsDraft()
        onBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (noteId == null || noteId == -1) "Новая заметка" else "Редактирование") },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.saveAsDraft()
                        onBack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.saveNote(onBack)
                    }) {
                        Icon(Icons.Default.Done, contentDescription = "Save")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = viewModel::onTitleChange,
                label = { Text("Заголовок") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = body,
                onValueChange = viewModel::onBodyChange,
                label = { Text("Тело заметки") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 5
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            // Tags Input
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = tagInput,
                    onValueChange = { tagInput = it },
                    label = { Text("Теги") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                IconButton(onClick = {
                    if (tagInput.isNotBlank()) {
                        viewModel.addTag(tagInput.trim())
                        tagInput = ""
                    }
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Tag")
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            if (tags.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(tags) { tag ->
                        InputChip(
                            selected = true,
                            onClick = { viewModel.removeTag(tag) },
                            label = { Text(tag) },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { viewModel.saveNote(onBack) },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotBlank() || body.isNotBlank()
            ) {
                Text("Сохранить")
            }
        }
    }
}

package com.acon.acon.core.designsystem.component.topbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AconTopBar(
    modifier: Modifier = Modifier,
    leadingIcon: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {},
    trailingIcon: @Composable () -> Unit = {},
    isStartAlignment: Boolean = false
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        if (isStartAlignment) {
            Row(
                modifier = Modifier.align(Alignment.CenterStart),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    leadingIcon()
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box {
                    content()
                }
            }
        } else {
            Box(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                leadingIcon()
            }
            Box(
                modifier = Modifier.align(Alignment.Center)
            ) {
                content()
            }
        }

        Box(
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            trailingIcon()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AconTopBarPreview() {
    AconTopBar(
        leadingIcon = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        content = {
            Text("리딩과 트레일링이 있는 탑바")
        },
        trailingIcon = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings"
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AconTopBarStartAlignmentPreview() {
    AconTopBar(
        leadingIcon = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        content = {
            Text("왼쪽 정렬 탑바")
        },
        isStartAlignment = true
    )
}

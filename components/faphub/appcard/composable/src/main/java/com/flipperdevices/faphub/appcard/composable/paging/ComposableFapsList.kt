package com.flipperdevices.faphub.appcard.composable.paging

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.flipperdevices.core.ui.errors.ComposableThrowableError
import com.flipperdevices.core.ui.ktx.ComposeLottiePic
import com.flipperdevices.core.ui.theme.LocalPallet
import com.flipperdevices.faphub.appcard.composable.AppCard
import com.flipperdevices.faphub.dao.api.model.FapItemShort
import com.flipperdevices.core.ui.res.R as DesignSystem

private const val DEFAULT_FAP_COUNT = 20

@Suppress("FunctionNaming")
fun LazyListScope.ComposableFapsList(
    faps: LazyPagingItems<FapItemShort>,
    onOpenFapItem: (FapItemShort) -> Unit,
    installationButton: @Composable (FapItemShort?, Modifier, TextUnit) -> Unit
) {
    val elementModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 12.dp)
    if (faps.loadState.refresh is LoadState.Loading) {
        items(DEFAULT_FAP_COUNT) {
            AppCard(
                modifier = elementModifier,
                fapItem = null,
                installationButton = { modifier, fontSize ->
                    installationButton(null, modifier, fontSize)
                }
            )
        }
        return
    } else if (faps.loadState.refresh is LoadState.Error) {
        val loadState = faps.loadState.refresh as? LoadState.Error ?: return
        item {
            ComposableThrowableError(
                modifier = elementModifier,
                throwable = loadState.error,
                onRetry = faps::retry
            )
        }
        return
    }

    ComposableLoadedFapsList(
        faps = faps,
        onOpenFapItem = onOpenFapItem,
        installationButton = installationButton
    )
    faps.loadState.append.let { loadState ->
        when (loadState) {
            is LoadState.Loading -> item {
                ComposableLoadingItem()
            }

            is LoadState.Error -> item {
                ComposableThrowableError(
                    throwable = loadState.error,
                    onRetry = faps::retry,
                    modifier = elementModifier
                )
            }

            else -> {}
        }
    }
}

@Suppress("FunctionNaming")
private fun LazyListScope.ComposableLoadedFapsList(
    faps: LazyPagingItems<FapItemShort>,
    onOpenFapItem: (FapItemShort) -> Unit,
    installationButton: @Composable (FapItemShort?, Modifier, TextUnit) -> Unit
) {
    val lastIndex = faps.itemCount - 1
    items(
        count = faps.itemCount,
        key = faps.itemKey(),
        contentType = faps.itemContentType()
    ) { index ->
        val item = faps[index]
        item?.let {
            AppCard(
                modifier = Modifier
                    .clickable(
                        onClick = { onOpenFapItem(it) }
                    )
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                fapItem = it,
                installationButton = { modifier, fontSize ->
                    installationButton(item, modifier, fontSize)
                }
            )
            if (index != lastIndex) {
                ComposableLoadingItemDivider()
            }
        }
    }
}

@Composable
private fun ComposableLoadingItemDivider() = Divider(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp),
    thickness = 1.dp,
    color = LocalPallet.current.fapHubDividerColor
)

@Composable
private fun ComposableLoadingItem() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        ComposeLottiePic(
            modifier = Modifier.padding(24.dp),
            picResId = DesignSystem.raw.dots_loader,
            rollBackPicResId = DesignSystem.drawable.pic_loader,
            tint = LocalPallet.current.fapHubDividerColor
        )
    }
}

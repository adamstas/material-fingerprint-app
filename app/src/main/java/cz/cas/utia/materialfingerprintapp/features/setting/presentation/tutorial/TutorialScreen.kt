package cz.cas.utia.materialfingerprintapp.features.setting.presentation.tutorial

import android.content.Intent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import android.net.Uri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import cz.cas.utia.materialfingerprintapp.core.AppConfig
import cz.cas.utia.materialfingerprintapp.core.ui.components.NavigationHandler
import cz.cas.utia.materialfingerprintapp.core.ui.components.TopBarTitle

@Composable
fun TutorialScreenRoot(
    viewModel: TutorialViewModel = hiltViewModel(),
    onTutorialCompleted: () -> Unit // from Settings will pop back stack so user is back at Settings; from LaunchScreen it will call its onEvent logic
) {

    NavigationHandler(
        navigationEventFlow = viewModel.navigationEvents,
        navigate = { event ->
            when (event) {
                TutorialNavigationEvent.ToTutorialCompletedDestination -> onTutorialCompleted()
            }
        }
    )

    TutorialScreen(
        onEvent = viewModel::onEvent
    )
}

@Composable
fun TutorialScreen(
    onEvent: (TutorialEvent) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { tutorialPages.size })

    Scaffold(
        topBar = {
            TopBarTitle(
                title = "Tutorial"
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {

                // inspiration from https://developer.android.com/develop/ui/compose/layouts/pager
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) { page ->
                    TutorialPageContent(
                        page = tutorialPages[page],
                        isLastPage = pagerState.currentPage == tutorialPages.lastIndex,
                        onTutorialCompleted = { onEvent(TutorialEvent.CompleteTutorial) }
                        )
                }

                Row(
                    Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(bottom = 28.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(pagerState.pageCount) { iteration ->
                        val color =
                            if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        val size by animateDpAsState(
                            targetValue = if (pagerState.currentPage == iteration) 12.dp else 8.dp,
                            label = "indicator size"
                        )
                        Box(
                            modifier = Modifier
                                .padding(2.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(size)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun TutorialPageContent(
    page: TutorialPage,
    isLastPage: Boolean,
    onTutorialCompleted: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = page.imageResId),
            contentDescription = "Tutorial image",
            modifier = Modifier.sizeIn(maxHeight = 400.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        if (page.hasLinkToTemplate) {
            Spacer(modifier = Modifier.height(16.dp))

            TemplateLink()
        }

        if (isLastPage) {
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onTutorialCompleted
            ) {
                Text("Finish tutorial")
            }
        }
    }
}

@Composable
fun TemplateLink() {
    val context = LocalContext.current
    val uri = Uri.parse(AppConfig.Tutorial.PHOTO_CAPTURING_TEMPLATE_LINK)

    Text(
        text = "DOWNLOAD THE TEMPLATE HERE",
        style = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, uri)
                context.startActivity(intent)
            }
            .padding(vertical = 4.dp)
    )
}
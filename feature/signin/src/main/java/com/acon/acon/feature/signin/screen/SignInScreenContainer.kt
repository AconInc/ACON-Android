package com.acon.acon.feature.signin.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.acon.acon.core.common.UrlConstants
import com.acon.acon.core.designsystem.R
import com.acon.acon.core.utils.feature.toast.showToast
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SignInScreenContainer(
    navigateToSpotListView: () -> Unit,
    navigateToAreaVerification: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.collectAsState()

    SignInScreen(
        state = state,
        modifier = modifier.fillMaxSize(),
        navigateToSpotListView = viewModel::navigateToSpotListView,
        onClickTermsOfUse = viewModel::onClickTermsOfUse,
        onClickPrivacyPolicy = viewModel::onClickPrivacyPolicy,
        onAnimationEnd = viewModel::signIn,
    )

    viewModel.emitUserType()
    viewModel.collectSideEffect { sideEffect ->
        when(sideEffect) {
            is SignInSideEffect.ShowToastMessage -> { context.showToast(R.string.signin_login_failed_toast) }
            is SignInSideEffect.NavigateToSpotListView -> { navigateToSpotListView() }
            is SignInSideEffect.NavigateToAreaVerification -> { navigateToAreaVerification() }
            is SignInSideEffect.OnClickTermsOfUse -> {
                val url = UrlConstants.TERM_OF_USE
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
            is SignInSideEffect.OnClickPrivacyPolicy -> {
                val url = UrlConstants.PRIVATE_POLICY
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        }
    }
}
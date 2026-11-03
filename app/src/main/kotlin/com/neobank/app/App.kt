package com.neobank.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.neobank.app.auth.presentation.WelcomeScreen
import com.neobank.app.cards.presentation.CardsScreen
import com.neobank.app.core.navigation.KmpBackHandler
import com.neobank.app.core.navigation.NavTab
import com.neobank.app.home.presentation.HomeScreen
import com.neobank.app.profile.presentation.ProfileScreen
import com.neobank.app.transactions.presentation.TransactionHistoryScreen
import com.neobank.app.transfer.presentation.SendMoneyScreen

enum class Screen {
    Welcome,
    Home,
    History,
    Cards,
    SendMoney,
    Profile
}

private fun NavTab.toScreen(): Screen = when (this) {
    NavTab.Home    -> Screen.Home
    NavTab.History -> Screen.History
    NavTab.Cards   -> Screen.Cards
    NavTab.Profile -> Screen.Profile
    NavTab.Transfer-> Screen.Home
}

@Composable
fun App() {
    MaterialTheme {
        var screenStack by remember { mutableStateOf(listOf(Screen.Welcome)) }
        var totalBalance by remember { mutableStateOf(50000.00) }
        val currentScreen = screenStack.last()

        val navigateTo: (Screen) -> Unit = { screen ->
            when (screen) {
                Screen.Home -> {
                    screenStack = listOf(Screen.Home)
                }
                Screen.Welcome -> {
                    screenStack = listOf(Screen.Welcome)
                }
                else -> {
                    if (currentScreen != screen) {
                        screenStack = listOf(Screen.Home, screen)
                    }
                }
            }
        }

        val goBack: () -> Unit = {
            if (screenStack.size > 1) {
                screenStack = screenStack.dropLast(1)
            }
        }

        KmpBackHandler(enabled = screenStack.size > 1) {
            goBack()
        }

        val onTabSelected: (NavTab) -> Unit = { tab ->
            navigateTo(tab.toScreen())
        }

        when (currentScreen) {
            Screen.Welcome -> {
                WelcomeScreen(onNavigateToHome = { navigateTo(Screen.Home) })
            }

            Screen.Home -> {
                HomeScreen(
                    balance = totalBalance,
                    onNavigateToCards     = { navigateTo(Screen.Cards) },
                    onNavigateToSendMoney = { navigateTo(Screen.SendMoney) },
                    onNavigateToHistory   = { navigateTo(Screen.History) },
                    onNavigateToProfile   = { navigateTo(Screen.Profile) },
                    selectedTab           = NavTab.Home,
                    onTabSelected         = onTabSelected
                )
            }

            Screen.History -> {
                TransactionHistoryScreen(
                    onNavigateToHome    = { navigateTo(Screen.Home) },
                    onNavigateToCards   = { navigateTo(Screen.Cards) },
                    onNavigateToProfile = { navigateTo(Screen.Profile) },
                    selectedTab         = NavTab.History,
                    onTabSelected       = onTabSelected
                )
            }

            Screen.Cards -> {
                CardsScreen(
                    onNavigateToHome    = { navigateTo(Screen.Home) },
                    onNavigateToHistory = { navigateTo(Screen.History) },
                    onNavigateToProfile = { navigateTo(Screen.Profile) },
                    selectedTab         = NavTab.Cards,
                    onTabSelected       = onTabSelected
                )
            }

            Screen.SendMoney -> {
                SendMoneyScreen(
                    currentBalance = totalBalance,
                    onSendMoney = { amountToSend ->
                        totalBalance -= amountToSend
                        navigateTo(Screen.Home)
                    },
                    onBackClick = { goBack() }
                )
            }

            Screen.Profile -> {
                ProfileScreen(
                    onNavigateToHome    = { navigateTo(Screen.Home) },
                    onNavigateToCards   = { navigateTo(Screen.Cards) },
                    onNavigateToHistory = { navigateTo(Screen.History) },
                    selectedTab         = NavTab.Profile,
                    onTabSelected       = onTabSelected
                )
            }
        }
    }
}
package com.example.impahla.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.impahla.data.local.AppDatabase
import com.example.impahla.data.repository.ContributionRepository
import com.example.impahla.ui.add_edit.AddEditContributionScreen
import com.example.impahla.ui.add_edit.AddEditViewModel
import com.example.impahla.ui.add_edit.AddEditViewModelFactory
import com.example.impahla.ui.dashboard.DashboardScreen
import com.example.impahla.ui.dashboard.DashboardViewModel
import com.example.impahla.ui.dashboard.DashboardViewModelFactory
import com.example.impahla.ui.detail.ContributionDetailScreen
import com.example.impahla.ui.detail.ContributionDetailViewModel
import com.example.impahla.ui.detail.ContributionDetailViewModelFactory
import com.example.impahla.ui.list.ContributionListScreen
import com.example.impahla.ui.list.ContributionListViewModel
import com.example.impahla.ui.list.ContributionListViewModelFactory

object ScreenRoutes {
    const val DASHBOARD = "dashboard"
    const val CONTRIBUTION_LIST = "contribution_list"
    const val ADD_EDIT_CONTRIBUTION = "add_edit_contribution?id={id}"
    const val CONTRIBUTION_DETAIL = "contribution_detail/{id}"

    fun addEditRoute(id: Long? = null): String {
        return if (id != null && id > 0) {
            "add_edit_contribution?id=$id"
        } else {
            "add_edit_contribution"
        }
    }

    fun detailRoute(id: Long): String {
        return "contribution_detail/$id"
    }
}

@Composable
fun NavGraph(navController: NavHostController) {
    val context = LocalContext.current.applicationContext
    val database = AppDatabase.getDatabase(context)
    val repository = ContributionRepository(database.contributionDao())

    NavHost(
        navController = navController,
        startDestination = ScreenRoutes.DASHBOARD
    ) {
        composable(ScreenRoutes.DASHBOARD) {
            val dashboardViewModel: DashboardViewModel = viewModel(
                factory = DashboardViewModelFactory(repository)
            )
            DashboardScreen(
                viewModel = dashboardViewModel,
                onNavigateToAdd = {
                    navController.navigate(ScreenRoutes.addEditRoute())
                },
                onNavigateToList = {
                    navController.navigate(ScreenRoutes.CONTRIBUTION_LIST)
                },
                onNavigateToDetail = { id ->
                    navController.navigate(ScreenRoutes.detailRoute(id))
                }
            )
        }

        composable(ScreenRoutes.CONTRIBUTION_LIST) {
            val listViewModel: ContributionListViewModel = viewModel(
                factory = ContributionListViewModelFactory(repository)
            )
            ContributionListScreen(
                viewModel = listViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAdd = {
                    navController.navigate(ScreenRoutes.addEditRoute())
                },
                onNavigateToDetail = { id ->
                    navController.navigate(ScreenRoutes.detailRoute(id))
                }
            )
        }

        composable(
            route = ScreenRoutes.ADD_EDIT_CONTRIBUTION,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            val addEditViewModel: AddEditViewModel = viewModel(
                factory = AddEditViewModelFactory(repository)
            )
            AddEditContributionScreen(
                viewModel = addEditViewModel,
                contributionId = id,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = ScreenRoutes.CONTRIBUTION_DETAIL,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            val detailViewModel: ContributionDetailViewModel = viewModel(
                factory = ContributionDetailViewModelFactory(repository)
            )
            ContributionDetailScreen(
                viewModel = detailViewModel,
                contributionId = id,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { editId ->
                    navController.navigate(ScreenRoutes.addEditRoute(editId))
                }
            )
        }
    }
}

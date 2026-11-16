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
import com.example.impahla.data.repository.EventRepository
import com.example.impahla.data.repository.ProgrammeRepository
import com.example.impahla.data.repository.UserRepository
import com.example.impahla.model.UserRole
import com.example.impahla.ui.add_edit.AddEditContributionScreen
import com.example.impahla.ui.add_edit.AddEditViewModel
import com.example.impahla.ui.add_edit.AddEditViewModelFactory
import com.example.impahla.ui.auth.AuthViewModel
import com.example.impahla.ui.auth.AuthViewModelFactory
import com.example.impahla.ui.auth.LoginScreen
import com.example.impahla.ui.auth.RegisterScreen
import com.example.impahla.ui.dashboard.ContributorDashboardScreen
import com.example.impahla.ui.dashboard.ContributorDashboardViewModel
import com.example.impahla.ui.dashboard.ContributorDashboardViewModelFactory
import com.example.impahla.ui.dashboard.ManagerDashboardScreen
import com.example.impahla.ui.dashboard.ManagerDashboardViewModel
import com.example.impahla.ui.dashboard.ManagerDashboardViewModelFactory
import com.example.impahla.ui.detail.ContributionDetailScreen
import com.example.impahla.ui.detail.ContributionDetailViewModel
import com.example.impahla.ui.detail.ContributionDetailViewModelFactory
import com.example.impahla.ui.detail.EventDetailsScreen
import com.example.impahla.ui.detail.EventDetailsViewModel
import com.example.impahla.ui.detail.EventDetailsViewModelFactory
import com.example.impahla.ui.event.CreateEventScreen
import com.example.impahla.ui.event.CreateEventViewModel
import com.example.impahla.ui.event.CreateEventViewModelFactory
import com.example.impahla.ui.event.MyEventsScreen
import com.example.impahla.ui.event.MyEventsViewModel
import com.example.impahla.ui.event.MyEventsViewModelFactory
import com.example.impahla.ui.explore.ExploreEventsScreen
import com.example.impahla.ui.explore.ExploreEventsViewModel
import com.example.impahla.ui.explore.ExploreEventsViewModelFactory
import com.example.impahla.ui.list.ContributionListScreen
import com.example.impahla.ui.list.ContributionListViewModel
import com.example.impahla.ui.list.ContributionListViewModelFactory

object ScreenRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val MANAGER_DASHBOARD = "manager_dashboard"
    const val CONTRIBUTOR_DASHBOARD = "contributor_dashboard"
    const val CREATE_EVENT = "create_event"
    const val MY_EVENTS = "my_events"
    const val EXPLORE_EVENTS = "explore_events"
    const val EVENT_DETAILS = "event_details/{eventId}"
    const val CONTRIBUTION_LIST = "contribution_list"
    const val ADD_EDIT_CONTRIBUTION = "add_edit_contribution?id={id}&eventId={eventId}"
    const val CONTRIBUTION_DETAIL = "contribution_detail/{id}"

    fun addEditRoute(id: Long? = null, eventId: Long? = null): String {
        val idPart = if (id != null && id > 0) "id=$id" else ""
        val eventPart = if (eventId != null && eventId > 0) "eventId=$eventId" else ""
        val params = listOf(idPart, eventPart).filter { it.isNotEmpty() }.joinToString("&")
        return if (params.isNotEmpty()) "add_edit_contribution?$params" else "add_edit_contribution"
    }

    fun eventDetailsRoute(eventId: Long): String {
        return "event_details/$eventId"
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
    val userRepository = UserRepository(database.userDao())
    val eventRepository = EventRepository(database.eventDao())
    val programmeRepository = ProgrammeRepository(database.programmeDao())

    NavHost(
        navController = navController,
        startDestination = ScreenRoutes.LOGIN
    ) {
        composable(ScreenRoutes.LOGIN) {
            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory(userRepository)
            )
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = { role ->
                    if (role == UserRole.EVENT_MANAGER) {
                        navController.navigate(ScreenRoutes.MANAGER_DASHBOARD) {
                            popUpTo(ScreenRoutes.LOGIN) { inclusive = true }
                        }
                    } else {
                        navController.navigate(ScreenRoutes.CONTRIBUTOR_DASHBOARD) {
                            popUpTo(ScreenRoutes.LOGIN) { inclusive = true }
                        }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(ScreenRoutes.REGISTER)
                }
            )
        }

        composable(ScreenRoutes.REGISTER) {
            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory(userRepository)
            )
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = { role ->
                    if (role == UserRole.EVENT_MANAGER) {
                        navController.navigate(ScreenRoutes.MANAGER_DASHBOARD) {
                            popUpTo(ScreenRoutes.LOGIN) { inclusive = true }
                        }
                    } else {
                        navController.navigate(ScreenRoutes.CONTRIBUTOR_DASHBOARD) {
                            popUpTo(ScreenRoutes.LOGIN) { inclusive = true }
                        }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(ScreenRoutes.MANAGER_DASHBOARD) {
            val managerViewModel: ManagerDashboardViewModel = viewModel(
                factory = ManagerDashboardViewModelFactory(eventRepository, repository)
            )
            ManagerDashboardScreen(
                viewModel = managerViewModel,
                onNavigateToCreateEvent = { navController.navigate(ScreenRoutes.CREATE_EVENT) },
                onNavigateToMyEvents = { navController.navigate(ScreenRoutes.MY_EVENTS) },
                onNavigateToContributions = { navController.navigate(ScreenRoutes.CONTRIBUTION_LIST) },
                onLogout = {
                    navController.navigate(ScreenRoutes.LOGIN) {
                        popUpTo(ScreenRoutes.MANAGER_DASHBOARD) { inclusive = true }
                    }
                }
            )
        }

        composable(ScreenRoutes.CONTRIBUTOR_DASHBOARD) {
            val contributorViewModel: ContributorDashboardViewModel = viewModel(
                factory = ContributorDashboardViewModelFactory(eventRepository, repository)
            )
            ContributorDashboardScreen(
                viewModel = contributorViewModel,
                onNavigateToExplore = { navController.navigate(ScreenRoutes.EXPLORE_EVENTS) },
                onLogout = {
                    navController.navigate(ScreenRoutes.CONTRIBUTOR_DASHBOARD) {
                        popUpTo(ScreenRoutes.CONTRIBUTOR_DASHBOARD) { inclusive = true }
                    }
                }
            )
        }

        composable(ScreenRoutes.CREATE_EVENT) {
            val createEventViewModel: CreateEventViewModel = viewModel(
                factory = CreateEventViewModelFactory(eventRepository)
            )
            CreateEventScreen(
                viewModel = createEventViewModel,
                onNavigateBack = { navController.popBackStack() },
                onEventCreated = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.MY_EVENTS) {
            val myEventsViewModel: MyEventsViewModel = viewModel(
                factory = MyEventsViewModelFactory(eventRepository)
            )
            MyEventsScreen(
                viewModel = myEventsViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.EXPLORE_EVENTS) {
            val exploreViewModel: ExploreEventsViewModel = viewModel(
                factory = ExploreEventsViewModelFactory(eventRepository)
            )
            ExploreEventsScreen(
                viewModel = exploreViewModel,
                onNavigateBack = { navController.popBackStack() },
                onEventClick = { eventId ->
                    navController.navigate(ScreenRoutes.eventDetailsRoute(eventId))
                }
            )
        }

        composable(
            route = ScreenRoutes.EVENT_DETAILS,
            arguments = listOf(
                navArgument("eventId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getLong("eventId") ?: 0L
            val detailsViewModel: EventDetailsViewModel = viewModel(
                factory = EventDetailsViewModelFactory(eventId, eventRepository, programmeRepository)
            )
            EventDetailsScreen(
                viewModel = detailsViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToContribute = { evId ->
                    navController.navigate(ScreenRoutes.addEditRoute(eventId = evId))
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
                },
                navArgument("eventId") {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            val eventId = backStackEntry.arguments?.getLong("eventId") ?: 0L
            val addEditViewModel: AddEditViewModel = viewModel(
                factory = AddEditViewModelFactory(repository)
            )
            if (eventId > 0) {
                addEditViewModel.setEventId(eventId)
            }
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

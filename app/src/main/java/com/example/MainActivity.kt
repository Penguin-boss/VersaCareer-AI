package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.*
import com.example.ui.theme.VersaCareerTheme
import com.example.ui.theme.*
import com.example.viewmodel.CareerViewModel
import com.example.ui.components.VersaCareerLogo
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      VersaCareerTheme {
        MainAppScreen()
      }
    }
  }
}

sealed class Screen(val route: String, val label: String, val activeIcon: ImageVector, val inactiveIcon: ImageVector) {
  object Home : Screen("home", "Home", Icons.Filled.Home, Icons.Outlined.Home)
  object Dashboard : Screen("dashboard", "Dashboard", Icons.Filled.GridView, Icons.Outlined.GridView)
  object Upload : Screen("upload", "Upload", Icons.Filled.CloudUpload, Icons.Outlined.CloudUpload)
  object Roadmap : Screen("roadmap", "Roadmap", Icons.Filled.Map, Icons.Outlined.Map)
  object Profile : Screen("profile", "Profile", Icons.Filled.Person, Icons.Outlined.Person)

  // Sub Screens
  object Analysis : Screen("analysis", "Resume Analysis", Icons.Filled.Description, Icons.Outlined.Description)
  object SkillGap : Screen("skill_gap", "Skill Gap Analysis", Icons.Filled.QueryStats, Icons.Outlined.QueryStats)
  object JobReadiness : Screen("job_readiness", "Job Readiness", Icons.Filled.Bolt, Icons.Outlined.Bolt)
  object FutureEmployability : Screen("future_employability", "Future Employability", Icons.Filled.TrackChanges, Icons.Outlined.TrackChanges)
  object ProjectRecommendation : Screen("project_recommendation", "Project Suggestions", Icons.Filled.Build, Icons.Outlined.Build)
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainAppScreen() {
  val navController = rememberNavController()
  val viewModel: CareerViewModel = viewModel()
  val currentBackStack by navController.currentBackStackEntryAsState()
  val currentRoute = currentBackStack?.destination?.route ?: Screen.Home.route

  val mainTabs = listOf(
    Screen.Home.route,
    Screen.Dashboard.route,
    Screen.Upload.route,
    Screen.Roadmap.route,
    Screen.Profile.route
  )

  val isMainTab = currentRoute in mainTabs

  var selectedTab by remember { mutableStateOf(Screen.Home.route) }

  if (currentRoute in mainTabs) {
    selectedTab = currentRoute
  }

  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val scope = rememberCoroutineScope()

  ModalNavigationDrawer(
    drawerState = drawerState,
    drawerContent = {
      VersaCareerDrawerContent(
        currentRoute = selectedTab,
        onNavigate = { route ->
          scope.launch {
            drawerState.close()
          }
          navController.navigate(route) {
            popUpTo(navController.graph.startDestinationId) {
              inclusive = false
              saveState = false
            }
            launchSingleTop = true
            restoreState = false
          }
        },
        onClose = {
          scope.launch {
            drawerState.close()
          }
        }
      )
    }
  ) {
    Scaffold(
      modifier = Modifier.fillMaxSize(),
      containerColor = BrandBackground,
      topBar = {
        if (isMainTab && currentRoute != Screen.Roadmap.route) {
          TopNavBarHeader(
            currentRoute = currentRoute,
            onBackClick = { navController.popBackStack() },
            onMenuClick = {
              scope.launch {
                if (drawerState.isClosed) drawerState.open() else drawerState.close()
              }
            }
          )
        }
      },
      bottomBar = {
        if (isMainTab) {
          CustomBottomNavDock(
            currentRoute = selectedTab,
            onNavigate = { route ->
              navController.navigate(route) {
                popUpTo(navController.graph.startDestinationId) {
                  inclusive = false
                  saveState = false
                }
                launchSingleTop = true
                restoreState = false
              }
            }
          )
        }
      }
    ) { paddingValues ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(if (isMainTab) paddingValues else PaddingValues(0.dp))
          .background(BrandBackground)
      ) {
        // NavHost handles seamless page swaps
        NavHost(
          navController = navController,
          startDestination = Screen.Home.route,
          modifier = Modifier.fillMaxSize()
        ) {
          composable(Screen.Home.route) {
            HomeScreen(
              viewModel = viewModel,
              onNavigateToUpload = { navController.navigate(Screen.Upload.route) },
              onNavigateToDashboard = { navController.navigate(Screen.Dashboard.route) },
              onNavigateToAnalysis = { navController.navigate(Screen.Analysis.route) },
              onNavigateToSkillGap = { navController.navigate(Screen.SkillGap.route) },
              onNavigateToRoadmap = { navController.navigate(Screen.Roadmap.route) },
              onNavigateToJobReadiness = { navController.navigate(Screen.JobReadiness.route) },
              onNavigateToFutureEmployability = { navController.navigate(Screen.FutureEmployability.route) },
              onNavigateToProjectRecommendation = { navController.navigate(Screen.ProjectRecommendation.route) }
            )
          }

          composable(Screen.Dashboard.route) {
            DashboardScreen(
              viewModel = viewModel,
              onNavigateToAnalysis = { navController.navigate(Screen.Analysis.route) },
              onNavigateToSkillGap = { navController.navigate(Screen.SkillGap.route) },
              onNavigateToRoadmap = { navController.navigate(Screen.Roadmap.route) },
              onNavigateToJobReadiness = { navController.navigate(Screen.JobReadiness.route) },
              onNavigateToFutureEmployability = { navController.navigate(Screen.FutureEmployability.route) },
              onNavigateToProjectRecommendation = { navController.navigate(Screen.ProjectRecommendation.route) }
            )
          }

          composable(Screen.Upload.route) {
            UploadScreen(
              viewModel = viewModel,
              onNavigateToAnalysis = { navController.navigate(Screen.Analysis.route) }
            )
          }

          composable(Screen.Roadmap.route) {
            RoadmapScreen(
              viewModel = viewModel,
              onBackClick = { navController.navigateUp() }
            )
          }

          composable(Screen.Profile.route) {
            ProfileScreen(
              viewModel = viewModel,
              onNavigateToUpload = { navController.navigate(Screen.Upload.route) },
              onNavigateToDashboard = { navController.navigate(Screen.Dashboard.route) }
            )
          }

          composable(Screen.Analysis.route) {
            AnalysisScreen(
              viewModel = viewModel,
              onNavigateToRoadmap = { navController.navigate(Screen.Roadmap.route) },
              onBackClick = { navController.navigateUp() }
            )
          }

          composable(Screen.SkillGap.route) {
            SkillGapScreen(
              viewModel = viewModel,
              onBackClick = { navController.navigateUp() }
            )
          }

          composable(Screen.JobReadiness.route) {
            JobReadinessScreen(
              viewModel = viewModel,
              onBackClick = { navController.navigateUp() }
            )
          }

          composable(Screen.FutureEmployability.route) {
            FutureEmployabilityScreen(
              viewModel = viewModel,
              onBackClick = { navController.navigateUp() }
            )
          }

          composable(Screen.ProjectRecommendation.route) {
            ProjectRecommendationScreen(
              viewModel = viewModel,
              onNavigateToRoadmap = { navController.navigate(Screen.Roadmap.route) },
              onBackClick = { navController.navigateUp() }
            )
          }
        }
      }
    }
  }
}

@Composable
fun TopNavBarHeader(
  currentRoute: String,
  onBackClick: () -> Unit,
  onMenuClick: () -> Unit
) {
  val isMainTab = currentRoute in listOf(
    Screen.Home.route,
    Screen.Dashboard.route,
    Screen.Upload.route,
    Screen.Roadmap.route,
    Screen.Profile.route
  )

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .statusBarsPadding()
      .height(64.dp)
      .background(BrandBackground.copy(alpha = 0.9f))
      .padding(horizontal = 20.dp, vertical = 8.dp),
    contentAlignment = Alignment.Center
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // App logo and brand letters at top-left, or back button on sub-screens
      Row(verticalAlignment = Alignment.CenterVertically) {
        if (!isMainTab) {
          IconButton(
            onClick = onBackClick,
            modifier = Modifier
              .size(40.dp)
              .testTag("back_button")
          ) {
            Icon(
              imageVector = Icons.Default.ArrowBack,
              contentDescription = "Back",
              tint = Color.White
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
        } else {
          VersaCareerLogo(
            modifier = Modifier.size(32.dp),
            showBackground = false
          )
          Spacer(modifier = Modifier.width(10.dp))
        }

        Text(
          text = "VersaCareerAI",
          style = MaterialTheme.typography.titleMedium,
          color = Color.White,
          fontWeight = FontWeight.Black,
          letterSpacing = (-0.5).sp
        )
      }

      // Notification Bell + Hambu Menu
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Box(contentAlignment = Alignment.TopEnd) {
          Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "Alerts",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
          )
          // Notification Dot
          Box(
            modifier = Modifier
              .size(8.dp)
              .background(BrandSecondary, CircleShape)
              .border(1.dp, BrandBackground, CircleShape)
          )
        }

        IconButton(
          onClick = onMenuClick,
          modifier = Modifier.size(36.dp).testTag("menu_icon_btn")
        ) {
          Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Hamburger option drawer",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
          )
        }
      }
    }
  }
}

@Composable
fun VersaCareerDrawerContent(
  currentRoute: String,
  onNavigate: (String) -> Unit,
  onClose: () -> Unit
) {
  val context = androidx.compose.ui.platform.LocalContext.current
  ModalDrawerSheet(
    drawerContainerColor = BrandBackground,
    drawerContentColor = Color.White,
    modifier = Modifier.width(300.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
      // Header Logo and branding
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 12.dp)
      ) {
        VersaCareerLogo(
          modifier = Modifier.size(32.dp),
          showBackground = false
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
          text = "VersaCareer AI",
          style = MaterialTheme.typography.titleMedium,
          color = Color.White,
          fontWeight = FontWeight.Black,
          letterSpacing = (-0.5).sp
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Navigation Options
      val items = listOf(
        Screen.Home,
        Screen.Dashboard,
        Screen.Roadmap,
        Screen.Profile
      )

      items.forEach { s ->
        val isSelected = currentRoute == s.route
        NavigationDrawerItem(
          icon = {
            Icon(
              imageVector = if (isSelected) s.activeIcon else s.inactiveIcon,
              contentDescription = s.label,
              tint = if (isSelected) BrandSecondary else BrandSecondaryText
            )
          },
          label = {
            Text(
              text = s.label,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) Color.White else BrandSecondaryText,
              style = MaterialTheme.typography.bodyMedium
            )
          },
          selected = isSelected,
          onClick = {
            onNavigate(s.route)
          },
          colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = BrandPrimary.copy(alpha = 0.2f),
            unselectedContainerColor = Color.Transparent
          ),
          modifier = Modifier
            .padding(vertical = 2.dp)
            .testTag("drawer_nav_${s.route}")
        )
      }

      Spacer(modifier = Modifier.weight(1f))

      HorizontalDivider(color = Color(0xFF334155), thickness = 1.dp)

      Spacer(modifier = Modifier.height(12.dp))

      // About
      NavigationDrawerItem(
        icon = {
          Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "About",
            tint = BrandSecondaryText
          )
        },
        label = {
          Text(
            "About VersaCareer AI",
            color = BrandSecondaryText,
            style = MaterialTheme.typography.bodyMedium
          )
        },
        selected = false,
        onClick = {
          onClose()
          android.widget.Toast.makeText(context, "VersaCareer AI v1.0 - Your ultimate path guides.", android.widget.Toast.LENGTH_SHORT).show()
        },
        colors = NavigationDrawerItemDefaults.colors(
          unselectedContainerColor = Color.Transparent
        ),
        modifier = Modifier.testTag("drawer_about")
      )

      // Help
      NavigationDrawerItem(
        icon = {
          Icon(
            imageVector = Icons.Default.HelpOutline,
            contentDescription = "Help",
            tint = BrandSecondaryText
          )
        },
        label = {
          Text(
            "Help & Support",
            color = BrandSecondaryText,
            style = MaterialTheme.typography.bodyMedium
          )
        },
        selected = false,
        onClick = {
          onClose()
          android.widget.Toast.makeText(context, "Contact: support@versacareer.ai", android.widget.Toast.LENGTH_SHORT).show()
        },
        colors = NavigationDrawerItemDefaults.colors(
          unselectedContainerColor = Color.Transparent
        ),
        modifier = Modifier.testTag("drawer_help")
      )

      // Feedback
      NavigationDrawerItem(
        icon = {
          Icon(
            imageVector = Icons.Default.RateReview,
            contentDescription = "Feedback",
            tint = BrandSecondaryText
          )
        },
        label = {
          Text(
            "Feedback",
            color = BrandSecondaryText,
            style = MaterialTheme.typography.bodyMedium
          )
        },
        selected = false,
        onClick = {
          onClose()
          android.widget.Toast.makeText(context, "Thank you for supporting us!", android.widget.Toast.LENGTH_SHORT).show()
        },
        colors = NavigationDrawerItemDefaults.colors(
          unselectedContainerColor = Color.Transparent
        ),
        modifier = Modifier.testTag("drawer_feedback")
      )

      Spacer(modifier = Modifier.height(12.dp))

      HorizontalDivider(color = Color(0xFF334155), thickness = 1.dp)

      Spacer(modifier = Modifier.height(12.dp))

      // Version Number
      Text(
        text = "Version 1.0",
        style = MaterialTheme.typography.labelSmall,
        color = BrandSecondaryText.copy(alpha = 0.6f),
        modifier = Modifier
          .padding(horizontal = 12.dp)
          .testTag("drawer_version")
      )
      Spacer(modifier = Modifier.height(8.dp))
    }
  }
}

@Composable
fun CustomBottomNavDock(
  currentRoute: String,
  onNavigate: (String) -> Unit
) {
  val items = listOf(
    Screen.Home,
    Screen.Dashboard,
    Screen.Upload, // Act as center item
    Screen.Roadmap,
    Screen.Profile
  )

  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .navigationBarsPadding(),
    color = Color.Transparent
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(84.dp)
        .background(
          brush = Brush.verticalGradient(
            colors = listOf(BrandCardBg.copy(alpha = 0.95f), BrandCardBg)
          )
        )
        .border(width = 1.dp, color = Color(0xFF334155).copy(alpha = 0.5f)),
      contentAlignment = Alignment.Center
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
      ) {
        items.forEach { s ->
          if (s == Screen.Upload) {
            // Highlighted floating button for center Upload
            Box(
              modifier = Modifier
                .offset(y = (-16).dp)
                .size(64.dp)
                .background(
                  brush = Brush.linearGradient(
                    colors = listOf(BrandPrimary, BrandSecondary)
                  ),
                  shape = CircleShape
                )
                .border(4.dp, BrandBackground, CircleShape)
                .clickable { onNavigate(s.route) }
                .testTag("nav_btn_upload"),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.CloudUpload,
                contentDescription = "Highlighted Upload button",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
              )
            }
          } else {
            // General Navigation tab column
            val isSelected = currentRoute == s.route
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Center,
              modifier = Modifier
                .width(64.dp)
                .fillMaxHeight()
                .clickable { onNavigate(s.route) }
                .testTag("nav_btn_${s.route}")
            ) {
              Icon(
                imageVector = if (isSelected) s.activeIcon else s.inactiveIcon,
                contentDescription = s.label,
                tint = if (isSelected) BrandSecondary else BrandSecondaryText,
                modifier = Modifier.size(24.dp)
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = s.label,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) BrandSecondary else BrandSecondaryText,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
              )
            }
          }
        }
      }
    }
  }
}

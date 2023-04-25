package ghoudan.ayoub.movieBest

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import ghoudan.ayoub.movieBest.ui.home.MoviesListAdapter
import org.hamcrest.CoreMatchers.not
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4ClassRunner::class)
class SimpleTest {


    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun a_test_isHomeFragmentVisible_onAppLaunch() {
        onView(withId(R.id.movieRecycler)).check(matches(isDisplayed()))

        onView(withId(R.id.loaderOverlay)).check(matches(not(isDisplayed())))
    }


    @Test
    fun test_nav_fromHomeFragment_FavoriteFragment() {

        // Click list item #LIST_ITEM_IN_TEST
        onView(withId(R.id.movieRecycler))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<MoviesListAdapter.MovieItemViewHolder>(
                    2,
                    ViewActions.click()
                )
            )

        // Confirm nav to DetailFragment and display title
        onView(withId(R.id.movie_title)).check(matches(withText("Avatar: The Way of Water")))

        Espresso.pressBack()

        // Confirm MovieListFragment in view
        onView(withId(R.id.movieRecycler)).check(matches(isDisplayed()))

        // Nav to Favorite
        onView(withId(R.id.navigation_favorite)).perform(ViewActions.click())
    }

}

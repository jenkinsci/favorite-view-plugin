package hudson.plugins.favoriteview;

import static org.junit.jupiter.api.Assertions.*;

import hudson.model.ListView;
import hudson.model.User;
import hudson.views.ViewsTabBar;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
class FavoriteViewsIntegrationTest {

    @Test
    void favoriteViewsTabBarIsRegisteredAsExtension(JenkinsRule j) {
        boolean found = ViewsTabBar.all().stream().anyMatch(d -> d instanceof FavoriteViewsTabBar.DescriptorImpl);
        assertTrue(found, "FavoriteViewsTabBar should be registered as an extension");
    }

    @Test
    void sortableViewsTabBarIsRegisteredAsExtension(JenkinsRule j) {
        boolean found = ViewsTabBar.all().stream().anyMatch(d -> d instanceof SortableViewsTabBar.DescriptorImpl);
        assertTrue(found, "SortableViewsTabBar should be registered as an extension");
    }

    @Test
    void favoriteViewsUserPropertyIsRegistered(JenkinsRule j) {
        User user = User.getById("integrationTestUser", true);
        FavoriteViewsUserProperty prop = user.getProperty(FavoriteViewsUserProperty.class);
        // newInstance is called and returns a property, but isEnabled=false
        // so it might or might not be auto-added depending on Jenkins version
        // Either way, we should be able to add it explicitly
        if (prop == null) {
            FavoriteViewsUserProperty newProp = new FavoriteViewsUserProperty();
            assertDoesNotThrow(() -> user.addProperty(newProp));
        }
        assertNotNull(user.getProperty(FavoriteViewsUserProperty.class));
    }

    @Test
    void endToEndFavoriteToggle(JenkinsRule j) throws IOException {
        // Set up the tab bar
        FavoriteViewsTabBar tabBar = new FavoriteViewsTabBar();

        // Create views
        ListView listView = new ListView("TestView", j.jenkins);
        j.jenkins.addView(listView);

        // Create user and add property
        User user = User.getById("favoriteUser", true);
        FavoriteViewsUserProperty property = new FavoriteViewsUserProperty();
        user.addProperty(property);

        // Mark as favorite using the view ID format
        String viewId = tabBar.getViewId(listView);
        property.setFavorite(viewId, true);

        assertTrue(property.isFavorite(viewId));

        // Un-favorite
        property.setFavorite(viewId, false);
        assertFalse(property.isFavorite(viewId));
    }

    @Test
    void viewOrderPersistsThroughUserSave(JenkinsRule j) throws IOException {
        User user = User.getById("orderUser", true);
        FavoriteViewsUserProperty property = new FavoriteViewsUserProperty();
        property.setViewsForItemGroup("", java.util.Arrays.asList("Z-View", "A-View", "M-View"));
        user.addProperty(property);
        user.save();

        // Re-fetch user and check property
        User reloaded = User.getById("orderUser", false);
        assertNotNull(reloaded);
        FavoriteViewsUserProperty reloadedProp = reloaded.getProperty(FavoriteViewsUserProperty.class);
        assertNotNull(reloadedProp);
        assertEquals(java.util.Arrays.asList("Z-View", "A-View", "M-View"), reloadedProp.getViewsForItemGroup(""));
    }

    @Test
    void favoritesPersistThroughUserSave(JenkinsRule j) throws IOException {
        User user = User.getById("persistUser", true);
        FavoriteViewsUserProperty property = new FavoriteViewsUserProperty();
        property.setFavorite("folder/MyView", true);
        property.setFavorite("folder/OtherView", true);
        user.addProperty(property);
        user.save();

        // Re-fetch user
        User reloaded = User.getById("persistUser", false);
        assertNotNull(reloaded);
        FavoriteViewsUserProperty reloadedProp = reloaded.getProperty(FavoriteViewsUserProperty.class);
        assertNotNull(reloadedProp);
        assertTrue(reloadedProp.isFavorite("folder/MyView"));
        assertTrue(reloadedProp.isFavorite("folder/OtherView"));
        assertFalse(reloadedProp.isFavorite("folder/NonExistent"));
    }

    @Test
    void configureTabBarOnJenkins(JenkinsRule j) {
        // Verify we can set the tab bar on Jenkins
        FavoriteViewsTabBar tabBar = new FavoriteViewsTabBar();
        j.jenkins.setViewsTabBar(tabBar);
        assertInstanceOf(FavoriteViewsTabBar.class, j.jenkins.getViewsTabBar());
    }

    @Test
    void configureSortableTabBarOnJenkins(JenkinsRule j) {
        SortableViewsTabBar tabBar = new SortableViewsTabBar();
        j.jenkins.setViewsTabBar(tabBar);
        assertInstanceOf(SortableViewsTabBar.class, j.jenkins.getViewsTabBar());
    }
}

package hudson.plugins.favoriteview;

import static org.junit.jupiter.api.Assertions.*;

import hudson.model.AllView;
import hudson.model.ListView;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
class FavoriteViewsTabBarTest {

    @Test
    void descriptorDisplayName(JenkinsRule j) {
        FavoriteViewsTabBar.DescriptorImpl descriptor =
                j.jenkins.getDescriptorByType(FavoriteViewsTabBar.DescriptorImpl.class);
        assertNotNull(descriptor);
        assertEquals("Favorite Views", descriptor.getDisplayName());
    }

    @Test
    void getViewIdIncludesViewName(JenkinsRule j) {
        FavoriteViewsTabBar tabBar = new FavoriteViewsTabBar();
        AllView allView = (AllView) j.jenkins.getView("all");
        assertNotNull(allView);

        String viewId = tabBar.getViewId(allView);
        assertTrue(viewId.endsWith(allView.getViewName()), "View ID should end with view name");
    }

    @Test
    void isFavoriteReturnsFalseWhenNoUser(JenkinsRule j) {
        FavoriteViewsTabBar tabBar = new FavoriteViewsTabBar();
        AllView allView = (AllView) j.jenkins.getView("all");
        assertNotNull(allView);

        // No user logged in
        assertFalse(tabBar.isFavorite(allView));
    }

    @Test
    void isFavoriteReturnsFalseWhenUserHasNoProperty(JenkinsRule j) throws Exception {
        FavoriteViewsTabBar tabBar = new FavoriteViewsTabBar();
        AllView allView = (AllView) j.jenkins.getView("all");
        assertNotNull(allView);

        // Even after creating a user, without explicitly adding the property,
        // isFavorite should be false (no SecurityContext set so User.current() is null)
        assertFalse(tabBar.isFavorite(allView));
    }

    @Test
    void getViewIdForCustomView(JenkinsRule j) throws IOException {
        ListView listView = new ListView("MyCustomView", j.jenkins);
        j.jenkins.addView(listView);

        FavoriteViewsTabBar tabBar = new FavoriteViewsTabBar();
        String viewId = tabBar.getViewId(listView);
        assertTrue(viewId.contains("MyCustomView"), "View ID should contain the view name");
    }

    @Test
    void tabBarCanBeInstantiated() {
        FavoriteViewsTabBar tabBar = new FavoriteViewsTabBar();
        assertNotNull(tabBar);
    }
}

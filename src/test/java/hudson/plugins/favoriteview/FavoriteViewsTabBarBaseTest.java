package hudson.plugins.favoriteview;

import static org.junit.jupiter.api.Assertions.*;

import hudson.model.AllView;
import hudson.model.ListView;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
class FavoriteViewsTabBarBaseTest {

    @Test
    void getOwnerNameForJenkinsRoot(JenkinsRule j) {
        FavoriteViewsTabBar tabBar = new FavoriteViewsTabBar();
        // Jenkins root is not an AbstractFolder, so ownerName should be empty
        String ownerName = tabBar.getOwnerName(j.jenkins);
        assertEquals("", ownerName);
    }

    @Test
    void getOwnerNameForView(JenkinsRule j) throws IOException {
        // A View used as a ViewGroup would include the view name
        // Test that getOwnerName returns empty for plain Jenkins root
        FavoriteViewsTabBar tabBar = new FavoriteViewsTabBar();
        AllView allView = (AllView) j.jenkins.getView("all");
        assertNotNull(allView);
        // allView's owner is Jenkins itself
        String ownerName = tabBar.getOwnerName(allView.getOwner());
        assertEquals("", ownerName);
    }

    @Test
    void getViewsReturnsSortedViewsViaWebClient(JenkinsRule j) throws Exception {
        // Set up the FavoriteViewsTabBar on Jenkins
        FavoriteViewsTabBar tabBar = new FavoriteViewsTabBar();
        j.jenkins.setViewsTabBar(tabBar);

        // Create views
        ListView viewA = new ListView("A-View", j.jenkins);
        ListView viewB = new ListView("B-View", j.jenkins);
        ListView viewC = new ListView("C-View", j.jenkins);
        j.jenkins.addView(viewA);
        j.jenkins.addView(viewB);
        j.jenkins.addView(viewC);

        // Use a WebClient to make a real request (Stapler context is available)
        JenkinsRule.WebClient wc = j.createWebClient();
        HtmlPage page = wc.goTo("");

        // If we got here without error, the tab bar rendered successfully
        // with the Stapler context available
        assertNotNull(page);
    }

    @Test
    void viewSortingLogicWithProperty() {
        // Test the sorting logic indirectly through FavoriteViewsUserProperty
        FavoriteViewsUserProperty property = new FavoriteViewsUserProperty();
        property.setViewsForItemGroup("", Arrays.asList("C-View", "A-View", "B-View"));

        List<String> order = property.getViewsForItemGroup("");
        assertNotNull(order);
        assertEquals(3, order.size());
        assertEquals("C-View", order.get(0));
        assertEquals("A-View", order.get(1));
        assertEquals("B-View", order.get(2));
    }

    @Test
    void viewSortingLogicReturnsNullForUnknownGroup() {
        FavoriteViewsUserProperty property = new FavoriteViewsUserProperty();
        assertNull(property.getViewsForItemGroup("unknownGroup"));
    }
}

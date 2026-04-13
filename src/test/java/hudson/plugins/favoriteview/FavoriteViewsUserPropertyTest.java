package hudson.plugins.favoriteview;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kohsuke.stapler.StaplerRequest2;

class FavoriteViewsUserPropertyTest {

    private FavoriteViewsUserProperty property;

    @BeforeEach
    void setUp() {
        property = new FavoriteViewsUserProperty();
    }

    @Test
    void isFavoriteReturnsFalseByDefault() {
        assertFalse(property.isFavorite("someView"));
    }

    @Test
    void setFavoriteTrueAddsView() throws IOException {
        property.setFavorite("myView", true);
        assertTrue(property.isFavorite("myView"));
    }

    @Test
    void setFavoriteFalseRemovesView() throws IOException {
        property.setFavorite("myView", true);
        assertTrue(property.isFavorite("myView"));

        property.setFavorite("myView", false);
        assertFalse(property.isFavorite("myView"));
    }

    @Test
    void removingNonExistentFavoriteDoesNotThrow() throws IOException {
        assertDoesNotThrow(() -> property.setFavorite("nonExistent", false));
        assertFalse(property.isFavorite("nonExistent"));
    }

    @Test
    void multipleFavoritesAreIndependent() throws IOException {
        property.setFavorite("view1", true);
        property.setFavorite("view2", true);
        property.setFavorite("view3", true);

        assertTrue(property.isFavorite("view1"));
        assertTrue(property.isFavorite("view2"));
        assertTrue(property.isFavorite("view3"));

        property.setFavorite("view2", false);

        assertTrue(property.isFavorite("view1"));
        assertFalse(property.isFavorite("view2"));
        assertTrue(property.isFavorite("view3"));
    }

    @Test
    void getViewsForItemGroupReturnsNullWhenNotSet() {
        assertNull(property.getViewsForItemGroup("someGroup"));
    }

    @Test
    void setAndGetViewsForItemGroup() {
        List<String> views = Arrays.asList("view1", "view2", "view3");
        property.setViewsForItemGroup("myGroup", views);

        List<String> result = property.getViewsForItemGroup("myGroup");
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(Arrays.asList("view1", "view2", "view3"), result);
    }

    @Test
    void viewOrderForDifferentItemGroupsAreIndependent() {
        List<String> groupAViews = Arrays.asList("a1", "a2");
        List<String> groupBViews = Arrays.asList("b1", "b2", "b3");

        property.setViewsForItemGroup("groupA", groupAViews);
        property.setViewsForItemGroup("groupB", groupBViews);

        assertEquals(Arrays.asList("a1", "a2"), property.getViewsForItemGroup("groupA"));
        assertEquals(Arrays.asList("b1", "b2", "b3"), property.getViewsForItemGroup("groupB"));
    }

    @Test
    void overwritingViewOrderForItemGroup() {
        property.setViewsForItemGroup("group", Arrays.asList("old1", "old2"));
        property.setViewsForItemGroup("group", Arrays.asList("new1"));

        List<String> result = property.getViewsForItemGroup("group");
        assertEquals(1, result.size());
        assertEquals("new1", result.get(0));
    }

    @Test
    void readResolveInitializesNullViewOrder() throws Exception {
        // Simulate deserialization where viewOrder could be null
        // Use reflection to set viewOrder to null
        var field = FavoriteViewsUserProperty.class.getDeclaredField("viewOrder");
        field.setAccessible(true);
        field.set(property, null);

        Object result = property.readResolve();
        assertSame(property, result);

        // After readResolve, setting view order should not throw
        assertDoesNotThrow(() -> property.setViewsForItemGroup("test", Arrays.asList("v1")));
        assertEquals(Arrays.asList("v1"), property.getViewsForItemGroup("test"));
    }

    @Test
    void readResolvePreservesExistingViewOrder() {
        property.setViewsForItemGroup("group", Arrays.asList("v1", "v2"));

        Object result = property.readResolve();
        assertSame(property, result);

        assertEquals(Arrays.asList("v1", "v2"), property.getViewsForItemGroup("group"));
    }

    @Test
    void favoriteWithEmptyStringViewId() throws IOException {
        property.setFavorite("", true);
        assertTrue(property.isFavorite(""));

        property.setFavorite("", false);
        assertFalse(property.isFavorite(""));
    }

    @Test
    void viewIdWithSlashesAndSpecialChars() throws IOException {
        String viewId = "folder/subfolder$MyView";
        property.setFavorite(viewId, true);
        assertTrue(property.isFavorite(viewId));
    }

    @Test
    void reconfigureReturnsSameInstance() {
        FavoriteViewsUserProperty result =
                (FavoriteViewsUserProperty) property.reconfigure((StaplerRequest2) null, null);
        assertSame(property, result);
    }
}

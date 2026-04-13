package hudson.plugins.favoriteview;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
class SortableViewsTabBarTest {

    @Test
    void descriptorDisplayName(JenkinsRule j) {
        SortableViewsTabBar.DescriptorImpl descriptor =
                j.jenkins.getDescriptorByType(SortableViewsTabBar.DescriptorImpl.class);
        assertNotNull(descriptor);
        assertEquals("Sortable Views", descriptor.getDisplayName());
    }

    @Test
    void tabBarCanBeInstantiated() {
        SortableViewsTabBar tabBar = new SortableViewsTabBar();
        assertNotNull(tabBar);
    }
}

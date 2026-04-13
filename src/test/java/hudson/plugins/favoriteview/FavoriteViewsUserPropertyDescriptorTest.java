package hudson.plugins.favoriteview;

import static org.junit.jupiter.api.Assertions.*;

import hudson.model.User;
import hudson.model.UserProperty;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
class FavoriteViewsUserPropertyDescriptorTest {

    @Test
    void descriptorDisplayName(JenkinsRule j) {
        FavoriteViewsUserProperty.DescriptorImpl descriptor =
                j.jenkins.getDescriptorByType(FavoriteViewsUserProperty.DescriptorImpl.class);
        assertNotNull(descriptor);
        assertEquals("Favorite Views", descriptor.getDisplayName());
    }

    @Test
    void descriptorIsDisabled(JenkinsRule j) {
        FavoriteViewsUserProperty.DescriptorImpl descriptor =
                j.jenkins.getDescriptorByType(FavoriteViewsUserProperty.DescriptorImpl.class);
        assertFalse(descriptor.isEnabled());
    }

    @Test
    void newInstanceReturnsProperty(JenkinsRule j) {
        FavoriteViewsUserProperty.DescriptorImpl descriptor =
                j.jenkins.getDescriptorByType(FavoriteViewsUserProperty.DescriptorImpl.class);
        User user = User.getById("testuser", true);
        UserProperty prop = descriptor.newInstance(user);
        assertNotNull(prop);
        assertInstanceOf(FavoriteViewsUserProperty.class, prop);
    }
}

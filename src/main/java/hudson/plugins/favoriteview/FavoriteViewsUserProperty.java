package hudson.plugins.favoriteview;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.User;
import hudson.model.UserProperty;
import hudson.model.UserPropertyDescriptor;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.json.JSONObject;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.StaplerRequest2;

public class FavoriteViewsUserProperty extends UserProperty {
    private final Set<String> favoriteViews = Collections.synchronizedSet(new HashSet<>());

    private Map<String, List<String>> viewOrder = new HashMap<>();

    public boolean isFavorite(String viewId) {
        return favoriteViews.contains(viewId);
    }

    public Object readResolve() {
        if (viewOrder == null) {
            viewOrder = new HashMap<>();
        }
        return this;
    }

    public void setFavorite(String viewId, boolean favorite) throws IOException {
        if (favorite) {
            favoriteViews.add(viewId);
        } else {
            favoriteViews.remove(viewId);
        }
    }

    @CheckForNull
    public List<String> getViewsForItemGroup(String itemGroup) {
        return viewOrder.get(itemGroup);
    }

    public void setViewsForItemGroup(String itemGroup, List<String> views) {
        viewOrder.put(itemGroup, views);
    }

    @Override
    public UserProperty reconfigure(StaplerRequest2 req, JSONObject form) {
        return this;
    }

    @Extension
    @Symbol("favoriteViews")
    public static class DescriptorImpl extends UserPropertyDescriptor {

        @Override
        public UserProperty newInstance(User user) {
            return new FavoriteViewsUserProperty();
        }

        /*
         * Intentionally set to false to hide it from the config screen.
         * @return false
         */
        @Override
        public boolean isEnabled() {
            return false;
        }

        @Override
        @NonNull
        public String getDisplayName() {
            return "Favorite Views";
        }
    }
}

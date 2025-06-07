package hudson.plugins.favoriteview;

import com.cloudbees.hudson.plugins.folder.AbstractFolder;
import hudson.model.ItemGroup;
import hudson.model.User;
import hudson.model.View;
import hudson.model.ViewGroup;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.kohsuke.stapler.Stapler;

public interface FavoriteViewsTabBarBase {

    default String getItemGroup() {
        ViewGroup viewGroup = Stapler.getCurrentRequest2().findAncestorObject(ViewGroup.class);
        return getOwnerName(viewGroup);
    }

    default String getOwnerName(ViewGroup owner) {
        StringBuilder ownerFullName = new StringBuilder();
        // Support for nested-view plugin where a view is also a ViewGroup
        while (owner instanceof View v) {
            ownerFullName.insert(0, v.getViewName() + "$");
            owner = v.getOwner();
        }

        if (owner instanceof AbstractFolder<?> f) {
            ownerFullName.insert(0, f.getFullName() + "/");
        }
        return ownerFullName.toString();
    }

    default Collection<View> getViews(Collection<View> views) {
        ItemGroup<?> itemGroup = Stapler.getCurrentRequest2().findAncestorObject(ItemGroup.class);
        User user = User.current();
        if (user == null) {
            return views;
        }
        FavoriteViewsUserProperty property = user.getProperty(FavoriteViewsUserProperty.class);
        if (property == null) {
            return views;
        }

        String itemFullname = itemGroup.getFullName();
        List<String> viewsForItemGroup = property.getViewsForItemGroup(itemFullname);

        if (viewsForItemGroup == null) {
            return views;
        }

        Map<String, View> allViewsMap = new TreeMap<>();
        views.forEach(view -> allViewsMap.put(view.getViewName(), view));
        List<View> sortedViews = new ArrayList<>();
        viewsForItemGroup.forEach(viewName -> {
            if (allViewsMap.containsKey(viewName)) {
                sortedViews.add(allViewsMap.get(viewName));
            }
        });
        allViewsMap.forEach((viewName, view) -> {
            if (!viewsForItemGroup.contains(viewName)) {
                sortedViews.add(view);
            }
        });

        return sortedViews;
    }
}

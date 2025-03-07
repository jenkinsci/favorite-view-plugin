package hudson.plugins.favoriteview;

import com.cloudbees.hudson.plugins.folder.AbstractFolder;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.User;
import hudson.model.View;
import hudson.model.ViewGroup;
import hudson.views.ViewsTabBar;
import java.io.IOException;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.verb.POST;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.HttpResponses;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.Stapler;

public class FavoriteViewsTabBar extends ViewsTabBar implements FavoriteViewsTabBarBase {

	@DataBoundConstructor
	public FavoriteViewsTabBar() {
	}

	@Extension
  @Symbol("favoriteViews")
	public static class DescriptorImpl extends AbstractFavoriteViewsTabBarDescriptor {

		@Override
    @NonNull
		public String getDisplayName() {
			return "Favorite Views";
		}

    @POST
    public void doToggleFavorite(
				@QueryParameter("favorite") String favorite,
				@QueryParameter("view") String view) throws IOException {
      User user = User.current();
      if (user != null) {

        FavoriteViewsUserProperty property = user.getProperty(FavoriteViewsUserProperty.class);
        if (property == null) {
          property = new FavoriteViewsUserProperty();
          user.addProperty(property);
        }

        property.setFavorite(view, "true".equals(favorite));
        user.save();
      } else {
        throw HttpResponses.forbidden();
      }
    }

	}

	public static View getView() {
		return Stapler.getCurrentRequest2().findAncestorObject(View.class);
	}

	public String getViewId(View view) {
    return getOwnerName(view.getOwner()) + view.getViewName();
	}

	public boolean isFavorite(View view) {
		User user = User.current();
		if (user == null)
			return false;

		FavoriteViewsUserProperty property = user
				.getProperty(FavoriteViewsUserProperty.class);
		if (property == null)
			return false;

		String viewId = getViewId(view);
		return property.isFavorite(viewId);
	}
}

package hudson.plugins.favoriteview;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.User;
import hudson.model.View;
import hudson.views.ViewsTabBar;
import hudson.views.ViewsTabBarDescriptor;
import java.io.IOException;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.StaplerResponse2;
import org.kohsuke.stapler.verb.POST;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.HttpResponses;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.Stapler;

public class FavoriteViewsTabBar extends ViewsTabBar {

	@DataBoundConstructor
	public FavoriteViewsTabBar() {
	}

	@Extension
  @Symbol("favoriteViews")
	public static class DescriptorImpl extends ViewsTabBarDescriptor {

		@Override
    @NonNull
		public String getDisplayName() {
			return "Favorite Views";
		}

    @POST
    public void doToggleFavorite(
        StaplerResponse2 resp,
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

	public static String getViewId(View view) {
    String ownerFullName = view.getOwner().getItemGroup().getFullName();
    if (!"".equals(ownerFullName)) {
      return ownerFullName + "/" + view.getDisplayName();
    }
		return view.getDisplayName();
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

package hudson.plugins.favoriteview;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.User;
import hudson.model.View;
import hudson.security.ACL;
import hudson.security.AccessControlled;
import hudson.security.Permission;
import hudson.views.ViewsTabBar;
import hudson.views.ViewsTabBarDescriptor;

import java.io.IOException;

import jenkins.model.Jenkins;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.verb.POST;
import org.springframework.security.access.AccessDeniedException;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.HttpResponse;
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
    public HttpResponse doToggleFavorite(
				@QueryParameter("favorite") String favorite,
				@QueryParameter("view") String view) throws IOException {
			User user = User.current();
			if (user == null)
				return HttpResponses.forbidden();

			FavoriteViewsUserProperty property = user
					.getProperty(FavoriteViewsUserProperty.class);
			if (property == null) {
				property = new FavoriteViewsUserProperty();
				user.addProperty(property);
			}

			property.setFavorite(view, "true".equals(favorite));
			user.save();

			return HttpResponses.forwardToPreviousPage();
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

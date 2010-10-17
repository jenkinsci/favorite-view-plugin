package hudson.plugins.favoriteview;

import hudson.Extension;
import hudson.model.Hudson;
import hudson.model.User;
import hudson.model.View;
import hudson.security.ACL;
import hudson.security.AccessControlled;
import hudson.security.Permission;
import hudson.views.ViewsTabBar;
import hudson.views.ViewsTabBarDescriptor;

import java.io.IOException;

import org.acegisecurity.AccessDeniedException;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.HttpResponses;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.Stapler;

public class FavoriteViewsTabBar extends ViewsTabBar implements
		AccessControlled {

	@DataBoundConstructor
	public FavoriteViewsTabBar() {
	}

	@Extension
	public static class DescriptorImpl extends ViewsTabBarDescriptor {

		@Override
		public String getDisplayName() {
			return "Favorite Views";
		}

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
		return Stapler.getCurrentRequest().findAncestorObject(View.class);
	}

	public static String getViewId(View view) {
		return view.getDisplayName();
	}

	public static View getViewById(String id) {
		return Hudson.getInstance().getView(id);
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

	public void checkPermission(Permission p) throws AccessDeniedException {
		getACL().checkPermission(p);
	}

	public ACL getACL() {
		return getView().getACL();
	}

	public boolean hasPermission(Permission p) {
		return getACL().hasPermission(p);
	}
}

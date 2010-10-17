package hudson.plugins.favoriteview;

import hudson.Extension;
import hudson.model.UserProperty;
import hudson.model.UserPropertyDescriptor;
import hudson.model.User;
import hudson.model.Descriptor.FormException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.StaplerRequest;

public class FavoriteViewsUserProperty extends UserProperty {
	private Set<String> favoriteViews = Collections
			.synchronizedSet(new HashSet<String>());

	public boolean isFavorite(String viewId) {
		return favoriteViews.contains(viewId);
	}

	public void setFavorite(String viewId, boolean favorite) {
		if (favorite) {
			favoriteViews.add(viewId);
		} else {
			favoriteViews.remove(viewId);
		}
	}
	
	@Override
	public UserProperty reconfigure(StaplerRequest req, JSONObject form)
			throws FormException {
		return this;
	}

	@Extension
	public static class DescriptorImpl extends UserPropertyDescriptor {

		@Override
		public UserProperty newInstance(User user) {
			return new FavoriteViewsUserProperty();
		}
		
		@Override
		public String getDisplayName() {
			return "Favorite Views";
		}

	}

}
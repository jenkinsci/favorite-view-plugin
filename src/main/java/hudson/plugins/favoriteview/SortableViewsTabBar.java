package hudson.plugins.favoriteview;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.views.ViewsTabBar;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

public class SortableViewsTabBar extends ViewsTabBar implements FavoriteViewsTabBarBase {

  @DataBoundConstructor
  public SortableViewsTabBar() {
  }

  @Extension
  @Symbol("sortable")
  public static class DescriptorImpl extends AbstractFavoriteViewsTabBarDescriptor {

    @Override
    @NonNull
    public String getDisplayName() {
      return "Sortable Views";
    }
  }
}

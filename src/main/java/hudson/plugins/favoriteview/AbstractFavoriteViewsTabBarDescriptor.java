package hudson.plugins.favoriteview;

import hudson.model.User;
import hudson.views.ViewsTabBarDescriptor;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.stream.Collectors;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest2;
import org.kohsuke.stapler.StaplerResponse2;
import org.kohsuke.stapler.verb.POST;

public abstract class AbstractFavoriteViewsTabBarDescriptor extends ViewsTabBarDescriptor {

  @POST
  public void doSortViews(StaplerRequest2 req, StaplerResponse2 resp) throws ServletException, IOException {
    User user = User.current();
    if (user != null) {
      JSONObject src = req.getSubmittedForm();
      FavoriteViewsUserProperty property = user.getProperty(FavoriteViewsUserProperty.class);
      if (property == null) {
        property = new FavoriteViewsUserProperty();
        user.addProperty(property);
      }
      property.setViewsForItemGroup(src.getString("itemGroup"), src.getJSONArray("viewName")
          .stream().map(Object::toString).collect(Collectors.toList()));
      user.save();
    }
    resp.sendRedirect2(req.getReferer());
  }
}

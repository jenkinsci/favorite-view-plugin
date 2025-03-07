Behaviour.specify(".fv-icon", "favorite-view-toggle", 0, (e) => {
  e.onclick = (event) => {
    event.preventDefault();
    toggleFavoriteView(e);
  }
});

function toggleFavoriteView(e) {
  const parentDiv = e.closest("div");
  const viewId = parentDiv.dataset.viewId;
  const url = parentDiv.dataset.url;
  const favorite = parentDiv.dataset.favorite === "true";
  var params = new URLSearchParams({
    favorite: !favorite,
    view: viewId,
  });
  fetch(url + "?" + params, {
    method: 'post',
    headers: crumb.wrap({}),
  }).then((rsp) => {
    if (rsp.ok) {
      if (favorite) {
        parentDiv.setAttribute('data-favorite', 'false');
        parentDiv.querySelector('.fv-icon-active').classList.add('jenkins-hidden');
        parentDiv.querySelector('.fv-icon-inactive').classList.remove('jenkins-hidden');
      } else {
        parentDiv.setAttribute('data-favorite', 'true');
        parentDiv.querySelector('.fv-icon-inactive').classList.add('jenkins-hidden');
        parentDiv.querySelector('.fv-icon-active').classList.remove('jenkins-hidden');
      }
    }
  });
}

Behaviour.specify("#fv-sortTabs", "favorite-view-sort-tabs", 0, (e) => {
  e.onclick = (event) => {
    event.preventDefault();
    const formTemplate = document.getElementById("fv-sortTabs-template");
    const form = formTemplate.firstElementChild.cloneNode(true);
    form.classList.remove("no-json");
    const title = formTemplate.dataset.title;
    dialog.form(form, {
      title: title,
      okText: "Sort",
    });
  }
});

Behaviour.specify("#fv-sortcontainer", "favorite-view-sortcontainer", 0, function(s) {
  registerSortableDragDrop(s);
});

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

Behaviour.specify(".fv-overflow", "favorite-view-overflow", 0, (e) => {
  e.onclick = () => {
    top.location.href = e.dataset.url;
  }
});

Favorite view plugin
==============================
[![Build Status](https://ci.jenkins.io/buildStatus/icon?job=Plugins/favorite-view-plugin/master)](https://ci.jenkins.io/job/plugins/job/favorite-view-plugin/)
[![Jenkins Plugin](https://img.shields.io/jenkins/plugin/v/favorite-view.svg)](https://plugins.jenkins.io/favorite-view/)
[![Jenkins Plugin Installs](https://img.shields.io/jenkins/plugin/i/favorite-view.svg?color=blue)](https://plugins.jenkins.io/favorite-view/)
[![Contributors](https://img.shields.io/github/contributors/jenkinsci/favorite-view-plugin.svg)](https://github.com/jenkinsci/favorite-view-plugin/graphs/contributors)

The plugin provides 2 alternative implementation of the tab bar.
Both support sorting of the tabs on a per user basis.
* Favorite Views<br/>
  Users can mark some views as favorites, and these will show up as tabs. Other views are listed in an overflow button.<br/>
  ![Favorite](/docs/favorite-view-selected.png)
  ![Other](/docs/non-favorite-view-selected.png)


* Sortable Views<br/>
  ![Sortable](/docs/sortable.png)

### Enable
Go to `Manage Jenkins -> System` or the folder config and select `Favorite Views` or `Sortable Views` from the `Views Tab Bar` dropdown.


### Known limitations
- When a favorite view is renamed it will no longer be a favorite.
- When a folder is renamed the favorite information is lost for that folder.
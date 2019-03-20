import React, {Component} from 'react';
import { Route, Switch } from 'react-router-dom'

import PlaylistList from './PlaylistList'
import AddPlaylistForm from './AddPlaylistForm'
import UpdatePlaylistForm from './UpdatePlaylistForm'

class PlaylistsHome extends Component {
  render() {
    return (
      <div>
        <Switch>
          <Route exact path="/playlist" component={PlaylistList} />
          <Route exact path="/playlist/add" component={AddPlaylistForm} />
          <Route path="/playlist/:id" component={UpdatePlaylistForm} />
        </Switch>
      </div>
    )
  }
}
export default PlaylistsHome;

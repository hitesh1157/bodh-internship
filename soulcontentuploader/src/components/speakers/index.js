import React, {Component} from 'react';
import { Route, Switch } from 'react-router-dom'

import SpeakersList from './SpeakersList'
import SpeakerForm from './SpeakerForm'

class SpeakersHome extends Component {
  render() {
    return (
      <div>
        <Switch>
          <Route exact path="/speaker" component={SpeakersList} />
          <Route exact path="/speaker/add" component={SpeakerForm} />
          <Route path="/speaker/:id" component={SpeakerForm} />
        </Switch>
      </div>
    )
  }
}
export default SpeakersHome;

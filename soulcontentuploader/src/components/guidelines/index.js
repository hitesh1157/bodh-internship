import React, {Component} from 'react';
import { Route, Switch } from 'react-router-dom'

import Guidelines from './Guidelines'

class GuidelinesHome extends Component {
  render() {
    return (
      <div>
        <Switch>
          <Route exact path="/guidelines" component={Guidelines} />
        </Switch>
      </div>
    )
  }
}
export default GuidelinesHome;

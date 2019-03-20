import React, {Component} from 'react';
import { Route, Switch } from 'react-router-dom'

import PoemsList from './PoemsList'
import PoemForm from './PoemForm'

class PoemsHome extends Component {
  render() {
    return (
      <div>
        <Switch>
          <Route exact path="/poem" component={PoemsList} />
          <Route exact path="/poem/add" component={PoemForm} />
          <Route path="/poem/:id" component={PoemForm} />
        </Switch>
      </div>
    )
  }
}
export default PoemsHome;

import React, {Component} from 'react';
import { Route, Switch } from 'react-router-dom'

import LecturesList from './LecturesList'
import AddLectureForm from './AddLectureForm'
import UpdateLectureForm from './UpdateLectureForm'

class LecturesHome extends Component {
  render() {
    return (
      <div>
        <Switch>
          <Route exact path="/lecture" component={LecturesList} />
          <Route exact path="/lecture/add" component={AddLectureForm} />
          <Route path="/lecture/:id" component={UpdateLectureForm} />
        </Switch>
      </div>
    )
  }
}
export default LecturesHome;

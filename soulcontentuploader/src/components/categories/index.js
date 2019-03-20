import React, {Component} from 'react';
import { Route, Switch } from 'react-router-dom'

import CategoriesList from './CategoriesList'
import AddCategoryForm from './AddCategoryForm'
import UpdateCategoryForm from './UpdateCategoryForm'
import CategoryReordering from './CategoryReordering'

class CategoriesHome extends Component {
  render() {
    return (
      <div>
        <Switch>
          <Route exact path="/category" component={CategoriesList} />
          <Route exact path="/category/add" component={AddCategoryForm} />
          <Route exact path="/category/reorder" component={CategoryReordering} />
          <Route path="/category/:id" component={UpdateCategoryForm} />
        </Switch>
      </div>
    )
  }
}
export default CategoriesHome;

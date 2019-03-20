import React from 'react';
import { Link } from 'react-router-dom'
import Search from '../shared/Search';
import Categories from '../../services/categories'

import RaisedButton from 'material-ui/RaisedButton';

import './categories.css';


const styles = {
  addBtn: {
    margin: '20px 20px 0 0',
    float: 'right'
  },
  title: {
    padding: 20
  }
};

class CategoryReordering extends React.Component{
  constructor(props) {
    super(props)
    this.state = {
      selectedCategories: [],
      allCategories: [],
      page: false
    };
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleSelectedCategoriesChange = this.handleSelectedCategoriesChange.bind(this);
    this.getValues = this.getValues.bind(this);
    
  }

  componentWillMount() {
    Categories.getAll()
    .then((response) => {
       var categories = response.data;
       var items = [];
       categories.forEach(function(category, index) {
        console.log(category);
        items.push({
          id: category.id,
          value: category.name.en,
          
        });
       })
       this.getValues(items)
    })
    .catch(err => {
      alert(err);
    })
  }


  getValues(items) {

    this.setState({
      "allCategories": items,
      "selectedCategories": items
    })

  }

  handleSelectedCategoriesChange(items) {
    var selected_category_ids_names = [];
    items.forEach(function(category, index) {
      selected_category_ids_names.push({id: category.id, value: category.value, position: index});
    })
    this.setState({
      "selectedCategories": selected_category_ids_names
    });

  }

  handleSubmit(event) {

    Categories.updateOrder(this.state)
    .then((response) => {
      alert("Successful.")
      this.setState({
        selectedCategories: [],
        allCategories: [],
      })
    })
    .catch(err => {
      alert(err + " Error");
    })
  }

  render() {

  return(
    <div>
      <Link to="/category/reorder">
       <RaisedButton label="Done" secondary={true} style={styles.addBtn} onTouchTap={this.handleSubmit} />
      </Link>

      <h2 style={styles.title}>Categories</h2>
      <form className="form">
      <Search items={this.state.allCategories}
              placeholder='Pick categories'
              multiple={true}
              onItemsChanged={this.handleSelectedCategoriesChange}
              initialSelected={this.state.selectedCategories}
              autoCompleteVisibility={this.state.page} />

      </form>          
    </div>
  )}
}

export default CategoryReordering;

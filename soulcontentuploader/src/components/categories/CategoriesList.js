import React from 'react';
import { Link } from 'react-router-dom'
import ReactTable from 'react-table';
import sortBy from "lodash/sortBy";
import Categories from '../../services/categories'

import RaisedButton from 'material-ui/RaisedButton';
import ContentAdd from 'material-ui/svg-icons/content/add';

const styles = {
  addBtn: {
    margin: '20px 20px 0 0',
    float: 'right'
  },
  title: {
    padding: 20
  }
};

class CategoriesList extends React.Component{
  constructor(props) {
    super(props)
    this.state = {
      data: []
    }
  }

  componentDidMount() {
    Categories.getAll()
      .then((response) => {
        var categories = response.data;
        var sortedCategories = sortBy(categories, "modifiedDate").reverse();
        var items = [];
        sortedCategories.forEach(function(category,index) {
          items.push({
            category_en: category.name.en,
            publish_type: category.publishType,
            edit: <Link to={"/category/"+category.id}>Edit</Link>});
        })
        this.setState({
          data: items
        });
      })
      .catch((err) => {
        alert(err);
      })

      // console.log(this.state.data);
  }

  filterCaseInsensitive(filter, row) {
    const id = filter.pivotId || filter.id;
    return (
      row[id] !== undefined ?
        String(row[id].toLowerCase()).startsWith(filter.value.toLowerCase())
      :
        true
    );
  }

  render() {
    const columns = [{
      Header: 'Name',
      accessor: 'category_en', // String-based value accessors!
      width: 400
    },
    {
      Header: 'Publish type',
      accessor: 'publish_type', // String-based value accessors!
      width: 400
    },
    {
      Header: 'Edit Link',
      accessor: 'edit',
      width: 400
    }]

  return(
    <div>
      <Link to="/category/reorder">
       <RaisedButton label="Reorder" secondary={true} style={styles.addBtn} />
      </Link>
      
      <Link to="/category/add">
        <RaisedButton
          label="Add category"
          secondary={true}
          style={styles.addBtn}
          icon={<ContentAdd />}
        />
      </Link>
      <h2 style={styles.title}>Categories</h2>
      <ReactTable
        data={this.state.data}
        columns={columns}
        defaultPageSize={10}
        filterable={true}
        defaultFilterMethod={this.filterCaseInsensitive}
        className={"-striped -highlight"}/>
    </div>
  )}
}

export default CategoriesList;

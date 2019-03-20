import React from 'react';
import { Link } from 'react-router-dom'
import ReactTable from 'react-table';
import sortBy from "lodash/sortBy";
import RaisedButton from 'material-ui/RaisedButton';
import ContentAdd from 'material-ui/svg-icons/content/add';

import Poems from '../../services/poems'

const styles = {
  addBtn: {
    margin: '20px 20px 0 0',
    float: 'right'
  },
  title: {
    padding: 20
  }
};

class PoemsList extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      data: []
    }
  }

  componentDidMount() {
    Poems.getAll()
      .then((response) => {
        var poems = response.data;
        var sortedPoems = sortBy(poems, "modifiedDate").reverse();
        var items = [];
        sortedPoems.forEach(function(poem,index) {
          items.push({
            poem_en: poem.name.en,
            poem_type: poem.poemType,
            edit: <Link to={"/poem/"+poem.id}>Edit</Link>
          });
        })
        this.setState({
          data: items
        });
      })
      .catch(err => {
        alert(err);
      })
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
    const columns = [
    {
      Header: 'Speaker',
      accessor: 'poem_en',
      width: 400
    },
    {
      Header: 'Speaker Type',
      accessor: 'poem_type',
      width: 400
    },
    {
      Header: 'Edit Link',
      accessor: 'edit',
      width: 400
    }]

    let table;
    if (this.state && this.state.data) {
      table = <ReactTable
                data={this.state.data}
                columns={columns}
                defaultPageSize={50}
                filterable={true}
                defaultFilterMethod={this.filterCaseInsensitive}
                className={"-striped -highlight"}
                />
    }

  return(
    <div>
      <Link to="/poem/add">
        <RaisedButton
          label="Add Poem"
          secondary={true}
          style={styles.addBtn}
          icon={<ContentAdd />}
        />
      </Link>
      <h2 style={styles.title}>Poems</h2>
      {table}
      </div>
  )}
}

export default PoemsList;

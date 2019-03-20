import React from 'react';
import { Link } from 'react-router-dom'
import ReactTable from 'react-table';
import sortBy from "lodash/sortBy";
import RaisedButton from 'material-ui/RaisedButton';
import ContentAdd from 'material-ui/svg-icons/content/add';

import Speakers from '../../services/speakers'

const styles = {
  addBtn: {
    margin: '20px 20px 0 0',
    float: 'right'
  },
  title: {
    padding: 20
  }
};

class SpeakersList extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      data: []
    }
  }

  componentDidMount() {
    Speakers.getAll()
      .then((response) => {
        var speakers = response.data;
        var sortedSpeakers = sortBy(speakers, "modifiedDate").reverse();
        var items = [];
        sortedSpeakers.forEach(function(speaker,index) {
          items.push({
            speaker_en: speaker.name.en,
            speaker_type: speaker.speakerType,
            edit: <Link to={"/speaker/"+speaker.id}>Edit</Link>
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
      accessor: 'speaker_en',
      width: 400
    },
    {
      Header: 'Speaker Type',
      accessor: 'speaker_type',
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
        className={"-striped -highlight"}/>
    }

  return(
    <div>
      <Link to="/speaker/add">
        <RaisedButton
          label="Add Speaker"
          secondary={true}
          style={styles.addBtn}
          icon={<ContentAdd />}
        />
      </Link>
      <h2 style={styles.title}>Speakers</h2>
      {table}
      </div>
  )}
}

export default SpeakersList;

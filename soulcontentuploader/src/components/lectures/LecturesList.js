import React from 'react';
import { Link } from 'react-router-dom'
import ReactTable from 'react-table';
import sortBy from "lodash/sortBy";
import Lectures from '../../services/lectures'

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

class LecturesList extends React.Component {
  constructor(props) {
    super(props)
    this.state = { data: [],
    loading: true }
  }

  componentDidMount() {
    Lectures.getAll()
      .then((response) => {
        var lectures = response.data;
        var sortedLectures = sortBy(lectures, "modifiedDate").reverse();
        var items = [];
        sortedLectures.forEach(function(lecture,index) {
          var speaker_en;
          var speaker_type;
          var publish_type;
          if (typeof lecture.speaker !== "undefined"){
            speaker_en = lecture.speaker.name.en
            speaker_type = lecture.speaker.speakerType
            publish_type = lecture.publishType
          }
          items.push({
            lecture_en: lecture.name.en,
            speaker_en: speaker_en,
            speaker_type: speaker_type,
            publish_type: publish_type,
            edit: <Link to={"/lecture/"+lecture.id}>Edit</Link>
          });
        })
        this.setState({
          data: items,
          loading: false
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

    const columns = [{
      Header: 'Name',
      accessor: 'lecture_en', // String-based value accessors!
      width: 250
    },
    {
      Header: 'Speaker',
      accessor: 'speaker_en',
      width: 250
    },
    {
      Header: 'Speaker Type',
      accessor: 'speaker_type',
      width: 250
    },
    {
      Header: 'Publish Type',
      accessor: 'publish_type',
      width: 250
    },
    {
      Header: 'Edit Link',
      accessor: 'edit',
      width: 150
    }]

  let table;
  if (this.state && this.state.data) {
    table = <ReactTable
        data={this.state.data}
        columns={columns}
        defaultPageSize={50}
        loading={this.state.loading}
        loadingText="Loading..."
        filterable={true}
        defaultFilterMethod={this.filterCaseInsensitive}
        className={"-striped -highlight"}/>
  }

  return(
    <div>
      <Link to="/lecture/add">
        <RaisedButton
          label="Add lecture"
          secondary={true}
          style={styles.addBtn}
          icon={<ContentAdd />}
        />
      </Link>
      <h2 style={styles.title}>Lectures</h2>
      {table}
    </div>
  )}
}

export default LecturesList;

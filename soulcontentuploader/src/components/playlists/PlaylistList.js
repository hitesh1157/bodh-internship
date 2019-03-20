import React from 'react';
import { Link } from 'react-router-dom'
import ReactTable from 'react-table';
import sortBy from "lodash/sortBy";
import Playlists from '../../services/playlists'

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

class PlaylistList extends React.Component{
  constructor(props) {
    super(props)
    this.state = {
      data: []
    }
  }

  componentDidMount() {
    Playlists.getAll()
      .then((response) => {
        var playlists = response.data;
        var sortedPlaylists = sortBy(playlists, "modifiedDate").reverse();
        var items = [];
        sortedPlaylists.forEach(function(playlist,index) {
          var speaker_en;
          var speaker_type;
          var publish_type;
          if (typeof playlist.speaker !== "undefined"){
            speaker_en = playlist.speaker.name.en
            speaker_type = playlist.speaker.speakerType
            publish_type = playlist.publishType
          }
            items.push({
              playlist_en: playlist.name.en,
              speaker_en: speaker_en,
              speaker_type: speaker_type,
              publish_type: publish_type,
              edit: <Link to={"/playlist/"+playlist.id}>Edit</Link>});
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
    const columns = [{
      Header: 'Name',
      accessor: 'playlist_en', // String-based value accessors!
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

  return(
    <div>
      <Link to="/playlist/add">
        <RaisedButton
          label="Add playlist"
          secondary={true}
          style={styles.addBtn}
          icon={<ContentAdd />}
        />
      </Link>
      <h2 style={styles.title}>Playlists</h2>
      <ReactTable
        data={this.state.data}
        columns={columns}
        defaultPageSize={50}
        filterable={true}
        defaultFilterMethod={this.filterCaseInsensitive}
        className={"-striped -highlight"}/>
    </div>
  )}
}

export default PlaylistList;

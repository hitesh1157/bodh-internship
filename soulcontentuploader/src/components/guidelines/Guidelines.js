import React, { Component } from 'react';

import './guidelines.css'

class Guidelines extends Component {
  render() {
    return (
      <div >
      <br />
        <h3>Content Guidelines :</h3>
        <ul className='bullets'>
          <li> Any non-relevant content like announcement, music, crowd voice, etc should be trimmed before uploading any Lecture.</li>
          <li> Any content promoting hatred/controversy should be trimmed.</li>
          <li> Any Playlist with one or more missing lectures should not be published.</li>
          <li> Proper names should be given to any content that is uploaded.</li>
        </ul>
        <br />
        <br />
        <h3>Nomenclature:</h3>
        <ul className='bullets'>
          <li> Category Name -> {'{Category name}'}</li>
          <li> Playlist Name -> {'{Category name}'} - {'{Speaker name}'}</li>
          <li> Lecture Name -> {'{Category name}'} - {'{Lecture name}'}</li>
        </ul>
      </div>
    )
  }
}

export default Guidelines;

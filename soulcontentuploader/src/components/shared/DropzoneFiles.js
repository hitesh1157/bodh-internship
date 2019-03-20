import React, { Component } from 'react';
import Dropzone from "react-dropzone";

class DropzoneFiles extends Component {
  onDrop(acceptedFiles, rejectedFiles) {
    this.props.onChange(acceptedFiles);
  }

  render() {
    var status = ""
        if (this.props.uploadStatus==="2"){
          status = this.props.uploadFileName || <img src={this.props.uploadUrl} alt="Uploaded" width="100" height="100"/>
        }
        if (this.props.uploadStatus==="1"){
          status =  this.props.uploadFileName || "Uploading..."
        }
        if (this.props.uploadStatus==="0"){
          status = "Please drop a file"
        }
    return (
      <div >
        <Dropzone multiple={false} disablePreview={false} style={{}} accept={this.props.fileTypes} onDrop={this.onDrop.bind(this)}>
        {({ isDragActive, isDragReject }) => {
          let styles = {
            width: '100%',
            height: 150,
            textAlign: 'center',
            borderWidth: 2,
            borderColor: '#666',
            borderStyle: 'dashed',
            borderRadius: 5
          };

          if (isDragActive) {
            styles = {
              ...styles,
              borderStyle: 'solid',
              borderColor: '#6c6',
              backgroundColor: '#666'
            };
          }
          if (isDragReject) {
            styles = {
              ...styles,
              borderStyle: 'solid',
              borderColor: '#c66',
              backgroundColor: '#666'
            };
          }

          return (<div style={styles}>{status}</div>)
        }}
        </Dropzone>
      </div>
      );
  }
}

export default DropzoneFiles;

import React, { Component } from 'react';
// import Select from 'react-select';
import SelectField from 'material-ui/SelectField'
import MenuItem from 'material-ui/MenuItem'
import * as constants from '../../config/Constants';

class SelectSect extends Component{
  constructor(props) {
    super(props)
    this.state = {
      data: null
    }
  }

  componentDidMount() {
    var sects = constants.SECTS;
    var items = [];
    sects.forEach(function(sect,index) {
      items.push(<MenuItem value={sect} key={index} primaryText={ sect } />);
    })
    this.setState({
      data: items
    });
  }

  render() {
    let select;
    let textStyle = {
      width: '100%'
    }
    if (this.state && this.state.data) {

      select = <SelectField style={textStyle}
            floatingLabelText={this.props.name}
            value={this.props.value}
            onChange={this.props.onChange}
            required
            >{this.state.data}</SelectField>
    }
    return (
      <div>
      {select}
      </div>
     )}
  }

export default SelectSect;

import React, { Component } from "react";
import { Route, Link, withRouter } from 'react-router-dom'
import Drawer from "material-ui/Drawer";
import Divider from 'material-ui/Divider';
import MenuItem from "material-ui/MenuItem";
import ActionExitToApp from 'material-ui/svg-icons/action/exit-to-app';
import ActionRecordVoiceOver from 'material-ui/svg-icons/action/record-voice-over';
import AvMic from 'material-ui/svg-icons/av/mic';
import AvPlaylistPlay from 'material-ui/svg-icons/av/playlist-play';
import AvLibraryBooks from 'material-ui/svg-icons/av/library-books';
import ActionDescription from 'material-ui/svg-icons/action/description';
import AvLibraryMusic from 'material-ui/svg-icons/av/library-music';

import {grey600} from 'material-ui/styles/colors';
import './sidebar.css';

import Account from '../../services/account'

const style = {
  paper: {
    'textAlign': 'left'
  },
};

const LogOutBtn = withRouter(({ history }) => (
  <div className="logoff" title="Log out" onTouchTap={() => {
    Account.logout().then(() => history.push('/'))
  }}>
    <ActionExitToApp color={grey600}/>
  </div>
))

const MenuLink = ({ component, to, activeOnlyWhenExact }) => (
  <Route path={to} exact={activeOnlyWhenExact} children={({ match }) => (
    <div className={match ? 'active' : ''}>
      <Link to={to}>{component}</Link>
    </div>
  )}/>
)

class Sidebar extends Component {
  constructor(props) {
    super(props);
    this.state = {
      drawerOpen: this.props.open,
      userInfo: this.props.userData.info,
      userRoles: this.props.userData.roles
    };
  }

  updateDimensions() {
    this.setState({
      drawerOpen: document.body.clientWidth > 768
    });
  }

  handleToggle = () =>
    this.setState({
      drawerOpen: !this.state.drawerOpen
    });

  render() {
    return (
      <Drawer open={this.state.drawerOpen} className="drawer">
        <div className="header">
            <LogOutBtn />
            <div className="profile-pic no-pic">{this.state.userInfo.name.substring(0,1)}</div>
            <div className="name">{this.state.userInfo.name}</div>
            <div className="email">{this.state.userInfo.email}</div>
        </div>
        <Divider />
        <MenuLink to="/speaker" component={<MenuItem style={style.paper} primaryText="Speaker" leftIcon={<ActionRecordVoiceOver />} />} />
        <MenuLink to="/lecture" component={<MenuItem style={style.paper} primaryText="Lecture" leftIcon={<AvMic />} />} />
        <MenuLink to="/playlist" component={<MenuItem style={style.paper} primaryText="Playlist" leftIcon={<AvPlaylistPlay />} />} />
        <MenuLink to="/category" component={<MenuItem style={style.paper} primaryText="Category" leftIcon={<AvLibraryBooks />} />} />
        <MenuLink to="/poem" component={<MenuItem style={style.paper} primaryText="Poem" leftIcon={<AvLibraryMusic />} />} />
        <div className="footer">
          <MenuLink to="/guidelines" component={<MenuItem style={style.paper} primaryText="Guidlines" leftIcon={<ActionDescription />} />} />
        </div>
      </Drawer>

    );
  }
}

export default Sidebar;

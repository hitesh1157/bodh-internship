import React, {Component} from "react";
import { Route, Switch } from 'react-router-dom'
import AppBar from "material-ui/AppBar";
import ImageFilterTiltShift from 'material-ui/svg-icons/image/filter-tilt-shift';

import "./Seva.css";
import Sidebar from './sidebar'
import Account from '../services/account'
import LecturesHome from './lectures/'
import SpeakersHome from './speakers/'
import PlaylistsHome from './playlists/'
import CategoriesHome from './categories/'
import PoemsHome from './poems/'
import GuidelinesHome from './guidelines/'

const Home = () => <h3>Home</h3>

const loadingStyle = {
  height: 96,
  width: 96
}

class Seva extends Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: true,
      drawerOpen: document.body.clientWidth > 768,
      env: window.location.port
        ? "development"
        : "production"
    };
    console.log(`This app is running in ${process.env.NODE_ENV}, using ${process.env.REACT_APP_API_ENV} api.`);

  }

  updateDimensions() {
    if (this.refs.SevaApp) {
      this.setState({
        drawerOpen: document.body.clientWidth > 768
      });
      this.sidebar.updateDimensions();
    }
  }

  componentWillMount() {
    Account.onAuthStateChanged((user, err) => {
      if (user) {
        Promise.all([Account.getUserInfo(), Account.user.getIdToken()]).then(([info, token]) => {
          Account.token = token;
          this.handleLoaded(info);
          console.log('user info loaded');
        })
      }
    });
  }

  componentDidMount() {
    this.updateDimensions();
    window.addEventListener("resize", this.updateDimensions.bind(this));
  }

  componentWillUnmount() {
    window.removeEventListener("resize", this.updateDimensions.bind(this));
  }

  handleLoaded(userInfo) {
    this.setState({loading: false,  userData: userInfo});
  }

  handleToggle = () => {
    this.setState({
      drawerOpen: !this.state.drawerOpen
    });
    this.sidebar.handleToggle();
  }

  render() {
    const contentStyle = {
      transition: "margin-left 450ms cubic-bezier(0.23, 1, 0.32, 1)"
    };

    if (this.state.drawerOpen) {
      contentStyle.marginLeft = 256;
      if (document.body.clientWidth < 500){
        contentStyle.marginRight = -256;
      }
    }

    if (this.state.loading) {
      return (
        <div className="app-loading-icon">
            <ImageFilterTiltShift style={loadingStyle} />
          </div>
      )
    }

    let footer;
    if (this.state.env === 'development')
      footer = <div className="footer fix">Running in {this.state.env} mode.</div>

    return (
      <div className="App" ref="SevaApp">
        <Sidebar ref={instance => { this.sidebar = instance; }} open={this.state.drawerOpen} userData={this.state.userData} />
        <div style={contentStyle}>
          <AppBar title="Bodh - Jain Uploader" onLeftIconButtonTouchTap={this.handleToggle} />
          <div className="main-container">
            <Switch>
              <Route path="/home" component={Home} />
              <Route path="/speaker" component={SpeakersHome} />
              <Route path="/lecture" component={LecturesHome} />
              <Route path="/playlist" component={PlaylistsHome} />
              <Route path="/category" component={CategoriesHome} />
              <Route path="/poem" component={PoemsHome} />
              <Route path="/guidelines" component={GuidelinesHome} />
            </Switch>
          </div>
          {footer}
        </div>
      </div>
    );
  }
}

export default Seva;

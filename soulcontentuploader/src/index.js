import React from 'react';
import ReactDOM from 'react-dom';
import injectTapEventPlugin from 'react-tap-event-plugin';
import { BrowserRouter as Router, Route, Switch, Redirect } from 'react-router-dom'

import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import 'react-table/react-table.css'
import './index.css';
import Account from './services/account'
import { Seva, Login } from './components';
import unregisterServiceWorker from './registerServiceWorker';

// Needed for onTouchTap
// http://stackoverflow.com/a/34015469/988941
injectTapEventPlugin();

const Root = () => {
  return (
    <MuiThemeProvider>
      <Seva />
    </MuiThemeProvider>
  );
}

const PublicRoute = ({ component: Component, ...rest }) => (
  <Route {...rest} render={props => (
    !Account.loggedIn() ? (
      <Component {...props}/>
    ) : (
      <Redirect to={{
        pathname: '/',
        state: { from: props.location }
      }}/>
    )
  )}/>
)

const PrivateRoute = ({ component: Component, isPrivate, ...rest }) => (
  <Route {...rest} render={props => (
    Account.loggedIn() ? (
      <Component {...props} />
    ) : (
      <Redirect to={{
        pathname: '/login',
        state: { from: props.location }
      }}/>
    )
  )}/>
)


ReactDOM.render(
  <Router>
    <Switch>
      <PublicRoute path="/login" component={Login}/>
      <PrivateRoute path="/" component={Root} isPrivate={true} />
    </Switch>
  </Router>, document.getElementById('root'));
// registerServiceWorker();
unregisterServiceWorker();

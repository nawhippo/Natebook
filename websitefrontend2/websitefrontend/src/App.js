import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import About from './pages/about/About';
import { UserProvider } from './pages/login/UserContext';
import { useUserContext } from './pages/login/UserContext';
import Login from './pages/login/Login';
import Home from './pages/home/Home';
// Move currentUserData outside of the component class

class App extends Component {
  render() {
    return (
      <UserProvider>
        <Router>
          <div className="App">
            <header className="App-header">
              <Switch>
                <Route exact path="/" render={() => (
                  <div>
                    <div className="App-intro">
                      Select an endpoint
                    </div>
                  </div>
                )} />
                <Route path="/about" component={About} />
                <Route path="/home" component={Home} />
                <Route path="/login" component={Login}/>   
              </Switch>
            </header>
          </div>
        </Router>
      </UserProvider>
    );

  }
}

export default () => (
  <UserProvider>
    <App />
  </UserProvider>
);
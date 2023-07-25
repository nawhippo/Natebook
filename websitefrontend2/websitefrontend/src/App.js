import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import About from './pages/about/About';
import Home from './pages/home/Home';
import { UserProvider } from './pages/login/UserProvider';
import UserControllerComp from './pages/users/UserRoutes';
class App extends Component {
  const currentUserData = {
    userId: 123,
    firstname: "John",
    lastname: "Doe",
    email: "john@example.com",
    username: "johndoe",
    // Other user data...
  };
  render() {
    return (
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
              {/* User Controllers */}
              <UserControllerComp/>
            </Switch>
          </header
        </div>
      </Router>
    );
  }
}

export default App;
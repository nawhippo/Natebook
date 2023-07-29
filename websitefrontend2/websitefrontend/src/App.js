import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Switch, Redirect } from 'react-router-dom';
import About from './pages/about/About';
import { UserProvider } from './pages/login/UserContext';
import { useUserContext } from './pages/login/UserContext';
import Login from './pages/login/Login';
import Home from './pages/home/Home';
import createAccount from './pages/account/createAccount';
import getAllPosts from './pages/posts/allposts/getAllPosts';
import specPost from './pages/posts/specpost/specPost';
import specFriend from './pages/friends/specfriend/specFriend';
import getAllFriends from './pages/friends/allfriends/getAllFriends';
import createMessage from './pages/message/createMessage/createMessage';
import getAllMessages from './pages/message/getAllMessages/getAllMessages';
import createPost from './pages/posts/createPost/createPost'
import ProtectedRoute from './pages/login/ProtectedRoute';
class App extends Component {
  render() {
    return (
      <Router>
        <div className="App">
          <header className="App-header">
            <Switch>
              {/* Routes that don't require authentication */}
              <Route path="/about" component={About} />
              <Route path="/createAccount" component={createAccount} />
              <Route path="/login" component={Login}/>
              <Route path="/home" exact component={Home}/>
              {/* Routes that require authentication */}
              <UserProvider>

              <ProtectedRoute path="/getAllFriends" component={getAllFriends} />
              <ProtectedRoute path="/specFriend" component={specFriend} />
              <ProtectedRoute path="/createMessage" component={createMessage} />
              <ProtectedRoute path="/getAllMessages" component={getAllMessages}
              />
              <ProtectedRoute path="/specPost" component={specPost} />
              <ProtectedRoute path="/getAllPosts" component={getAllPosts} />
              <ProtectedRoute path="/createPost" component={createPost} />
              </UserProvider> */
            </Switch>
          </header>
        </div>
      </Router>
    );
  }
}

export default App;
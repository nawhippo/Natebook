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

class App extends Component {
  render() {
    return (
      <Router>
        <div className="App">
          <header className="App-header">
            <Switch>
              {/* Routes that don't require authentication */}
              <Route path="/" component={Home} />
              <Route path="/about" component={About} />
              <Route path="/createAccount" component={createAccount} />
              <Route path="/login" component={Login}/>
              {/* Routes that require authentication */}
              <UserProvider>
                <Route path="/getAllFriends" component={getAllFriends}/>
                <Route path="/specFriend" component={specFriend}/>
                <Route path="/createMessage" component={createMessage}/>
                <Route path="/getAllMessages" component={getAllMessages}/>
                <Route path="/specPost" component={specPost}/>
                <Route path="/getAllPosts" component={getAllPosts}/>
                <Route path="/createPost" component={createPost}/>
              </UserProvider>
            </Switch>
          </header>
        </div>
      </Router>
    );
  }
}

export default App;
import React, { Component, useEffect, useState } from 'react';
import { BrowserRouter as Router, Route, Switch, Redirect } from 'react-router-dom';
import About from './pages/about/About';
import { UserProvider } from './pages/login/UserContext';
import { useUserContext } from './pages/login/UserContext';
import Login from './pages/login/Login';
import Home from './pages/home/Home';
import createAccount from './pages/account/createAccount';
import getAllPostByUsername from './pages/posts/allposts/getAllPostsByUsername';
import specPost from './pages/posts/specpost/specPost';
import specFriend from './pages/friends/specfriend/specFriend';
import GetAllFriends from './pages/friends/allfriends/getAllFriends';
import createMessage from './pages/message/createMessage/createMessage';
import getAllMessages from './pages/message/getAllMessages/getAllMessages';
import createPost from './pages/posts/createPost/createPost';
import ProtectedRoute from './pages/login/ProtectedRoute';
import Banner from './banners/banner';
import './universal.css';
import SendFriendRequestByUsername from './pages/friends/friendrequests/SendFriendRequestByUsername';
import GetAllFriendRequests from './pages/friends/friendrequests/ViewAllFriendRequests';
import Logout from './pages/login/Logout';
import GetAccount from './pages/account/getAccount';
import GetAllMessages from './pages/message/getAllMessages/getAllMessages';
class App extends Component {
  render() {
    return (
      <UserProvider>
        <Router>
          <div className="App">
            <Banner />
            <header className="App-header">
              <Switch>
                {/* Routes that don't require authentication */}
                <Route path="/about" component={About} />
                <Route path="/createAccount" component={createAccount} />

                {/* Routes that require authentication */}
                <PrivateRoute path="/home" exact component={Home} />
                <PrivateRoute path ="/accountDetails" exact component={GetAccount} />
                <PrivateRoute path="/getAllFriends" component={GetAllFriends} />
                <PrivateRoute path="/specFriend/:userId" component={specFriend} /> 
                <PrivateRoute path="/createMessage" component={createMessage} />
                <PrivateRoute path="/login" component={Login} />
                <PrivateRoute path="/specPost/:userId" component={specPost} /> 
                <PrivateRoute path="/getAllPosts" component={getAllPostByUsername} />
                <PrivateRoute path="/createPost" component={createPost} />
                <PrivateRoute path="/sendFriendRequestByUsername" component={SendFriendRequestByUsername} />
                <PrivateRoute path="/getFriendRequests" component={GetAllFriendRequests} />
                <PrivateRoute path="/SendFriendRequest" component={SendFriendRequestByUsername} />
                <PrivateRoute path="/logout" component={Logout} />
                <PrivateRoute path="/getAllMessages" component={GetAllMessages} /> 

                 {/* Fallback route */}
                 <Route path="*" component={() => <Redirect to="/login" />} />
              </Switch>    
            </header>
          </div>
        </Router>
      </UserProvider>
    );
  }
}

class PrivateRoute extends Component {
  render() {
    const { component: Component, ...rest } = this.props;
    const user = user.appUserID;

    if (!user) {
      return <Redirect to="/login" />;
    }

    return <Route {...rest} render={props => <Component {...props} />} />;
}
}
export default App;
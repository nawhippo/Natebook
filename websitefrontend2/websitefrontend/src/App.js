import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Switch, useLocation } from 'react-router-dom';
import { UserProvider } from './pages/usercontext/UserContext';
import SnowFlake from './FunSFX/snowflake';
import FeedPage from './pages/feedPage/feedPage';
import AccountPage from './pages/accountPage/accountPage';
import FriendsPage from "./pages/friendsPage/friendsPage";
import AllUserPage from './pages/allUsersPage/allUsersPage';
import MessagesPage from './pages/messagePage/MessagesPage';
import ProfilePage from './pages/profilePage/profilePage';
import LoginPage from './pages/loginPage/loginPage';
import Banner from "./banners/websiteBanner"
import "./FunSFX/snowflake.css"
import "./App.css"
import SessionExpiredNotification from "./utility/SessionExpiredNotification";
function App() {
  const [backgroundClass, setBackgroundClass] = useState('');
  const location = useLocation();

  useEffect(() => {
    changeBackgroundPattern();
  }, [location]);

  const changeBackgroundPattern = () => {
    const patterns = ['polkadots', 'slantylines', 'diagonalStripes', 'checkerboard', 'zigzag', 'dottedGrid', 'houndstooth', 'lattice'];
    const selectedPattern = patterns[Math.floor(Math.random() * patterns.length)];
    setBackgroundClass(selectedPattern);
  };

  const snowflakes = Array.from({ length: 50 }).map((_, index) => (
      <SnowFlake key={index} />
  ));

  return (
      <UserProvider>
        <div className={`app ${backgroundClass}`}>
          <SessionExpiredNotification />
          <Router>
            <div className="App">
              <div className="snowflake-container">{snowflakes}</div>
              <Banner />
              <header className="App-header">
                <Switch>
                  <Route path="/Account"  exact component={AccountPage} />
                  <Route path="/Feed" exact component={FeedPage} />
                  <Route path="/AllUsersPage" exact component={AllUserPage} />
                  <Route path="/Friends" component={FriendsPage} />
                  <Route path="/UserProfile/:userid" component={ProfilePage}/>
                  <Route path="/Messages" component={MessagesPage}/>
                  <Route path="/Login" component={LoginPage}/>
                </Switch>
              </header>
            </div>
          </Router>
        </div>
      </UserProvider>
  );
}

export default App;
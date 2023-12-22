import React, {useEffect, useState} from 'react';
import SearchIcon from '@mui/icons-material/Search';
import './allUsersPage.css';
import ProfilePictureComponent from "../../buttonComponents/ProfilePictureComponent";
import {useUserContext} from "../usercontext/UserContext";

const AllUsersPage = () => {
  const [addressBookData, setAddressBookData] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [isLoggedIn, setIsLoggedIn] = useState(false); // Add isLoggedIn state
  const { user } = useUserContext(); // Get user from the user context
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch('/api/user/getAllWebsiteUsers');
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        const data = await response.json();
        setAddressBookData(data);
        setIsLoading(false);
      } catch (error) {
        setError(error.message);
        setIsLoading(false);
      }
    };

    fetchData();
  }, []);

  const filteredUsers = searchTerm
      ? addressBookData.filter(userItem =>
          userItem.username.toLowerCase().includes(searchTerm.toLowerCase())
      )
      : addressBookData;

  const handleSearch = (e) => {
    e.preventDefault();
  };

  const buttonStyle = {
    backgroundColor: user && user.backgroundColor ? user.backgroundColor : 'grey',
    color: '#FFFFFF',
  };

  return (
      <div>
        <p>{addressBookData && addressBookData.message}</p>
        <h2>Users</h2>
        <form onSubmit={handleSearch} className="search-bar-container">
          <input
              className="search-input"
              type="text"
              placeholder="Search by username"
              value={searchTerm}
              onChange={e => setSearchTerm(e.target.value)}
          />
          <button className="search-button" type="submit" style={buttonStyle}>
            <SearchIcon />
          </button>
        </form>
        {isLoading ? (
            <div>Loading...</div>
        ) : error ? (
            <div>Error: {error}</div>
        ) : (
            <ul>
              {filteredUsers && filteredUsers.length > 0 ? (
                  filteredUsers.map(userItem => (
                      <li key={userItem.appUserID} className="user-item">
                <span className="user-name">
                  @{userItem.username} - {userItem.firstname} {userItem.lastname}
                </span>

                        <div className="button-group">
                          <ProfilePictureComponent userid={userItem.appUserID} style={{fontSize: '30px !important'}} />
                        </div>
                      </li>
                  ))
              ) : (
                  <p>No website users found.</p>
              )}
            </ul>
        )}
      </div>
  );
};

export default AllUsersPage;
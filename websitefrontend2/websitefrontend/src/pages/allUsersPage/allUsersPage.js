import React, { useState, useEffect } from 'react';
import { useUserContext } from '../usercontext/UserContext';
import SendFriendRequestButton from '../../buttonComponents/sendFriendRequestButton/sendFriendRequestButton';
import ViewProfileButton from '../../buttonComponents/viewProfileButton/viewProfileButton';
import SearchIcon from '@mui/icons-material/Search'; // Import the search icon
import './allUsersPage.css'
const AllUsersPage = () => {
  const [addressBookData, setAddressBookData] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const { user } = useUserContext();

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

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  return (
      <div>
        <p>{addressBookData && addressBookData.message}</p>
        {user && (
            <>
              <h2>Users</h2>
              <form onSubmit={handleSearch} className="search-bar-container">
                <input
                    className="search-input"
                    type="text"
                    placeholder="Search by username"
                    value={searchTerm}
                    onChange={e => setSearchTerm(e.target.value)}
                />
                <button className="search-button" type="submit">
                  <SearchIcon />
                </button>
              </form>
              {filteredUsers && filteredUsers.length > 0 ? (
                  <ul>
                    {filteredUsers.map(userItem => (
                        <li key={userItem.appUserID} className="user-item">
        <span className="user-name">
            {userItem.username} - {userItem.firstname} {userItem.lastname}
        </span>
                          <div className="button-group">
                            <ViewProfileButton userid={userItem.appUserID} />
                            {user.username !== userItem.username && (
                                <SendFriendRequestButton username={userItem.username} />
                            )}
                          </div>
                        </li>
                    ))}
                  </ul>
              ) : (
                  <p>No website users found.</p>
              )}
            </>
        )}
      </div>
  );
};

export default AllUsersPage;
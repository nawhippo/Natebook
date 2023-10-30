import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useUserContext } from '../usercontext/UserContext';
import SendFriendRequestButton from '../../buttonComponents/sendFriendRequestButton/sendFriendRequestButton';
import ViewProfileButton from '../../buttonComponents/viewProfileButton/viewProfileButton';

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
  }, [addressBookData]);

  const filteredUsers = searchTerm
    ? addressBookData.filter(user =>
        user.username.toLowerCase().includes(searchTerm.toLowerCase())
      )
    : addressBookData;

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
          <h2>Website Users</h2>
          <input
            type="text"
            placeholder="Search by username"
            value={searchTerm}
            onChange={e => setSearchTerm(e.target.value)}
          />
          {filteredUsers && filteredUsers.length > 0 ? (
            <ul>
              {filteredUsers.map(userItem => ( 
                <li key={userItem.appUserID}>
                  {userItem.username} - {userItem.firstname} {userItem.lastname}
                  <ViewProfileButton userid={userItem.appUserID} /> 
                  {user.username !== userItem.username && (
                    <SendFriendRequestButton username={userItem.username} />
                  )}
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
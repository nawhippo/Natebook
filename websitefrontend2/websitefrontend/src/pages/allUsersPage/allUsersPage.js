import React, { useEffect, useState } from 'react';
import SearchIcon from '@mui/icons-material/Search';
import './allUsersPage.css';
import ProfilePictureComponent from "../../buttonComponents/ProfilePictureComponent";
import { useUserContext } from "../usercontext/UserContext";
import { fetchWithJWT } from '../../utility/fetchInterceptor';
import UserStatus from "../../buttonComponents/getStatus/getStatus";
import {getRandomColor} from "../../FunSFX/randomColorGenerator";

const AllUsersPage = () => {
  const [addressBookData, setAddressBookData] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const { user } = useUserContext();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetchWithJWT('/api/user/getAllWebsiteUsers');
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
          userItem.username.toLowerCase().includes(searchTerm.toLowerCase().trim()) ||
          userItem.firstname.toLowerCase().includes(searchTerm.toLowerCase().trim()) ||
          userItem.lastname.toLowerCase().includes(searchTerm.toLowerCase().trim())
      )
      : addressBookData;

  const handleInputChange = (event) => setSearchTerm(event.target.value);

  const buttonStyle = {
    backgroundColor: user?.backgroundColor || getRandomColor(),
    color: '#FFFFFF',
  };

  return (
      <div style={{marginRight: "30px", marginLeft: "30px"}}>
        <p>{addressBookData && addressBookData.message}</p>
        <h2 style={{textAlign: 'center', fontSize: '30px'}}>Users</h2>
        <div className="search-bar-container">
          <input
              className="search-input"
              type="text"
              value={searchTerm}
              placeholder="Search users..."
              onChange={handleInputChange}
          />
          <button className="search-button" type="submit" onClick={() => setSearchTerm(searchTerm)} style={buttonStyle}>
            <SearchIcon />
          </button>
        </div>
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
                        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                          <div style={{fontSize:"30px"}}>
                            @{userItem.username} - {userItem.firstname} {userItem.lastname}
                            <UserStatus appUserId={userItem.appUserID}/>
                          </div>
                        </div>
                      </span>
                        <div className="button-group">
                          <ProfilePictureComponent userid={userItem.appUserID} style={{ width: '150px', height: '150px' }} />
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
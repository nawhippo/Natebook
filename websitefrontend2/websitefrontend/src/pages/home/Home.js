import React, { useState, useEffect } from 'react';
import { Link, useHistory } from 'react-router-dom';
import { useUserContext } from '../usercontext/UserContext';
const Home = () => {
  const [homeData, setHomeData] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState(''); 
  const history = useHistory();
  const { user } = useUserContext();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch('/api/home');
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        const data = await response.json();
        setHomeData(data);
        setIsLoading(false);
      } catch (error) {
        setError(error.message);
        setIsLoading(false);
      }
    };

    fetchData();
  }, []);



  const handleViewProfileClick = (username) =>{
    console.log(username);
    history.push(`/userProfile/${username}`);
  }


  const filteredUsers = searchTerm
    ? homeData.filter(user =>
        user.username.toLowerCase().includes(searchTerm.toLowerCase())
      )
    : homeData;

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  return (
    <div>
      <h1>Home Page</h1>
      {user && <p>Logged in as: {user.username}</p>}
      <p>{homeData && homeData.message}</p>
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
              {filteredUsers.map(user => ( 
                <li key={user.appUserID}>
                  {user.username} - {user.firstname} {user.lastname}
                  <button onClick={() => { handleViewProfileClick(user.username); }}>View Profile</button>
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

export default Home;
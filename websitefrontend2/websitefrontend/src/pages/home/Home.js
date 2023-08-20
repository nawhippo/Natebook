import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useUserContext } from '../login/UserContext';

const Home = () => {
  const [homeData, setHomeData] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

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
          <Link to={`/account/${user.appUserID}/accountDetails`}>View Your Profile</Link>
          <h2>Website Users</h2>
          {homeData && homeData.length > 0 ? (
            <ul>
              {homeData.map(user => (
                <li key={user.appUserID}>
                  {user.username} - {user.firstname} {user.lastname}
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
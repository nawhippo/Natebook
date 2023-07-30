import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useUserContext } from '../login/UserContext';

const Home = () => {
  const [homeData, setHomeData] = useState(null);
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
      <h2>TEST TEST</h2>
      <h1>Home Page</h1>
      {/* Display the username if logged in, otherwise display nothing */}
      {user && <p>Logged in as: {user.username}</p>}
      <p>{homeData && homeData.message}</p>

      {/* Add links to the specific endpoints with the user ID */}
      {user && (
        <>
          <Link to={`/getAllFriends`}>View Friend</Link>
          <br />
          <Link to={`/getAllPosts`}>View All of Your Posts</Link>
          <br />
          <Link to={`/getAllMessages`}>View Messages</Link>
        </>
      )}
    </div>
  );
};

export default Home;
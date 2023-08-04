import React, { useState, useEffect } from 'react';
import { useUserContext } from '../../login/UserContext';
const GetAllPosts = ({ targetUsername }) => { // Use targetUsername instead of userid
  const { user } = useUserContext(); // Access the user object from the context
  const [allPostsData, setAllPostsData] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch(`/api/${user.userId}/postsByUsername/${targetUsername}`); 
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        const data = await response.json();
        setAllPostsData(data);
        setIsLoading(false);
      } catch (error) {
        setError(error.message);
        setIsLoading(false);
      }
    };

    fetchData();
  }, [user.userId, targetUsername]); 

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  return (
    <div>
      <h1>All Posts</h1>

      {allPostsData && allPostsData.length > 0 ? (
        allPostsData.map((post) => (
          <div key={post.id} className="post-card">
            <h2>{post.title}</h2>
            <p>{post.description}</p>
            <p>{post.likes}</p>
            <p>{post.dislikes}</p>
            <p>{post.email}</p>
            <p>{post.dateTime}</p>
          </div>
        ))
      ) : (
        <p>No posts found.</p>
      )}
    </div>
  );
};

export default GetAllPosts;
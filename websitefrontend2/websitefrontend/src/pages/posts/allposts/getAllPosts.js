import React, { useState, useEffect } from 'react';

const GetAllPosts = ({ userid }) => { // Changed parameter name to lowercase "userid"
  const [allPostsData, setAllPostsData] = useState(null); // Renamed "allPostsData"

  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch(`/${userid}/posts`); // Use "userid" here
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        const data = await response.json();
        setAllPostsData(data); // Set the correct state variable here
        setIsLoading(false);
      } catch (error) {
        setError(error.message);
        setIsLoading(false);
      }
    };

    fetchData();
  }, [userid]); // Use "userid" in the dependency array

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
        allPostsData.map((post) => ( // Use "allPostsData" here
          <div key={post.id} className="post-card">
            <h2>{post.title}</h2>
            <p>{post.description}</p>
            <p>{post.likes}</p>
            <p>{post.dislikes}</p>
            <p>{post.email}</p>
          </div>
        ))
      ) : (
        <p>No posts found.</p> 
      )}
    </div>
  );
};

export default GetAllPosts;
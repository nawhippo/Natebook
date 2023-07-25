
import React, { useState, useEffect } from 'react';
//user id is a prop passed to this function
const AllPosts = ( {userid} ) => {
  const [allPostsData, setallPostsData] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        //dollar sign cause variable data.
        const response = await fetch(`/api/${userid}/posts`);
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        const data = await response.json();
        setallPostsData(data);
        setIsLoading(false);
      } catch (error) {
        setError(error.message);
        setIsLoading(false);
      }
    };

    fetchData();
  }, [userId]);

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
        aboutData.map((post) => (
                <div key={post.id} className="post-card">
                <h2>{post.title}</h2>
                <p>{post.description}</p>
                <p>{post.likes}</p>
                <p>{post.dislikes}</p>
                <p>{friend.email}</p>
                </div>
        ))
      ) : (
      <p>No friends found.</p>
      )}
      </div>
  );
      }
export default AllPosts;
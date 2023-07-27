import React, { useState, useEffect } from 'react';

const specPost = ({ userId, postId }) => {
  const [post, setPost] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchPost = async () => {
      try {
        const response = await fetch(`/api/${userId}/${postId}`);
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        const postData = await response.json();
        setPost(postData);
        setIsLoading(false);
      } catch (error) {
        setError(error.message);
        setIsLoading(false);
      }
    };

    fetchPost();
  }, [userId, postId]);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  if (!post) {
    return <div>No post found.</div>;
  }

  return (
    <div>
      <h1>Post Details</h1>
      <div className="post-card">
        <h2>{post.title}</h2>
        <p>{post.description}</p>
        <p>{post.likes}</p>
        <p>{post.dislikes}</p>+
      </div>
    </div>
  );
};

export default specPost;
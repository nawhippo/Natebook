import React, { useState, useEffect } from 'react';
import { useUserContext } from '../../login/UserContext';
import '../posts.css'
const GetAllPostsByUsername = () => {
  const { user } = useUserContext();
  const [targetUsername, setTargetUsername] = useState('');
  const [allPostsData, setAllPostsData] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [inputValue, setInputValue] = useState('');

  const fetchData = (username) => {
    setIsLoading(true);
    fetch(`/api/post/${user.appUserID}/postsByUsername/${username}`)
      .then((response) => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then((data) => {
        setAllPostsData(data);
        setIsLoading(false);
      })
      .catch((error) => {
        setError(error.message);
        setIsLoading(false);
      });
  };


  const handleInputChange = (event) => {
    const value = event.target.value;
    setInputValue(value);
    console.log('Input Value: ', value);
  };

  const handleSearchClick = () => {
    setTargetUsername(inputValue);
    fetchData(inputValue);
  };

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  return (
    <div>
      <h1>All Posts</h1>
      <input
        type="text"
        value={inputValue}
        placeholder="Enter username"
        onChange={handleInputChange}
      />
      <button onClick={handleSearchClick}>Search</button>
      {allPostsData && allPostsData.length > 0 ? (
        allPostsData.map((post) => (
          <div key={post.id} className="post-card">
            <h2>{post.title}</h2>
            <p>{post.description}</p>
            <p>Likes : {post.likes} Dislikes: {post.dislikes}</p>
            <p>{post.email}</p>
            <p>{post.dateTime}</p>
            <p>{post.appUser.username}</p>
          </div>
        ))
      ) : (
        <p>No posts found.</p>
      )}
    </div>
  );
};

export default GetAllPostsByUsername;
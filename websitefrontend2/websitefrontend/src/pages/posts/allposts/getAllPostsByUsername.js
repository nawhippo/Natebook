import React, { useState, useEffect } from 'react';
import { useUserContext } from '../../usercontext/UserContext';
import Post from '../post';

const PostsPage = () => {
  const { user } = useUserContext();
  const [inputValue, setInputValue] = useState('');
  const [allPostsData, setAllPostsData] = useState(null);

  useEffect(() => {
    fetchData();
  }, []);

  const handleInputChange = (e) => {
    setInputValue(e.target.value);
  };

  const handleSearchClick = () => {
    fetchData(inputValue);
  };

  const fetchData = async (event) => {
    if (!user) {
      console.error("User is null");
      return;
    }
    const endpoint = event 
      ? `/api/post/${user.appUserID}/postsByUsername/${event}`
      : `/api/post/${user.appUserID}/friendPosts`;

    try {
      const response = await fetch(endpoint);
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      const data = await response.json();
      setAllPostsData(data);
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div>
      <h1>Friend Posts</h1>
      <input type="text" value={inputValue} placeholder="Enter username" onChange={handleInputChange} />
      <button onClick={handleSearchClick}>Search</button>
      {allPostsData && allPostsData.length > 0 
        ? allPostsData.map((post) => <Post key={post.id} post={post} user={user} />)
        : <p>No posts found.</p>}
    </div>
  );
      }
  export default PostsPage;
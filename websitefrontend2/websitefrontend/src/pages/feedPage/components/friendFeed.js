import React, { useState, useEffect } from 'react';
import Post from '../../objects/post';
import { useUserContext } from '../../usercontext/UserContext';
import CreatePostButton from '../../../buttonComponents/createPostButton/createPostButton';
const FriendFeed = () => {
  const { user } = useUserContext();
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [inputValue, setInputValue] = useState('');
  const [allPostsData, setAllPostsData] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [filteredPosts, setFilteredPosts] = useState([]);

  useEffect(() => {
    fetchData();
  }, []);

  useEffect(() => {
    if (allPostsData && searchTerm) {
      setFilteredPosts(allPostsData.filter(post => post.posterusername.toLowerCase().includes(searchTerm.toLowerCase())));
    } else if (allPostsData) {
      setFilteredPosts(allPostsData);
    }
  }, [searchTerm, allPostsData]);

  const fetchData = () => {
    const endpoint = `/api/post/${user.appUserID}/friendPosts`; 
    
    setIsLoading(true);
    fetch(endpoint)
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
      .catch((err) => {
        setError(err.message);
        setIsLoading(false);
      });
  };

  const handleInputChange = (event) => setSearchTerm(event.target.value);

  return (
    <div>
      <h1>Friend Posts</h1>
      {user && <CreatePostButton></CreatePostButton>}
      <input 
        type="text" 
        value={searchTerm} 
        placeholder="Search by poster username" 
        onChange={handleInputChange} 
      />
      
      {filteredPosts && filteredPosts.length > 0 ? filteredPosts.map((post) => (
        <Post 
          key={post.id}
          post={post} 
          user={user} 
          fetchData={fetchData}
        />
      )) : <p>No posts found.</p>}
    </div>
  );
};

export default FriendFeed;
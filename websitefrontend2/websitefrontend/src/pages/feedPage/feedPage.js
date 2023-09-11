import React, { useState, useEffect } from 'react';
import Post from "../objects/post";
import { useUserContext } from '../usercontext/UserContext';
const FeedPage = () => {
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
    if (searchTerm) {
      setFilteredPosts(allPostsData.filter(post => post.posterusername.toLowerCase().includes(searchTerm.toLowerCase())));
    } else {
      setFilteredPosts(allPostsData);
    }
  }, [searchTerm, allPostsData]);

  const fetchData = () => {
    const endpoint = '/api/public-feed';
    
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
      <h1>All Posts</h1>
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

export default FeedPage;
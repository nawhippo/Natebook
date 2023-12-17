import React, { useState, useEffect } from 'react';
import Post from '../../objects/post';
import { useUserContext } from '../../usercontext/UserContext';
import CreatePostButton from '../../../buttonComponents/createPostButton/createPostButton';
import IconButton from '@mui/material/IconButton'; // Make sure this is correctly imported
import SearchIcon from '@mui/icons-material/Search'; // Make sure this is correctly imported
import '../../../global.css';

const PublicFeed = () => {
    const { user } = useUserContext();
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);
    const [inputValue, setInputValue] = useState(''); // This state seems unused, consider removing if not needed
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
        const endpoint = '/api/publicFeed';
        setIsLoading(true);
        fetch(endpoint)
            .then((response) => {
                if (response.status === 404) {
                    setAllPostsData([]);
                    throw new Error('No posts present');
                    if (!response.ok) {
                    throw new Error('Network response was not ok');
                        setAllPostsData([]);
                    }
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
            <h1>Feed</h1>
            <div className="search-bar-container">
                <input
                    className="search-input"
                    type="text"
                    value={searchTerm}
                    placeholder="Search by Username"
                    onChange={handleInputChange}
                />
                <button className="search-button" type="submit" onClick={() => handleInputChange({ target: { value: searchTerm } })}>
                    <SearchIcon />
                </button>
            </div>

            {user && (
                <div className="create-post-section">
                    <CreatePostButton />
                </div>
            )}
            {isLoading ? (
                <p>Loading...</p>
            ) : error ? (
                <p>Error: {error}</p>
            ) : filteredPosts && filteredPosts.length > 0 ? (
                filteredPosts.map((post) => (
                    <Post
                        key={post.id}
                        post={post}
                        user={user}
                        fetchData={fetchData}
                    />
                ))
            ) : (
                <p>No posts found.</p>
            )}
        </div>
    );
};

export default PublicFeed;
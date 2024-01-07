import React, {useEffect, useState} from 'react';
import Post from '../../objects/post';
import CreatePostButton from '../../../buttonComponents/createPostButton/createPostButton';
import SearchIcon from '@mui/icons-material/Search';
import '../../../global.css';
import {useUserContext} from '../../usercontext/UserContext';

const PublicFeed = () => {
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
            setFilteredPosts(allPostsData.filter(post => post.posterUsername.toLowerCase().includes(searchTerm.toLowerCase())));
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


    const buttonStyle = {
        backgroundColor: user?.backgroundColor || 'grey',
        color: '#FFFFFF',
    };

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
                <button className="search-button" type="submit" onClick={() => handleInputChange({ target: { value: searchTerm } })} style={buttonStyle}>
                    <SearchIcon />
                </button>
            </div>

            {user && (
                <div className="create-post-section" style={{
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                        justifyContent: 'center'}}>
                    <CreatePostButton />
                </div>
            )}
            <div style={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                justifyContent: 'center',
            }}>
            {isLoading ? (
                <p>Loading...</p>
            ) : error ? (
                <p>Error: {error}</p>
            ) : filteredPosts && filteredPosts.length > 0 ? (
                filteredPosts.map((post) => (
                    <Post
                        key={post.id}
                        post={post}

                        fetchData={fetchData}
                    />
                ))
            ) : (
                <p>No posts found.</p>
            )}
            </div>
        </div>
    );
};

export default PublicFeed;
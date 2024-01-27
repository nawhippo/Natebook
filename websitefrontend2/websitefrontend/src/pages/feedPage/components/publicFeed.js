import React, {useEffect, useState} from 'react';
import Post from '../../objects/post';
import CreatePostButton from '../../../buttonComponents/createPostButton/createPostButton';
import SearchIcon from '@mui/icons-material/Search';
import '../../../global.css';
import {useUserContext} from '../../usercontext/UserContext';
import {getRandomColor} from "../../../FunSFX/randomColorGenerator";
import {fetchWithJWT} from "../../../utility/fetchInterceptor";

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

    const refreshPosts = () => {
        fetchData();
    };


    useEffect(() => {
        if (allPostsData && searchTerm) {
            const searchTermLower = searchTerm.toLowerCase();
            setFilteredPosts(allPostsData.filter(post =>
                post.posterUsername.toLowerCase().includes(searchTermLower) ||
                post.title.toLowerCase().includes(searchTermLower)
            ));
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

    const deleteNotificationForPost = (postId) => {
        if (user && postId) {
            fetchWithJWT(`/api/notifications/posts/${user.appUserID}/delete/${postId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Notification could not be deleted');
                    }
                    console.log('Notification deleted');
                })
                .catch(error => console.error('Error:', error));
        }
    };

    const handleInputChange = (event) => setSearchTerm(event.target.value);


    const buttonStyle = {
        backgroundColor: user?.backgroundColor || getRandomColor(),
        color: '#FFFFFF',
    };

    return (
        <div style={{marginRight: "30px", marginLeft: "30px"}}>
            <h1 style={{textAlign:'center'}}>Feed</h1>
            <div className="search-bar-container">
                <input
                    className="search-input"
                    type="text"
                    value={searchTerm}
                    placeholder="Search by username or subject"
                    onChange={handleInputChange}
                />
                <button className="search-button" type="submit" onClick={handleInputChange} style={buttonStyle}>
                    <SearchIcon />
                </button>
            </div>

            {user && (
                <div className="create-post-section" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
                    <CreatePostButton onPostCreated={refreshPosts}/>
                </div>
            )}

            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
                {isLoading ? <p>Loading...</p> : error ? <p>Error: {error}</p> : filteredPosts.length > 0 ? filteredPosts.map((post) => (
                    <div key={post.id} onMouseEnter={() => deleteNotificationForPost(post.id)}>
                        <Post post={post} posterid={post.posterAppUserId} fetchData={fetchData} />
                    </div>
                )) : <p>No posts found.</p>}
            </div>
        </div>
    );
};

export default PublicFeed;
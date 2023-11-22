import React, { useState, useEffect } from 'react';
import { useUserContext } from '../../pages/usercontext/UserContext';
import '../../global.css'
import './CreatePostButton.css'
import PostAddIcon from '@mui/icons-material/PostAdd';
import PublicIcon from '@mui/icons-material/Public';
import PublicOffIcon from '@mui/icons-material/PublicOff';

const CreatePostButton = () => {
  const { user } = useUserContext(); 
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [dateTime, setDateTime] = useState('');
  const [publicStatus, setPublicStatus] = useState(true);
  const [message, setMessage] = useState('');
  const [showForm, setShowForm] = useState(false);
  useEffect(() => {
    const currentDate = new Date();
    const year = currentDate.getFullYear();
    const month = (currentDate.getMonth() + 1).toString().padStart(2, '0');
    const day = currentDate.getDate().toString().padStart(2, '0');
    const hours = currentDate.getHours().toString().padStart(2, '0');
    const minutes = currentDate.getMinutes().toString().padStart(2, '0');
    const seconds = currentDate.getSeconds().toString().padStart(2, '0');
    const milliseconds = currentDate.getMilliseconds().toString().padStart(3, '0');

    const formattedDateTime = `${year}-${month}-${day}T${hours}:${minutes}:${seconds}.${milliseconds}`;
    setDateTime(formattedDateTime);
  }, []);

  const handlePublicStatusToggle = () => {
    setPublicStatus(!publicStatus);
  };


  const toggleForm = () => { 
    setShowForm(!showForm);
  };


const handleCreatePost = async () => {
  try {
    const parsedDateTime = new Date(dateTime);
    const formattedDateTime = `${(parsedDateTime.getMonth() + 1).toString().padStart(2, '0')}-${parsedDateTime.getDate().toString().padStart(2, '0')}-${parsedDateTime.getFullYear()} at ${parsedDateTime.toLocaleTimeString('en-US', {
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit',
      hour12: true,
    })}`;

    const response = await fetch(`/api/post/${user.appUserID}/createPost`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        title: title,
        description: description,
        dateTime: formattedDateTime,
        friendsOnly: !publicStatus,
        posterUsername: user.username
      }),
    });

    if (response.ok) {
      setMessage('Post created successfully!');
      setTitle('');
      setDescription('');
    } else {
      setMessage('Failed to create post.');
    }
  } catch (error) {
    console.error('Error creating post:', error);
    setMessage('An error occurred while creating the post.'); 
  }
};


  return (
      <div className="create-post-container">
        <button className="button" onClick={toggleForm}>Create Post</button>
        {showForm && (
            <div>
              <div>
                <input
                    className="post-input"
                    type="text"
                    placeholder="Title"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                />
              </div>
              <div>
            <textarea
                className="post-textarea"
                placeholder="Description"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
            />
              </div>
              <div className="public-status-icon" onClick={handlePublicStatusToggle}>
                {publicStatus ? <PublicIcon /> : <PublicOffIcon />}
                <span>{publicStatus ? 'Public' : 'Private'}</span>
              </div>
              <div>
                <button className="button" onClick={handleCreatePost}>Submit Post</button>
              </div>
              <div>
                {message && <p className="create-post-message">{message}</p>}
              </div>
            </div>
        )}
      </div>
  );
};

export default CreatePostButton;
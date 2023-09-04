import React, { useState, useEffect } from 'react';
import { useUserContext } from '../../usercontext/UserContext';

const CreatePost = () => {
  const { user } = useUserContext(); 
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [dateTime, setDateTime] = useState('');
  const [publicStatus, setPublicStatus] = useState(true);
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
  
  const handlePublicStatusChange = (e) => {
    setPublicStatus(e.target.checked);
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
        public: publicStatus,
      }),
    });

    if (response.ok) {
      alert('Post created successfully!');
    } else {
      alert('Failed to create post.');
    }
  } catch (error) {
    console.error('Error creating post:', error);
  }
};

  return (
    <div>
      <h2>Create Post</h2>
      <div>
        <input
          type="text"
          placeholder="Title"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />
      </div>
      <div>
        <textarea
          placeholder="Description"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />
      </div>
      <div>
        <label>
          <input
          type="checkbox"
          checked={publicStatus}
          onChange={handlePublicStatusChange}
          />
        </label>
      </div>
      <div>
      <button onClick={handleCreatePost}>Create Post</button>
    </div>
  </div>
  );
};

export default CreatePost;
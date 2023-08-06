import React, { useState, useEffect } from 'react';
import { useUserContext } from '../../login/UserContext';

const CreatePost = () => {
  const { user } = useUserContext(); // Access the userId from the UserContext
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [dateTime, setDateTime] = useState('');

  useEffect(() => {
    // Set the date/time to the current date and time when the component mounts
    const currentDate = new Date();
    setDateTime(currentDate.toISOString().slice(0, 16));
  }, []);

  const handleCreatePost = async () => {
    try {
      const response = await fetch(`/api/${user.appUserID}/createPost`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          title: title,
          description: description,
          dateTime: dateTime, // Include the date/time in the request body
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
        <input
          type="datetime-local"
          value={dateTime}
          onChange={(e) => setDateTime(e.target.value)}
        />
      </div>
      <div>
        <button onClick={handleCreatePost}>Create Post</button>
      </div>
    </div>
  );
};

export default CreatePost;
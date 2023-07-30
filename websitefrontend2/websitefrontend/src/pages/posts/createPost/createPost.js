import React, { useState } from 'react';
import { useUserContext } from '../../login/UserContext';
const CreatePost = () => {
  //obviously only you can only use your own id for this.
  const { userId } = useUserContext(); // Access the userId from the UserContext
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');

  const handleCreatePost = async () => {
    try {
      const response = await fetch(`/createPost/${userId}`, { // Use the userId in the URL
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          title: title,
          description: description,
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
        <button onClick={handleCreatePost}>Create Post</button>
      </div>
    </div>
  );
};

export default CreatePost;
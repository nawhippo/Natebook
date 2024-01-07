import React, { useState } from 'react';
import { useUserContext } from '../../pages/usercontext/UserContext';
import PublicIcon from '@mui/icons-material/Public';
import PublicOffIcon from '@mui/icons-material/PublicOff';
import { fetchWithJWT } from "../../utility/fetchInterceptor";
import '../../global.css';
import './CreatePostButton.css';
import ProfilePictureComponent from "../ProfilePictureComponent";

const CreatePostButton = () => {
  const { user } = useUserContext();
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [images, setImages] = useState([]);
  const [publicStatus, setPublicStatus] = useState(true);
  const [message, setMessage] = useState('');

  const handlePublicStatusToggle = () => {
    setPublicStatus(!publicStatus);
  };

  const convertToBase64 = (file) => {
    return new Promise((resolve, reject) => {
      const fileReader = new FileReader();
      fileReader.readAsDataURL(file);
      fileReader.onload = () => {
        let base64 = fileReader.result;
        base64 = base64.split(',')[1];
        resolve(base64);
      };
      fileReader.onerror = (error) => {
        reject(error);
      };
    });
  };

  const handleImageChange = async (e) => {
    const selectedImages = Array.from(e.target.files);
    const base64Images = await Promise.all(selectedImages.map(async (file) => {
      return await convertToBase64(file);
    }));
    setImages(base64Images);
  };

  const buttonStyle = {
    backgroundColor: user && user.backgroundColor ? user.backgroundColor : 'grey',
    color: '#FFFFFF',
    border: 'none',
    padding: '10px 20px',
    borderRadius: '4px',
    cursor: 'pointer',
    margin: '10px 0'
  };

  const handleCreatePost = async () => {
    const postBody = {
      title,
      description,
      publicStatus,
      images
    };

    try {
      const response = await fetchWithJWT(`/api/post/${user.appUserID}/createPost`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(postBody)
      });

      const responseData = await response.json();
      if (response.ok) {
        setMessage('Post created successfully!');
        setTitle('');
        setDescription('');
        setImages([]);
      } else {
        setMessage('Failed to create post.');
      }
    } catch (error) {
      console.error('Error creating post:', error);
      setMessage('An error occurred while creating the post.');
    }
  };

  return (
      <div className="create-post-container" style={{
        padding: "20px",
        justifyContent:'center',
        display:'flex',
        width: '495px',
        flexDirection:'column',
        border: '2px dotted',
        borderRadius: '5px',
        borderColor: user && user.backgroundColor ? user.backgroundColor : 'grey',
        marginBottom: '30px'
      }}>
        <div style={{display: 'flex', justifyContent: "center", marginBottom: '15px'}}>
          <div style={{ transform: 'translateY(27.5px)', fontSize: '20px' }}>{user.username} </div>
          <ProfilePictureComponent userid={user.appUserID} style={{transform: 'translateY(30px) !important'}}/>
        </div>
        <input style={{width:'400px', transform:"translateX(30px)"}}
            className="post-input"
            type="text"
            placeholder="Subject"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
        />
        <textarea style={{width:'400px', transform:"translateX(30px)"}}
            className="post-textarea"
            placeholder="What's on your mind?"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
        />
        <div className="public-status-toggle" onClick={handlePublicStatusToggle}>
          {publicStatus ? <PublicIcon /> : <PublicOffIcon />}
          <span>{publicStatus ? 'Public' : 'Private'}</span>
        </div>
        <input
            id="image-upload"
            className="post-image-upload"
            type="file"
            accept="image/*"
            multiple
            onChange={handleImageChange}
        />
        <label htmlFor="image-upload" className="image-upload-label">Upload Image</label>
        <button className="button" onClick={handleCreatePost} style={{...buttonStyle, transform:'translateX(95px)'}} >Submit Post</button>
        {message && <p className="create-post-message">{message}</p>}
      </div>
  );
};

export default CreatePostButton;
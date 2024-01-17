import React, { useState } from 'react';
import {fetchWithJWT} from "../../utility/fetchInterceptor";

const AddImageToPostButton = ({ postid }) => {
    const [images, setImages] = useState([]);
    const [error, setError] = useState(null);

    const handleImageChange = (e) => {
        setImages([...e.target.files]);
    };

    const uploadPostImage = async () => {
        const formData = new FormData();
        images.forEach(image => formData.append('image', image));

        try {
            const response = await fetchWithJWT(`/post/${postid}/uploadImagetoPost`, {
                method: 'PUT',
                body: formData,
            });

            if (!response.ok) {
                throw new Error(`Error: ${response.status}`);
            }

            console.log('Images uploaded successfully');
        } catch (error) {
            console.error('Error:', error);
            setError(error);
        }
    };

    return (
        <div>
            <input type="file" multiple onChange={handleImageChange} />
            <button onClick={uploadPostImage}>Upload Images</button>
            {error && <p>Error uploading images: {error.message}</p>}
        </div>
    );
};

export default AddImageToPostButton;
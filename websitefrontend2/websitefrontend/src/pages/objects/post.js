import React, { useEffect, useState } from 'react';
import './PostStyle.css';
import Comment from './comment';
import postImage from './postimage';
import CommentForm from '../../buttonComponents/createCommentButton/createCommentButton';
import ReactionButtons from '../../buttonComponents/reactPostButtons/reactPostButtons';
import { DeletePostButton } from '../../buttonComponents/deletePostButton/deletePostButton';

const Post = ({ post, user, fetchData }) => {
    const [localLikesCount, setLocalLikesCount] = useState(post.likesCount);
    const [localDislikesCount, setLocalDislikesCount] = useState(post.dislikesCount);
    const [comments, setComments] = useState([]);
    const [images, setImages] = useState([]);
    const updateLikesDislikes = (newLikes, newDislikes) => {
        setLocalLikesCount(newLikes);
        setLocalDislikesCount(newDislikes);
    };

    const fetchComments = async () => {
        try {
            const response = await fetch(`/api/post/${post.id}/comments`);
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const commentsData = await response.json();
            setComments(commentsData);
        } catch (error) {
            console.error('Error fetching comments:', error);
        }
    };


    const fetchImages = async () => {
        try {
            const response = await fetch(`/api/post/${post.id}/images`);
            console.log(response);
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const imagesData = await response.json();
            setImages(imagesData);
        } catch (error) {
            console.error('Error fetching Images:', error);
        }
    };

    useEffect(() => {
        setLocalLikesCount(post.likesCount);
        setLocalDislikesCount(post.dislikesCount);
        fetchComments();
        fetchImages();
    }, [post]);

    return (
        <div className="post-container">
            <div className="post-title">{post.title}</div>
            <div className="post-description">{post.description}</div>
            <div className="post-content">{post.content}</div>
            <div className="post-content">{post.content}</div>
            <p>Likes: {localLikesCount} Dislikes: {localDislikesCount}</p>

            <ReactionButtons
                postId={post.id}
                updateLikesDislikes={updateLikesDislikes}
            />
            {images.map((image) => (
                <img
                    key={image.id}
                    src={`data:image/${image.format};base64,${image.base64EncodedImage}`} // Adjusted to use the correct field
                    alt="Post"
                    style={{ width: image.width/4, height: image.height/4 }}
                />
            ))}
            {user.username === post.posterUsername && (
                <DeletePostButton
                    postId={post.id}
                    fetchData={fetchData}
                />
            )}
            {images.map((comment) => (
                <Comment
                    key={comment.id}
                    comment={comment}
                    user={user}
                    postId={post.id}
                    posterId={post.posterid}
                    fetchData={fetchData}
                    updateLikesDislikes={updateLikesDislikes}
                />
            ))}
            <CommentForm
                userId={user.appUserID}
                posterusername={post.posterUsername}
                postId={post.id}
            />
            {comments.map((comment) => (
                <Comment
                    key={comment.id}
                    comment={comment}
                    user={user}
                    postId={post.id}
                    posterId={post.posterid}
                    fetchData={fetchData}
                    updateLikesDislikes={updateLikesDislikes}
                />
            ))}
        </div>
    );
};

export default Post;
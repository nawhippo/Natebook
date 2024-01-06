import React, {useEffect, useState} from 'react';
import './PostStyle.css';
import Comment from './comment';
import CommentForm from '../../buttonComponents/createCommentButton/createCommentButton';
import ReactionButtons from '../../buttonComponents/reactPostButtons/reactPostButtons';
import {DeletePostButton} from '../../buttonComponents/deletePostButton/deletePostButton';
import ProfilePictureComponent from "../../buttonComponents/ProfilePictureComponent";
import {useUserContext} from "../usercontext/UserContext";
import {fetchWithJWT} from "../../utility/fetchInterceptor";

const Post = ({ post, fetchData }) => {
    const user = useUserContext();
    const [localLikesCount, setLocalLikesCount] = useState(post.likesCount);
    const [localDislikesCount, setLocalDislikesCount] = useState(post.dislikesCount);
    const [comments, setComments] = useState([]);
    const [images, setImages] = useState([]);
    const [isUserLoggedIn, setIsUserLoggedIn] = useState(!!user); // Check if user is logged in
    const [areCommentsCollapsed, setAreCommentsCollapsed] = useState(true);

    const updateLikesDislikes = (newLikes, newDislikes) => {
        setLocalLikesCount(newLikes);
        setLocalDislikesCount(newDislikes);
    };

    const fetchComments = async () => {
        try {
            console.log(user);
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
            const response = await fetchWithJWT(`/api/post/${post.id}/images`);
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

    const footerStyle = {
        position: 'absolute',
        right: '40px'
    };
    return (
        <div className="post-container">
            <div className="post-title">{post.title}</div>
            <div className="post-description">{post.description}</div>
            <div className="post-content">{post.content}</div>
            <div style={{display: 'flex'}}>
                <div className="post-creator" style={{ transform: 'translateY(27.5px)' }}>By: {post.posterUsername}  </div>
                <ProfilePictureComponent userid={post.posterId} style={{ transform: 'translateY(30px) !important' }} />
            </div>
                <br/>
            {images.map((image) => (
                <img
                    key={image.id}
                    src={`data:image/${image.format};base64,${image.base64EncodedImage}`}
                    alt="Post"
                    style={{ width: image.width, height: image.height }}
                />
            ))}
            <p>Likes: {localLikesCount} Dislikes: {localDislikesCount}</p>

            <ReactionButtons
                postId={post.id}
                updateLikesDislikes={updateLikesDislikes}
            />

            {user && user.username === post.posterUsername && (
                <DeletePostButton
                    postId={post.id}
                    fetchData={fetchData}
                />
            )}
            {user && (
                <CommentForm
                    userId={user.appUserID}
                    posterusername={post.posterUsername}
                    postId={post.id}
                    updateComments={fetchComments}
                />
            )}
            {user && comments.map((comment) => (
                <Comment
                    key={comment.id}
                    comment={comment}
                    commenterId={comment.commenterid}
                    user={user}
                    postId={post.id}
                    posterId={post.posterId}
                    posterusername={post.posterUsername}
                    fetchData={fetchData}
                    updateComments={fetchComments}
                />
            ))}
        </div>
    );
};

export default Post;
import React from 'react';
import {CirclePicker} from 'react-color';
import {useUserContext} from '../../pages/usercontext/UserContext';

const ColorWheel = () => {
    const { updateBackgroundColor } = useUserContext();

    const handleChangeComplete = (color) => {
        updateBackgroundColor(color.hex);
    };

    return (
        <div>
            <CirclePicker onChangeComplete={handleChangeComplete} />
        </div>
    );
};

export default ColorWheel;
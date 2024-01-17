export function getRandomColor(param) {
        let color = localStorage.getItem('randomColor');
        const letters = '0123456789ABCDEF';
        if (!color) {
            color = '#';
            for (let i = 0; i < 6; i++) {
                color += letters[Math.floor(Math.random() * 16)];
            }
            localStorage.setItem('randomColor', color);
        }
        return color;
    }
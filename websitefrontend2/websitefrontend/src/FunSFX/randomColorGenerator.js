export function getRandomColor() {

    let color = localStorage.getItem('randomColor');
    if (!color) {

        const letters = '0123456789ABCDEF';
        color = '#';
        for (let i = 0; i < 6; i++) {
            color += letters[Math.floor(Math.random() * 16)];
        }

        localStorage.setItem('randomColor', color);
    }
    return color;
}

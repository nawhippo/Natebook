let overlayVisibilityCallback = null;

export function registerOverlayVisibilityCallback(callback) {
    overlayVisibilityCallback = callback;
}

export function showSessionExpiredOverlay() {
    if (overlayVisibilityCallback) {
        overlayVisibilityCallback(true);
    }
}

export function hideSessionExpiredOverlay() {
    if (overlayVisibilityCallback) {
        overlayVisibilityCallback(false);
    }
}
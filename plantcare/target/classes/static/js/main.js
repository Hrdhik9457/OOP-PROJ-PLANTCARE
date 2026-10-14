// PlantCare — minimal vanilla JS & PWA Support

// Mobile nav toggle
function toggleNav() {
    const nav = document.getElementById('mobileNav');
    if (nav) nav.classList.toggle('open');
}

// Highlight active nav link
(function () {
    const path = window.location.pathname;
    document.querySelectorAll('.nav-link').forEach(link => {
        const href = link.getAttribute('href');
        if (href === path || (href !== '/' && path.startsWith(href))) {
            link.classList.add('active');
        }
    });
})();

// Register Service Worker for PWA capabilities
if ('serviceWorker' in navigator) {
    window.addEventListener('load', () => {
        navigator.serviceWorker.register('/sw.js')
            .then(reg => {
                console.log('🌿 [PWA] Service Worker active with scope:', reg.scope);
            })
            .catch(err => {
                console.warn('🌿 [PWA] Service Worker registration failed:', err);
            });
    });
}

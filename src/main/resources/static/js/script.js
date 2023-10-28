<!-- Script JavaScript pour le défilement automatique vers le contenu -->

    // Attendre que la page soit entièrement chargée
    window.addEventListener('load', function () {
    // Trouver l'élément de contenu (par exemple, par son ID)
    var contentElement = document.getElementById('content');

    // Vérifier si l'élément de contenu existe
    if (contentElement) {
    // Définir une fonction pour faire défiler vers le contenu
    function scrollToContent() {
    contentElement.scrollIntoView({ behavior: 'smooth' });
}

    // Appeler la fonction pour faire défiler vers le contenu après un court délai (par exemple, 500 millisecondes)
    setTimeout(scrollToContent, 500);
}
});

function loadIMG(url, id) {
    var P = new Promise( function (resolve, reject) {
        var parent = document.getElementById(id);
        var element = document.createElement('img');
        element.setAttribute("src", url);
        element.setAttribute("alt", url);
        element.setAttribute("width","30%");
        parent.appendChild(element);
        element.onload  = function() { resolve(url); };
        element.onerror = function() { reject(url) ; };
      }
    );
    return P;
}

Promise.all([
    loadIMG('img/gallery/img1.jpg','img-gallery-container'),
    loadIMG('img/gallery/img2.jpg','img-gallery-container'),
    loadIMG('img/gallery/img3.jpg','img-gallery-container'),
    loadIMG('img/gallery/img4.jpg','img-gallery-container'),
    loadIMG('img/gallery/img5.jpg','img-gallery-container'),
    loadIMG('img/gallery/img6.jpg','img-gallery-container'),
    loadIMG('img/gallery/img7.jpg','img-gallery-container'),
    loadIMG('img/gallery/img8.jpg','img-gallery-container'),
    loadIMG('img/gallery/img9.jpg','img-gallery-container'),
    ]).then(function() {
        console.log('Wszystko z równoległej si ̨e załadowało!');
    }).catch(function() {
        console.log('Błąd ładowania galerii rownoległej');
    });

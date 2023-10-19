(function ($) {
    "use strict";
    $.fn.ratingLocales['<LANG>'] = {
        defaultCaption: '{rating} Stars',
        starCaptions: {
            0.5: 'Demi-étoile',
            1: 'Une étoile',
            1.5: 'Une étoile et demie',
            2: 'Deux étoiles',
            2.5: 'Deux étoiles et demie',
            3: 'Trois étoiles',
            3.5: 'Trois étoiles et demie',
            4: 'Quatre étoiles',
            4.5: 'Quatre étoiles et demie',
            5: 'Cinq étoiles'
        },
        clearButtonTitle: 'Effacer',
        clearCaption: 'Non noté'
    };
})(window.jQuery);

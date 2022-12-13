// console.log("Проверка работы JS-файлов в static")

(function(){
    const carousel = {
        element: document.querySelector('#carousel'),
        previous: document.querySelector('#carousel .previous'),
        next: document.querySelector('#carousel .next'),
        images: document.querySelectorAll('#carousel .img'),
        tick: 3,
        pause: 3000,
    }


    //Инициализация:
    for (let i = 0; i < carousel.images.length; ++i)
    {
        carousel.images[i].left = carousel.element.offsetWidth * i;
        carousel.images[i].style.left = carousel.images[i].left + 'px';
    }

    // Анимирование отдельных кадров:
    let j = 0, step = carousel.element.offsetWidth;
    function animation()
    {
        if(j < step)
        {
            j++;
            for (let i = 0; i < carousel.images.length; ++i)
            {
                carousel.images[i].left -= 1;
                carousel.images[i].style.left = carousel.images[i].left + 'px';
                //console.log(carousel.images[i].left);
            }

            setTimeout(animation, carousel.tick)
        }
        else
        {
            for(let i = 0; i < carousel.images.length; i++)
            {
                if( carousel.images[i].left == -carousel.element.offsetWidth )
                {
                    carousel.images[i].left = carousel.element.offsetWidth * (carousel.images.length - 1)
                    break;
                }

            }

            j = 0;
            setTimeout(animation, carousel.pause);
        }
    }
    animation();

   // console.log(carousel);
})();


//==================================================================================

// document.addEventListener('DOMContentLoaded', function () {
//     // инициализация слайдера
//     let slider = new SimpleAdaptiveSlider('.slider', {
//         loop: true,
//         autoplay: false,
//         swipe: true
//     });
//     let thumbnailsItem = document.querySelectorAll('.slider__thumbnails-item');
//
//     function setActiveThumbnail() {
//         let sliderItemActive = document.querySelector('.slider__item_active');
//         let index = parseInt(sliderItemActive.dataset.index);
//         for (let i = 0, length = thumbnailsItem.length; i < length; i++) {
//             if (i !== index) {
//                 thumbnailsItem[i].classList.remove('active');
//             } else {
//                 thumbnailsItem[index].classList.add('active');
//             }
//         }
//     }
//     setActiveThumbnail();
//     document.querySelector('.slider').addEventListener('slider.set.active', setActiveThumbnail);
//     let sliderThumbnails = document.querySelector('.slider__thumbnails');
//     sliderThumbnails.addEventListener('click', function(e) {
//         let $target = e.target.closest('.slider__thumbnails-item');
//         if (!$target) {
//             return;
//         }
//         let index = parseInt($target.dataset.slideTo, 10);
//         slider.moveTo(index);
//     });
// });
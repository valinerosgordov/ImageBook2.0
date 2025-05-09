function pageFlipSite(id, pageWidth, pageHeight, nPages, nInitialPages, hard, imageUrl, largeImageUrl) {
    var book = $('#' + id);
    var zoomViewport = $('#zoom-viewport-' + id);

    var element = [];
    for (i = 1; i <= nInitialPages; i++) {
        element[i] = $('<div class="pageFlipLoader"></div>');
        book.append(element[i]);
    }

    if (hard) {
        element[1].addClass("hard");
        element[2].addClass("hard");
    }

    book.turn({
        width: pageWidth,
        height: pageHeight,
        pages: nPages,
        // autoCenter:true,

        when: {
            turning: function (event, page, view) {
                var book = $(this),
                    currentPage = book.turn('page'),
                    pages = book.turn('pages');

                zoomViewport.find('.thumbnails .page-' + currentPage).parent().removeClass('current');
                zoomViewport.find('.thumbnails .page-' + page).parent().addClass('current');
            },
            missing: function (event, pages) {
                // Add pages that aren't in the book

                // var range = $(this).turn('range', page);
                // for (page = range[0]; page <= range[1]; page++)
                //     addPage(page);

                for (var i = 0; i < pages.length; i++) {
                    addPage(pages[i]);
                }
            }
        }


    });

    for (i = 1; i <= nInitialPages; i++) {
        loadPage(i, element[i]);
    }

    // book.bind('turning', function (e, page) {
    //     var range = $(this).turn('range', page);
    //     for (page = range[0]; page <= range[1]; page++)
    //         addPage(page);
    // });

    function addPage(page) {
        if (!book.turn('hasPage', page)) {

            // Create a new element for this page
            var element = $('<div class="pageFlipLoader"></div>');
            // Add the page to the flipbook
            book.turn('addPage', element, page);

            if (hard && (page === nPages - 1 || page === nPages)) {
                element.addClass("hard");
            }

            // Load the page
            loadPage(page, element);
        }
    }

    function loadPage(page, pageElement) {
        // Create an image element
        var img = $('<img />');

        img.mousedown(function(e) {
            e.preventDefault();
        });

        img.load(function () {
            // Add the image to the page after loaded
            $(this).appendTo(pageElement);
            // Remove the loader indicator
            pageElement.find('.pageFlipLoader').remove();
        });

        // Load the page
        img.attr('src', imageUrl(page));
        img.attr('class', 'pageImage');
    }

    // Zoom in / Zoom out

    function zoomTo(event) {
        setTimeout(function() {
            if (zoomViewport.zoom('value') === 1) {
                zoomViewport.zoom('zoomIn', event);
            } else {
                zoomViewport.zoom('zoomOut');
            }
        }, 1);
    }

    addNav(nPages);

    // set height for preview
    var thumbnailsHeight = zoomViewport.find('#thumbnailsPage').height();
    var previewHeight = pageHeight + 2 + thumbnailsHeight + 2*10;
    zoomViewport.height(previewHeight);


    // TODO refactor
    $(document).on('click', function(event) {
        console.log("clicked");
        if (!$(event.target).closest("[id^='zoom-viewport-']").length) {
            console.log("clicked outside");
            $("[id^='zoom-viewport-']").each(function() {
                console.log("call zoomOut");
                $(this).zoom('zoomOut');
            });
        }
    });

    // Using arrow keys to turn the page
    // TODO refactor
    $(document).keydown(function (e) {
        var previous = 37,
            next = 39,
            zoomPlus = 38,
            zoomMinus = 40,
            esc = 27;
        switch (e.keyCode) {
            // case previous:
            //     book.turn('previous');
            //     e.preventDefault();
            //     break;
            // case next:
            //     book.turn('next');
            //     e.preventDefault();
            //     break;
            case esc:
                zoomViewport.zoom('zoomOut');
                e.preventDefault();
                break;
        }
    });


    // $(document).keydown(function (e) {
    //     var previous = 37,
    //         next = 39,
    //         zoomPlus = 38,
    //         zoomMinus = 40,
    //         esc = 27;
    //     switch (e.keyCode) {
    //         case previous:
    //             book.turn('previous');
    //             e.preventDefault();
    //             break;
    //         case next:
    //             book.turn('next');
    //             e.preventDefault();
    //             break;
    //         case esc:
    //             zoomViewport.zoom('zoomOut');
    //             e.preventDefault();
    //             break;
    //     }
    // });


    zoomViewport.zoom({
        duration:600,
        flipbook: book,
        max: 2,
        when: {
            resize: function (event, scale, page, pageElement) {
                if (scale === 1) {
                    loadSmallPage(page, pageElement);
                } else {
                    loadLargePage(page, pageElement);
                }
            },

            zoomIn:function () {
                zoomViewport.find('.thumbnails').hide();
                this.style.position='inherit';

                if (!window.escTip && !$.isTouch) {
                    escTip = true;

                    $('<div />', {'class': 'exit-message'}).
                    html('<div>Нажмите ESC чтобы выйти</div>').
                    appendTo($('body')).
                    delay(2000).
                    animate({opacity:0}, 500, function() {
                        $(this).remove();
                    });
                }
            },

            zoomOut: function () {
                zoomViewport.find('.thumbnails').fadeIn();
                this.style.position='relative';
            }
        }
    });

    // Zoom event
    if ($.isTouch) {
        zoomViewport.bind('zoom.doubleTap', zoomTo);
    } else {
        zoomViewport.bind('zoom.tap', zoomTo);
    }

    // Events for thumbnails

    zoomViewport.find('.thumbnails').click(function (event) {
        var page;
        if (event.target && (page = /page-([0-9]+)/.exec($(event.target).attr('class')))) {
            book.turn('page', page[1]);
        }
    });

    zoomViewport.find('.thumbnails li div').bind($.mouseEvents.over, function () {
        $(this).addClass('thumb-hover');
    }).bind($.mouseEvents.out, function () {
        $(this).removeClass('thumb-hover');
    });

    if ($.isTouch) {
        zoomViewport.find('.thumbnails').addClass('thumbanils-touch').bind($.mouseEvents.move, function (event) {
            event.preventDefault();
        });
    }

    // Events for the next button

    book.find('.next-button').bind($.mouseEvents.over, function () {
        $(this).addClass('next-button-hover');
    }).bind($.mouseEvents.out, function () {
        $(this).removeClass('next-button-hover');
    }).bind($.mouseEvents.down, function () {
        $(this).addClass('next-button-down');
    }).bind($.mouseEvents.up, function () {
        $(this).removeClass('next-button-down');
    }).click(function () {
        book.turn('next');
    });

    // Events for the next button

    book.find('.previous-button').bind($.mouseEvents.over, function () {
        $(this).addClass('previous-button-hover');
    }).bind($.mouseEvents.out, function () {
        $(this).removeClass('previous-button-hover');
    }).bind($.mouseEvents.down, function () {
        $(this).addClass('previous-button-down');
    }).bind($.mouseEvents.up, function () {
        $(this).removeClass('previous-button-down');
    }).click(function () {
        book.turn('previous');
    });


    function addNav(pageCount) {
        var thumbDiv = zoomViewport.find('#thumbnailsPage');
        var leftDiv = document.createElement('div');
        thumbDiv.append(leftDiv);
        var leftImg = document.createElement('img');
        leftDiv.appendChild(leftImg);
        leftDiv.setAttribute('class', 'left-div');
        leftImg.setAttribute('class', 'scroll_L_Arrow');
        leftImg.setAttribute('src', '/static/pageflip/images/arrowl.png');
        leftImg.setAttribute('height', '60px');
        leftImg.setAttribute('onclick', 'scrollThumb(\'Go_L\', this)');

        var innerDiv = document.createElement('div');
        thumbDiv.append(innerDiv);
        innerDiv.setAttribute('class', 'thumbnailsInnerDiv');
        var ul = document.createElement('ul');
        innerDiv.appendChild(ul);
        for (i = 1; i <= pageCount; i++) {

            var li = document.createElement('li');
            ul.appendChild(li);

            var liDiv = document.createElement('div');
            liDiv.setAttribute('class', 'pagesBox');
            li.appendChild(liDiv);

            var img1 = document.createElement('img');
            liDiv.appendChild(img1);
            img1.setAttribute('src', imageUrl(i));
            img1.setAttribute('height', 50);
            img1.setAttribute('class', 'page-' + i);

            var pageNum = document.createElement('span');
            li.appendChild(pageNum);
            if (i === 1 || i === pageCount) {
                if (i === 1) {
                    li.className = 'i';
                    liDiv.className = 'current';
                } else {
                    li.className = 'i';
                }
                pageNum.innerHTML = i;
            } else {
                i++;

                li.className = 'd';

                var img2 = document.createElement('img');
                liDiv.appendChild(img2);
                img2.setAttribute('src', imageUrl(i));
                img2.setAttribute('height', 50);
                img2.setAttribute('class', 'page-' + i);

                pageNum.innerHTML = (i - 1) + '-' + i;
            }
        }

        var rightDiv = document.createElement('div');
        thumbDiv.append(rightDiv);
        var rightImg = document.createElement('img');
        rightDiv.appendChild(rightImg);
        rightDiv.setAttribute('class', 'right-div');
        rightImg.setAttribute('class', 'scroll_R_Arrow');
        rightImg.setAttribute('height', '60px');
        rightImg.setAttribute('src', '/static/pageflip/images/arrowr.png');
        rightImg.setAttribute('onclick', 'scrollThumb(\'Go_R\', this)');
    }

    // Load large page
    function loadLargePage(page, pageElement) {
        var img = $('<img />');
        img.load(function () {
            var prevImg = pageElement.find('img');
            $(this).css({width: '100%', height: '100%'});
            $(this).appendTo(pageElement);
            prevImg.remove();
        });

        // Load new page
        img.attr('src', largeImageUrl(page));
    }

    // Load small page
    function loadSmallPage(page, pageElement) {
        var img = pageElement.find('img');
        img.css({width: '100%', height: '100%'});
        img.unbind('load');

        // Load new page
        img.attr('src', imageUrl(page));
    }
}

function pageFlip(id, pageWidth, pageHeight, nPages, nInitialPages, hard, imageUrl, largeImageUrl) {
    var book = $('#' + id);

    var element = [];
    for (i = 1; i <= nInitialPages; i++) {
        element[i] = $('<div class="pageFlipLoader"></div>');
        book.append(element[i]);
    }

    if (hard) {
        element[1].addClass("hard");
        element[2].addClass("hard");
    }

    book.turn({
        width: pageWidth,
        height: pageHeight,
        pages: nPages,
        // autoCenter:true,

        when: {
            turning: function (event, page, view) {
                var book = $(this),
                    currentPage = book.turn('page'),
                    pages = book.turn('pages');

                $('.thumbnails .page-' + currentPage).parent().removeClass('current');
                $('.thumbnails .page-' + page).parent().addClass('current');
            },
            missing: function (event, pages) {
                // Add pages that aren't in the book

                // var range = $(this).turn('range', page);
                // for (page = range[0]; page <= range[1]; page++)
                //     addPage(page);

                for (var i = 0; i < pages.length; i++) {
                    addPage(pages[i]);
                }

            }
        }


    });

    for (i = 1; i <= nInitialPages; i++) {
        loadPage(i, element[i]);
    }

    // book.bind('turning', function (e, page) {
    //     var range = $(this).turn('range', page);
    //     for (page = range[0]; page <= range[1]; page++)
    //         addPage(page);
    // });

    function addPage(page) {
        if (!book.turn('hasPage', page)) {

            // Create a new element for this page
            var element = $('<div class="pageFlipLoader"></div>');
            // Add the page to the flipbook
            book.turn('addPage', element, page);

            if (hard && (page == nPages - 1 || page == nPages)) {
                element.addClass("hard");
            }

            // Load the page
            loadPage(page, element);
        }
    }

    function loadPage(page, pageElement) {
        // Create an image element
        var img = $('<img />');

        img.mousedown(function(e) {
            e.preventDefault();
        });

        img.load(function () {
            // Add the image to the page after loaded
            $(this).appendTo(pageElement);

            // Remove the loader indicator
            pageElement.find('.pageFlipLoader').remove();
        });

        // Load the page
        img.attr('src', imageUrl(page));
        img.attr('class', 'pageImage');
    }

    // Zoom in / Zoom out

    function zoomTo(event) {

        setTimeout(function() {
            if ($('#zoom-viewport').zoom('value')==1) {
                $('#zoom-viewport').zoom('zoomIn', event);
            } else {
                $('#zoom-viewport').zoom('zoomOut');
            }
        }, 1);

    }

    addNav(nPages);

    // Using arrow keys to turn the page
    $(document).keydown(function (e) {

        var previous = 37,
            next = 39,
            zoomPlus = 38,
            zoomMinus = 40,
            esc = 27;
        switch (e.keyCode) {
            case previous:
                book.turn('previous');
                e.preventDefault();
                break;
            case next:
                book.turn('next');
                e.preventDefault();
                break;
            case esc:
                $('#zoom-viewport').zoom('zoomOut');
                e.preventDefault();
                break;
        }
    });


    $("#zoom-viewport").zoom({
        duration:600,
        flipbook: book,
        max: 2,
        when: {
            resize: function (event, scale, page, pageElement) {
                if (scale === 1) {
                    loadSmallPage(page, pageElement);
                } else {
                    loadLargePage(page, pageElement);
                }
            },

            zoomIn:function () {
                $('.thumbnails').hide();
                document.getElementById('zoom-viewport').style.position='inherit';

                if (!window.escTip && !$.isTouch) {
                    escTip = true;

                    $('<div />', {'class': 'exit-message'}).
                    html('<div>Нажмите ESC чтобы выйти</div>').
                    appendTo($('body')).
                    delay(2000).
                    animate({opacity:0}, 500, function() {
                        $(this).remove();
                    });
                }
            },

            zoomOut: function () {
                $('.thumbnails').fadeIn();
                document.getElementById('zoom-viewport').style.position='relative';
            }
        }
    });

    // TODO remove
    // $("#zoom-viewport").bind("zoom.tap", function (event) {
    //     if ($(this).zoom("value") == 1) {
    //
    //         $('.thumbnails').hide();
    //         document.getElementById('zoom-viewport').style.position='inherit';
    //
    //         $(this).zoom("zoomIn", event);
    //     } else {
    //         $(this).zoom("zoomOut");
    //     }
    // });

    // Zoom event

    if ($.isTouch) {
        $('#zoom-viewport').bind('zoom.doubleTap', zoomTo);
    } else {
        $('#zoom-viewport').bind('zoom.tap', zoomTo);
    }

    // Events for thumbnails

    $('.thumbnails').click(function (event) {

        var page;

        if (event.target && (page = /page-([0-9]+)/.exec($(event.target).attr('class')))) {

            book.turn('page', page[1]);
        }
    });

    $('.thumbnails li div').bind($.mouseEvents.over, function () {

        $(this).addClass('thumb-hover');

    }).bind($.mouseEvents.out, function () {

        $(this).removeClass('thumb-hover');

    });

    if ($.isTouch) {

        $('.thumbnails').addClass('thumbanils-touch').bind($.mouseEvents.move, function (event) {
            event.preventDefault();
        });

    }

    // Events for the next button

    $('.next-button').bind($.mouseEvents.over, function () {

        $(this).addClass('next-button-hover');

    }).bind($.mouseEvents.out, function () {

        $(this).removeClass('next-button-hover');

    }).bind($.mouseEvents.down, function () {

        $(this).addClass('next-button-down');

    }).bind($.mouseEvents.up, function () {

        $(this).removeClass('next-button-down');

    }).click(function () {

        book.turn('next');

    });

    // Events for the next button

    $('.previous-button').bind($.mouseEvents.over, function () {

        $(this).addClass('previous-button-hover');

    }).bind($.mouseEvents.out, function () {

        $(this).removeClass('previous-button-hover');

    }).bind($.mouseEvents.down, function () {

        $(this).addClass('previous-button-down');

    }).bind($.mouseEvents.up, function () {

        $(this).removeClass('previous-button-down');

    }).click(function () {

        book.turn('previous');

    });


    function addNav(pageCount) {
        var thumbDiv = $('#thumbnailsPage');
        var leftDiv = document.createElement('div');
        thumbDiv.append(leftDiv);
        var leftImg = document.createElement('img');
        leftDiv.appendChild(leftImg);
        leftDiv.setAttribute('class', 'left-div');
        leftImg.setAttribute('class', 'scroll_L_Arrow');
        leftImg.setAttribute('src', '/static/pageflip/images/arrowl.png');
        leftImg.setAttribute('height', '60px');
        leftImg.setAttribute('onclick', 'scrollThumb(\'Go_L\', this)');

        var innerDiv = document.createElement('div');
        innerDiv.setAttribute('class', 'thumbnailsInnerDiv');
        thumbDiv.append(innerDiv);
        var ul = document.createElement('ul');
        innerDiv.appendChild(ul);
        for (i = 1; i <= pageCount; i++) {

            var li = document.createElement('li');
            ul.appendChild(li);

            var liDiv = document.createElement('div');
            liDiv.setAttribute('class', 'pagesBox');
            li.appendChild(liDiv);

            var img1 = document.createElement('img');
            liDiv.appendChild(img1);
            img1.setAttribute('src', imageUrl(i));
            img1.setAttribute('height', 50);
            img1.setAttribute('class', 'page-' + i);

            var pageNum = document.createElement('span');
            li.appendChild(pageNum);
            if (i == 1 || i == pageCount) {
                if (i == 1) {
                    li.className = 'i';
                    liDiv.className = 'current';
                }
                else {
                    li.className = 'i';
                }
                pageNum.innerHTML = i;
            } else {
                i++;

                li.className = 'd';

                var img2 = document.createElement('img');
                liDiv.appendChild(img2);
                img2.setAttribute('src', imageUrl(i));
                img2.setAttribute('height', 50);
                img2.setAttribute('class', 'page-' + i);

                pageNum.innerHTML = (i - 1) + '-' + i;
            }
        }


        var rightDiv = document.createElement('div');
        thumbDiv.append(rightDiv);
        var rightImg = document.createElement('img');
        rightDiv.appendChild(rightImg);
        rightDiv.setAttribute('class', 'right-div');
        rightImg.setAttribute('class', 'scroll_R_Arrow');
        rightImg.setAttribute('height', '60px');
        rightImg.setAttribute('src', '/static/pageflip/images/arrowr.png');
        rightImg.setAttribute('onclick', 'scrollThumb(\'Go_R\', this)');
    }

    // Load large page

    function loadLargePage(page, pageElement) {

        var img = $('<img />');

        img.load(function () {

            var prevImg = pageElement.find('img');
            $(this).css({width: '100%', height: '100%'});
            $(this).appendTo(pageElement);
            prevImg.remove();

        });

        // Loadnew page

        img.attr('src', largeImageUrl(page));
    }

    // Load small page

    function loadSmallPage(page, pageElement) {

        var img = pageElement.find('img');

        img.css({width: '100%', height: '100%'});

        img.unbind('load');
        // Loadnew page

        img.attr('src', imageUrl(page));
    }

    // function scrollThumb(direction) {
    //     if (direction == 'Go_L') {
    //         $('.thumbnailsInnerDiv').animate({
    //             scrollLeft: "-=" + 500 + 'px'
    //         }, function () {
    //         });
    //     } else if (direction == 'Go_R') {
    //         $('.thumbnailsInnerDiv').animate({
    //             scrollLeft: "+=" + 500 + 'px'
    //         }, function () {
    //         });
    //     }
    // }
}

// function scrollThumb(direction) {
//     if (direction === 'Go_L') {
//         zoomViewport.find('.thumbnailsInnerDiv').animate({
//             scrollLeft: "-=" + 500 + 'px'
//         }, function () {
//         });
//     } else if (direction === 'Go_R') {
//         zoomViewport.find('.thumbnailsInnerDiv').animate({
//             scrollLeft: "+=" + 500 + 'px'
//         }, function () {
//         });
//     }
// }

function scrollThumb(direction, event) {
    var thumbDiv = $(event).parent().siblings('.thumbnailsInnerDiv');
    if (direction === 'Go_L') {
        thumbDiv.animate({
            scrollLeft: "-=" + 500 + 'px'
        }, function () {
        });
    } else if (direction === 'Go_R') {
        thumbDiv.animate({
            scrollLeft: "+=" + 500 + 'px'
        }, function () {
        });
    }
}
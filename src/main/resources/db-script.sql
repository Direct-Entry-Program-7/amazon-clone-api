CREATE TABLE item
(
    code        VARCHAR(10) PRIMARY KEY,
    title       VARCHAR(255)  NOT NULL,
    image       VARCHAR(500)  NOT NULL,
    rating      ENUM('1','2','3','4','5') NOT NULL,
    qty         INT           NOT NULL,
    unit_price  DECIMAL(5, 2) NOT NULL,
    description VARCHAR(1000) NOT NULL
);

INSERT INTO item VALUES ('I001',
                         'https://m.media-amazon.com/images/I/71NTi82uBEL._AC_UL320_.jpg',
                         'Apple AirPods with Charging Case',
                         3,
                         299,
                         5,
                         '<div>Lorem ipsum dolor <b>sit</b> amet, consectetur adipisicing elit. Aliquid asperiores assumenda eligendi ex exercitationem
                         facilis, fugiat iste laborum libero molestias nemo nostrum numquam officia optio placeat possimus sed sunt ut.
                       </div>
                       <div>Ad alias commodi consequuntur deserunt dicta est eveniet excepturi fuga fugit illo illum minus molestias
                         mollitia, nam necessitatibus nesciunt nostrum odio odit officiis porro provident quam quas reiciendis sint ut.
                       </div>
                       <div>Consequatur dignissimos necessitatibus optio temporibus voluptatem? Dolor maiores neque perferendis praesentium
                         soluta! Aspernatur dicta dolor incidunt minima molestias, nihil pariatur perferendis porro possimus praesentium
                         provident quasi sit! Dolore placeat, repudiandae?
                       </div>');
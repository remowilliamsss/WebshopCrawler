# StoreCrawler
REST-сервис, который собирает и заносит в базу данных информацию о товарах из
интернет-магазинов, сравнивает цены на разных площадках.

Поддерживаемые сайты:
1. sneakerhead.ru - при запросе используем "sneakerhead" вместо
названия сайта
2. footboxshop.ru - при запросе используем "footbox" вместо названия
сайта

Функционал:
1. POST-запрос на адрес "api/products/search" с телом запроса
   

      {
         "query" : "Название искомой модели"
      }


вернет JSON с характеристиками, стоимостью и доступными размерами на разных площадках всех
найденных моделей по данному запросу:

    {
        [
            {
                "name": "Название модели",
                "sku": "Номер артикула",
                "image": "URL изображения",
                "category": "Название категории",
                "brand": "Название бренда",
                "color": "Цвет модели",
                "country": "Страна производителя",
                "gender": "Пол",
                "differences": [
                    {
                        "storeName": "Название магазина № 1",
                        "price": Цена в магазине №1,
                        "priceCurrency": "Обозначение валюты",
                        "sizes": "Доступные размеры в магазине № 1",
                        "url": "URL товара в магазине № 1"
                    },
                    {
                        "storeName": "Название магазина № 2",
                        "price": Цена в магазине №2,
                        "priceCurrency": "Обозначение валюты",
                        "sizes": "Доступные размеры в магазине № 2",
                        "url": "URL товара в магазине № 2"
                    },
                     ...
                ]
            },
            ...
        ]
    }

2. POST-запрос на адрес "api/products/find_by_sku" с телом запроса


      {
         "query" : "Артикул искомой модели"
      }


вернет JSON с характеристиками, стоимостью и доступными размерами этой модели на разных
площадках:

      {
        [
            {
                "name": "Название модели",
                "sku": "Номер артикула",
                "image": "URL изображения",
                "category": "Название категории",
                "brand": "Название бренда",
                "color": "Цвет модели",
                "country": "Страна производителя",
                "gender": "Пол",
                "differences": [
                    {
                        "storeName": "Название магазина № 1",
                        "price": Цена в магазине №1,
                        "priceCurrency": "Обозначение валюты",
                        "sizes": "Доступные размеры в магазине № 1",
                        "url": "URL товара в магазине № 1"
                    },
                    {
                        "storeName": "Название магазина № 2",
                        "price": Цена в магазине №2,
                        "priceCurrency": "Обозначение валюты",
                        "sizes": "Доступные размеры в магазине № 2",
                        "url": "URL товара в магазине № 2"
                    },
                     ...
                ]
            }
        ]
    }

3. GET-запрос на адрес "api/products" с названием сайта в параметре "store" - в ответе
   приходит JSON с товарами этого сайта. Поддерживается пагинация. Чтобы
   настроить разбитие на страницы, нужно передать параметры: "page" - номер
   страницы и "size" - количество товаров на странице (по умолчанию 20).

В проекте использовались:
1. Spring Boot
2. Spring REST
3. Spring Data
4. PostgreSQL
5. jsoup
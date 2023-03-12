# store-crawler
REST-сервис, который собирает и предоставляет информацию о товарах из
интернет-магазинов.

Поддерживаемые магазины:
1. sneakerhead.ru - при запросе используем "sneakerhead"
2. footboxshop.ru - при запросе используем "footbox"

Функционал:

1. GET-запрос на адрес "api/products" с названием магазина в параметре "store" - в ответе
   приходит JSON с товарами этого магазина:

   `{
      "products":[
         {
            "name": "Название модели",
            "sku": "Номер артикула",
            "category": "Категория",
            "brand": "Название бренда",
            "image": "URL изображения",
            "color": "Цвет модели",
            "price": Цена,
            "priceCurrency": "Обозначение валюты",
            "country": "Страна производителя",
            "size": "Доступные размеры",
            "gender": "Пол",
            "url": "URL товара",
            "storeType": "Название магазина"
         },
         ...
      ],
      "pageCount" = "Количество страниц"
   }`

   Поддерживается пагинация. Чтобы
   настроить разбитие на страницы, нужно передать параметры: "page" - номер
   страницы и "size" - количество товаров на странице.

В проекте использовались:
1. Spring Boot
2. Spring REST
3. Spring Data
4. Spring Security
5. PostgreSQL
6. jsoup
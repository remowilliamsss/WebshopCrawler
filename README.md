# store-crawler
Собирает и предоставляет информацию о товарах из
интернет-магазинов.

При запросах требуется базовая аутентификация.

Поддерживаемые магазины:
1. sneakerhead.ru - при запросе используем "sneakerhead"
2. footboxshop.ru - при запросе используем "footbox"

API:

1. GET-запрос на адрес "api/products" с названием магазина в параметре "store" - в ответе
   приходит JSON с товарами этого магазина:

   ```
   
   {
      "totalElements": 0,
      "totalPages": 0,
      "size": 0,
      "content": [
         {
            "name": "string",
            "sku": "string",
            "category": "string",
            "brand": "string",
            "image": "string",
            "color": "string",
            "price": 0,
            "priceCurrency": "string",
            "country": "string",
            "size": "string",
            "gender": "string",
            "url": "string",
            "storeType": "sneakerhead",
            "product": "string"
         },
         {
            "name": "string",
            "sku": "string",
            "category": "string",
            "brand": "string",
            "image": "string",
            "color": "string",
            "price": 0,
            "priceCurrency": "string",
            "country": "string",
            "size": "string",
            "gender": "string",
            "url": "string",
            "storeType": "footbox",
            "product": "string",
            "composition": "string",
            "coloring": "string"
         }
      ],
      "number": 0,
      "sort": {
         "empty": true,
         "sorted": true,
         "unsorted": true
      },
      "first": true,
      "last": true,
      "numberOfElements": 0,
      "pageable": {
         "offset": 0,
         "sort": {
            "empty": true,
            "sorted": true,
            "unsorted": true
         },
         "pageSize": 0,
         "pageNumber": 0,
         "paged": true,
         "unpaged": true
      },
      "empty": true
   }
   
   ```
   
   Поддерживается пагинация. Чтобы
   настроить разбитие на страницы, нужно передать параметры: "page" - номер
   страницы и "size" - количество товаров на странице.

В проекте использовались:
1. Spring Boot
2. Spring Cloud
3. Spring Data
4. Spring Security 
5. Springdoc 
6. PostgreSQL 
7. jsoup

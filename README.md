# StoreCrawler
 Application for crawling store pages

1. GET-запрос на адрес "crawlers/sneakerhead/start" - запускает краулер
   для сайта Sneakerhead. Краулер будет обходить сайт раз в сутки и
   обновлять информацию о товарах в базе данных.
2. GET-запрос на адрес "crawlers/sneakerhead/stop" - прерывает работу
   краулера для сайта Sneakerhead. Отменяет ежедневное сканирование.
3. GET-запрос на адрес "crawlers/sneakerhead/scan" - запускает разовое
   сканирование.
4. GET-запрос на адрес "crawlers/sneakerhead/stop_scan" - прерывает
   происходящее сканирование.
5. GET-запрос на адрес "crawlers/sneakerhead/products" - в ответе
   приходит JSON со всеми товарами с сайта Sneakerhead
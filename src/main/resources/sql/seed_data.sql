USE shopping_cart_localization;

-- Insert supported languages
INSERT INTO languages (code, country) VALUES
    ('en', 'US'),
    ('fi', 'FI'),
    ('sv', 'SE'),
    ('ja', 'JP'),
    ('ar', 'AR');

-- Insert content keys
INSERT INTO content (content_key) VALUES
    ('app.title'),
    ('select.language'),
    ('confirm.language'),
    ('enter.number.of.items'),
    ('number.of.items.prompt'),
    ('enter.items'),
    ('enter.quantity'),
    ('quantity.prompt'),
    ('enter.price'),
    ('price.prompt'),
    ('ok.button'),
    ('calculate.total'),
    ('total.cost'),
    ('error.title'),
    ('error.invalid.number'),
    ('error.invalid.input');

-- Insert English translations
INSERT INTO translations (content_id, language_id, translated_text)
SELECT c.id, l.id, t.txt
FROM content c
         JOIN languages l ON l.code='en' AND l.country='US'
         JOIN (
    SELECT 'app.title', 'Swostika Lama / Shopping Cart App' UNION ALL
    SELECT 'select.language', 'Select the language :' UNION ALL
    SELECT 'confirm.language', 'Confirm Button' UNION ALL
    SELECT 'enter.number.of.items', 'Enter the number of items :' UNION ALL
    SELECT 'number.of.items.prompt', 'Enter the number of items' UNION ALL
    SELECT 'enter.items', 'Enter Items' UNION ALL
    SELECT 'enter.quantity', 'Enter Quantity :' UNION ALL
    SELECT 'quantity.prompt', 'Enter number of quantity' UNION ALL
    SELECT 'enter.price', 'Enter Price :' UNION ALL
    SELECT 'price.prompt', 'Enter price' UNION ALL
    SELECT 'ok.button', 'Ok' UNION ALL
    SELECT 'calculate.total', 'Calculate Total :' UNION ALL
    SELECT 'total.cost', 'Total Cost is :' UNION ALL
    SELECT 'error.title', 'Error' UNION ALL
    SELECT 'error.invalid.number', 'Please enter a valid positive number of items.' UNION ALL
    SELECT 'error.invalid.input', 'Please enter valid numeric values for price and quantity.'
) t(k, txt) ON c.content_key = t.k;
-- Arabic translations
INSERT INTO translations (content_id, language_id, translated_text)
SELECT c.id, l.id, t.txt
FROM content c
         JOIN languages l ON l.code='ar' AND l.country='AR'
         JOIN (
    SELECT 'app.title', 'تطبيق سلة التسوق' UNION ALL
    SELECT 'select.language', 'اختر اللغة :' UNION ALL
    SELECT 'confirm.language', 'زر التأكيد' UNION ALL
    SELECT 'enter.number.of.items', 'أدخل عدد العناصر :' UNION ALL
    SELECT 'number.of.items.prompt', 'أدخل عدد العناصر' UNION ALL
    SELECT 'enter.items', 'أدخل العناصر' UNION ALL
    SELECT 'enter.quantity', 'أدخل الكمية :' UNION ALL
    SELECT 'quantity.prompt', 'أدخل عدد الكمية' UNION ALL
    SELECT 'enter.price', 'أدخل السعر :' UNION ALL
    SELECT 'price.prompt', 'أدخل السعر' UNION ALL
    SELECT 'ok.button', 'موافق' UNION ALL
    SELECT 'calculate.total', 'احسب المجموع :' UNION ALL
    SELECT 'total.cost', 'إجمالي التكلفة :' UNION ALL
    SELECT 'error.title', 'خطأ' UNION ALL
    SELECT 'error.invalid.number', 'يرجى إدخال عدد صحيح من العناصر.' UNION ALL
    SELECT 'error.invalid.input', 'يرجى إدخال قيم رقمية صالحة للسعر والكمية.'
) t(k, txt) ON c.content_key = t.k;

-- Japanese translations
INSERT INTO translations (content_id, language_id, translated_text)
SELECT c.id, l.id, t.txt
FROM content c
         JOIN languages l ON l.code='ja' AND l.country='JP'
         JOIN (
    SELECT 'app.title', 'ショッピングカートアプリ' UNION ALL
    SELECT 'select.language', '言語を選択 :' UNION ALL
    SELECT 'confirm.language', '確認ボタン' UNION ALL
    SELECT 'enter.number.of.items', 'アイテムの数を入力 :' UNION ALL
    SELECT 'number.of.items.prompt', 'アイテムの数を入力' UNION ALL
    SELECT 'enter.items', 'アイテムを入力' UNION ALL
    SELECT 'enter.quantity', '数量を入力 :' UNION ALL
    SELECT 'quantity.prompt', '数量を入力' UNION ALL
    SELECT 'enter.price', '価格を入力 :' UNION ALL
    SELECT 'price.prompt', '価格を入力' UNION ALL
    SELECT 'ok.button', 'OK' UNION ALL
    SELECT 'calculate.total', '合計を計算 :' UNION ALL
    SELECT 'total.cost', '合計金額 :' UNION ALL
    SELECT 'error.title', 'エラー' UNION ALL
    SELECT 'error.invalid.number', '有効な正のアイテム数を入力してください。' UNION ALL
    SELECT 'error.invalid.input', '価格と数量に有効な数値を入力してください。'
) t(k, txt) ON c.content_key = t.k;

-- Swedish translations
INSERT INTO translations (content_id, language_id, translated_text)
SELECT c.id, l.id, t.txt
FROM content c
         JOIN languages l ON l.code='sv' AND l.country='SE'
         JOIN (
    SELECT 'app.title', 'Shoppingvagn App' UNION ALL
    SELECT 'select.language', 'Välj språk :' UNION ALL
    SELECT 'confirm.language', 'Bekräfta knapp' UNION ALL
    SELECT 'enter.number.of.items', 'Ange antal objekt :' UNION ALL
    SELECT 'number.of.items.prompt', 'Ange antal objekt' UNION ALL
    SELECT 'enter.items', 'Ange objekt' UNION ALL
    SELECT 'enter.quantity', 'Ange kvantitet :' UNION ALL
    SELECT 'quantity.prompt', 'Ange antal kvantitet' UNION ALL
    SELECT 'enter.price', 'Ange pris :' UNION ALL
    SELECT 'price.prompt', 'Ange pris' UNION ALL
    SELECT 'ok.button', 'OK' UNION ALL
    SELECT 'calculate.total', 'Beräkna totalt :' UNION ALL
    SELECT 'total.cost', 'Totalkostnad :' UNION ALL
    SELECT 'error.title', 'Fel' UNION ALL
    SELECT 'error.invalid.number', 'Ange ett giltigt positivt antal objekt.' UNION ALL
    SELECT 'error.invalid.input', 'Ange giltiga numeriska värden för pris och kvantitet.'
) t(k, txt) ON c.content_key = t.k;

-- For finnish translations
INSERT INTO translations (content_id, language_id, translated_text)
SELECT c.id, l.id, t.txt
FROM content c
         JOIN languages l ON l.code='fi' AND l.country='FI'
         JOIN (
    SELECT 'app.title', 'Ostoskärry Sovellus' UNION ALL
    SELECT 'select.language', 'Valitse kieli :' UNION ALL
    SELECT 'confirm.language', 'Vahvista painike' UNION ALL
    SELECT 'enter.number.of.items', 'Syötä tuotteiden määrä :' UNION ALL
    SELECT 'number.of.items.prompt', 'Syötä tuotteiden määrä' UNION ALL
    SELECT 'enter.items', 'Syötä tuotteet' UNION ALL
    SELECT 'enter.quantity', 'Syötä määrä :' UNION ALL
    SELECT 'quantity.prompt', 'Syötä määrän numero' UNION ALL
    SELECT 'enter.price', 'Syötä hinta :' UNION ALL
    SELECT 'price.prompt', 'Syötä hinta' UNION ALL
    SELECT 'ok.button', 'OK' UNION ALL
    SELECT 'calculate.total', 'Laske yhteensä :' UNION ALL
    SELECT 'total.cost', 'Kokonaishinta :' UNION ALL
    SELECT 'error.title', 'Virhe' UNION ALL
    SELECT 'error.invalid.number', 'Syötä kelvollinen positiivinen määrä tuotteita.' UNION ALL
    SELECT 'error.invalid.input', 'Syötä kelvolliset numerot hinnalle ja määrälle.'
) t(k, txt) ON c.content_key = t.k;
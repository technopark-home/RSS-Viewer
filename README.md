# RSS-Viewer

## Обзор функциональности

# Агрегатор RSS, обладающий следующими возможностями для пользователей:

- Возможность выбора канала RSS
- Добавление статей в избранное для последующего просмотра
- Сохранение с возможностью прочтения при отсутствии интернета
- Фильтрация контента по выбранным категориям
- Возможность фильтровать прочитанное
- Сквозной поиск по заголовку и описанию

## Используемые технологии
- **Kotlin: Datetime, Serialization** и как язык программирования проекта
- **Room** для локальной базы данных
- **Fts4** для **Fast Search** поиска контента
- **Retrofit + XmlPullParser** для сетевого взаимодействия
- **Dagger & Hilt** в качестве **DI** для организации архитектуры приложения (KSP)
- **UI** на **Jetpack Compose** как основное оформление использование **Material theme 3** + icons (навигация **Navigation Compose**)
- Табличное представление **ScrollableTabRow + LazyColumn**, обновление данных **PullToRefreshBox**
- **DataStore<Preferences> + Gson** - хранение пользовательских настроек
- **Kotlin Coroutines** для асинхронных операций + **Flow**
- **WebView** - просмотр, сохранение локальных данных
- **Coil3** - загрузка графический изображений ленты


<img src="https://github.com/user-attachments/assets/63598881-abc5-4818-88d0-e5cd784bd980" width="248">
<img src="https://github.com/user-attachments/assets/19f15364-d3e5-405a-a4e3-9f9611953fb0" width="248">
<img src="https://github.com/user-attachments/assets/0db48aee-0a1c-4224-afa6-6179533ec9d0" width="248">
<img src="https://github.com/user-attachments/assets/9cfd7e6c-5e1b-4039-ba03-96534c5a4e27" width="248">


<img src="https://github.com/user-attachments/assets/495101a7-fb29-49fd-b485-a729d8ccd53d" width="248">

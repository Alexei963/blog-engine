# **Итоговый проект «Блоговый движок»**

Проект представляет из себя веб приложение, в котором пользователи могут опубликовать блоги на разные темы.

Приложение выглядит так:

![](https://github.com/Alexei963/blog-engine/blob/master/images/main%20page.png?raw=true)

В приложении реализовано:

- Регистрация пользователей;

![](https://github.com/Alexei963/blog-engine/blob/master/images/registration.png?raw=true)

- Под постом можно поставить лайк, дизлайк или комментарий;

![](https://github.com/Alexei963/blog-engine/blob/master/images/post.png?raw=true)
![](https://github.com/Alexei963/blog-engine/blob/master/images/comments.png?raw=true)

- Можно увидеть количество просмотров поста;
- В профиле пользователь может изменить свои данные (фото, ФИО, email, пароль);

![](https://github.com/Alexei963/blog-engine/blob/master/images/my%20profile.png?raw=true)

- К своему посту пользователь может добавить тег, и по этому тегу можно увидеть посты на эту тематику;
- Если пользователь является модератором – он может утверждать, отклонять, редактировать новый посты;

![](https://github.com/Alexei963/blog-engine/blob/master/images/moderation.png?raw=true)

- Так же модератор может изменять настройки приложения;

![](https://github.com/Alexei963/blog-engine/blob/master/images/settings.png?raw=true)

- Если пользователь забыл пароль для входа, он может его восстановить.

![](https://github.com/Alexei963/blog-engine/blob/master/images/password%20recovery.png?raw=true)

- На email указанный при регистрации придет ссылка на восстановление пароля. Когда пользователь перейдет по ссылке он попадет на форму восстановления пароля;

![](https://github.com/Alexei963/blog-engine/blob/master/images/password%20change.png?raw=true)

# Cтек используемых технологий.

Проект реализован на фреймворке SpringBoot. Авторизация реализована с помощью SpringSecurity. В качестве базы данных использовал MySQL.

# Запуск приложения.

1. Чтобы запустить проект локально у себя на компьютере – склонируйте проект.
2. Укажите параметры базы данных, в которой будут храниться данные. В файле application.yml нужно заполнить поля:

- datasource.url (путь к вашей базе);
- datasource.username (имя пользователя);
- datasource.password (пароль к базе);

1. Для отправки сообщений для восстановления пароля нужно заполнить поля:

- spring.mail.host (хост);
- spring.mail.port (порт);
- spring.mail.username (email адрес);
- spring.mail.password (пароль);

1. В проекте настроено логгирование. По умолчанию логи записываются в корень проекта. Но можно изменить директорию прописав путь в поле logging.file.name.
2. Для изменения директории куда будут сохраняться изображения можно изменить поле upload.path.

Для демонстрации я разместил проект на Heroku. Базу данных разместил на хостинге beget.com.

Ссылка на деплой https://aleksey-app-blog.herokuapp.com/

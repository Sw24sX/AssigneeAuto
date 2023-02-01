slack env https://slack.dev/java-slack-sdk/guides/getting-started-with-bolt

Для Подключения Slack и приложения:

- устанавливаем `SLACK_SIGNING_SECRET` (Настройки приложения -> Basic Information
   -> App Credentials -> Signing Secret)
- устанавливаем `SLACK_BOT_TOKEN` (Настройки приложения -> OAuth & Permissions -> Bot User OAuth Token (появится после публикации приложения. Для этого я добавил разешения `chat:write`, `channels:read`, `commands`))

В настройках добавляем следующие параметры:

- Настройки приложения -> Slash Commands. Добавляем весь список команд (список будет позднее). 
Для каждой команды необходимо указать url, сервера, куда будет стучаться Slack, формата `https://{your_domain}/slack/events`
- Настройки приложения -> Interactivity & Shortcuts. Включить, если выключено, вписать `Request URL` формата `https://{your_domain}/slack/events`

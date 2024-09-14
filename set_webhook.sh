#!/bin/bash
# shellcheck disable=SC2046
export $(grep -v '^#' .env | xargs)

echo "$URL"

curl -X POST "$BASE_URL$TELEGRAM_BOT_TOKEN/setWebhook" -d "url=$URL/webhook"
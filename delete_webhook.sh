#!/bin/bash
# shellcheck disable=SC2046
export $(grep -v '^#' .env | xargs)

curl -s -X POST "$BASE_URL$TELEGRAM_BOT_TOKEN/deleteWebhook"
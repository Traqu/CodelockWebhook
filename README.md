# CodelockWebhook

CodelockWebhook is a tool designed to monitor and report code lock violations on your game server environment.

Hourly, it goes through codelock **Access** logs and saves a MD5 hash for each violation it finds.
- All (*non-present already*) violations  for a player will be saved, but only the first one will be reported, to avoid `overflooding` your webhook channel
- Hashes are then used to not report same violation multiple times
  - This leads to an issue, where player keeps violating the same set of gates and those will not get reported
  - An Admin should resolve related multibasing issue asap
    - This can be ommited by including `timestamp` from the access logs into the string that is later hashed, but such solution would generate a report hourly (pessimistic), provided that *THE* player would keep abusing same set of gates
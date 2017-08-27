# Synchronized MP3 Playing Across Multiple Clients

Target:
- Server plays its own .mp3 song.
- Server sends .mp3 file to all clients.
- Periodic time updates, as well as start/pause/next commands keep server and clients synced.


1st Step:
- Send an .mp3 file over python sockets.
- Make sure the songs are still playable.

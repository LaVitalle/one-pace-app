package com.onepace.app.core.network

object ApiConstants {
    const val BASE_URL = "https://superflixapi.rest"
    const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"
    const val TMDB_IMAGE_URL = "https://image.tmdb.org/t/p/"
    const val TMDB_API_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NDFkNzg4MjJiMjQ0YmU2Y2U1NDY3NWNmYjQ1ZmM1ZiIsIm5iZiI6MTc3NTE1MjM5Ni41NjE5OTk4LCJzdWIiOiI2OWNlYWQwYzhkYzlkYzgyZDA0YjUyMjkiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.vccLxwjgnBJXZcdLWua_tG8baSqoqW2ni5HrXYRvFDY"
    const val ONE_PIECE_ID = 37854

    fun backdropUrl(path: String): String = "${TMDB_IMAGE_URL}w1280$path"
    fun posterUrl(path: String): String = "${TMDB_IMAGE_URL}w342$path"
    fun stillUrl(path: String): String = "${TMDB_IMAGE_URL}w300$path"
}
